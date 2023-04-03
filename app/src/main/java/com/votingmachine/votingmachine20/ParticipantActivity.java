package com.votingmachine.votingmachine20;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticipantActivity extends AppCompatActivity implements OnParticipantClick {


    //Variable initialisation...............
    private RecyclerView recyclerViewPA;
    private ProgressBar progressBarPA;
    private Button addNewParti;
    private DatabaseReference databaseReferencePA;
    private FirebaseDatabase firebaseDatabasePA;
    private FirebaseAuth firebaseAuthPA;
    private FirebaseUser firebaseUserPA;
    private ParticipantAdapter pA;
    private String panelName;
    private Button saveAN;
    private ArrayList<Participant> cList = new ArrayList<>();
    private ArrayList<String> voterListH = new ArrayList<>();
    private int sN0 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_addition);



        //Stuff..................
        saveAN = findViewById(R.id.save_and_next);
        recyclerViewPA = findViewById(R.id.participant_addition_recycler);
        addNewParti = findViewById(R.id.participant_addition_button);
        progressBarPA = findViewById(R.id.progressBar3);
        progressBarPA.setVisibility(View.VISIBLE);



        //FireBase Stuff.................
        firebaseAuthPA = FirebaseAuth.getInstance();
        firebaseUserPA = firebaseAuthPA.getCurrentUser();
        firebaseDatabasePA = FirebaseDatabase.getInstance();
        databaseReferencePA = firebaseDatabasePA.getReference("PanelInformation");





        //Getting Data From Previous Activity..........................
        try {

            Intent ii = getIntent();
            cList = (ArrayList<Participant>) ii.getSerializableExtra("CList");
            panelName = ii.getStringExtra("PanelName");
            sN0 = cList.size() + 1;
            progressBarPA.setVisibility(View.INVISIBLE);

        } catch (Exception e) {

            progressBarPA.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }



        try {
            pA = new ParticipantAdapter(cList, ParticipantActivity.this, this);
            recyclerViewPA.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPA.setAdapter(pA);
        } catch (Exception e) {
            e.printStackTrace();
        }


        addNewParti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    addNewParti.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    addNewParti.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }




                return false;
            }
        });

        //Adding New Participants..........................
        addNewParti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantActivity.this);
                View v = LayoutInflater.from(ParticipantActivity.this).inflate(R.layout.participant_n_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                progressBarPA.setVisibility(View.VISIBLE);
                                EditText name = v.findViewById(R.id.editTextTextPersonName);
                                String nOp = name.getText().toString().toUpperCase();

                                if (!TextUtils.isEmpty(nOp)) {


                                    Participant cP = new Participant(cList.size()+1, nOp, -1);
                                    cList.add(cP);
                                    pA.notifyItemInserted(cList.size()-1);
                                    recyclerViewPA.scrollToPosition(cList.size()-1);
                                    progressBarPA.setVisibility(View.INVISIBLE);


                                } else {

                                    progressBarPA.setVisibility(View.INVISIBLE);
                                }


                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        }).create().show();
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                Participant deletedT = cList.get(viewHolder.getAdapterPosition());
                cList.remove(viewHolder.getAdapterPosition());
                pA.notifyItemRemoved(viewHolder.getAdapterPosition());
                for(int i = 0; i<cList.size(); i++){
                    cList.get(i).setSerialNo(i+1);
                }
                pA.notifyDataSetChanged();

                Snackbar.make(recyclerViewPA,deletedT.nameOFP,Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletedT.setSerialNo(cList.size()+1);
                cList.add(deletedT);
                pA.notifyItemInserted(cList.size()-1);

            }
        }).show();



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewPA);




        saveAN.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    saveAN.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    saveAN.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });


        //Saving the Data of Participants......................
        saveAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cList.size() >= 2) {


                    progressBarPA.setVisibility(View.VISIBLE);
                    databaseReferencePA.child(firebaseUserPA.getDisplayName()).child("Panels").child(panelName).child("Participants")
                            .setValue(cList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {


                                databaseReferencePA.child(firebaseUserPA.getDisplayName()).child("Panels").child(panelName).child("VoterList")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                voterListH.clear();
                                                if (snapshot.exists()) {

                                                    for (DataSnapshot kl : snapshot.getChildren()) {

                                                        voterListH.add(kl.getValue().toString());
                                                    }


                                                    progressBarPA.setVisibility(View.INVISIBLE);
                                                    Intent ip = new Intent(ParticipantActivity.this, VoterList.class);
                                                    ip.putExtra("VoterList", voterListH);
                                                    ip.putExtra("PanelName", panelName);
                                                    startActivity(ip);


                                                } else {
                                                    progressBarPA.setVisibility(View.INVISIBLE);
                                                    Intent ip = new Intent(ParticipantActivity.this, VoterList.class);
                                                    ip.putExtra("PanelName", panelName);
                                                    startActivity(ip);
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            } else {
                                progressBarPA.setVisibility(View.INVISIBLE);
                                Toast.makeText(ParticipantActivity.this, "Please try again !!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(ParticipantActivity.this, "Minimum 2 Participants Are Required", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }



    @Override
    public void onPartiClick(int item) {

    }


}
