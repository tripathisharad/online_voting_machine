package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class PanelInformation extends AppCompatActivity implements OnItemClickListener {

    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private PanelInformatinAdapter panelInformatinAdapter;
    private Button addNewPannel;
    private FirebaseDatabase databaseP;
    private DatabaseReference databaseReferenceP;
    private FirebaseAuth currentAuthP;
    private FirebaseUser currentUserP;
    private ProgressBar progressBarP;
    private String panelName;
    private ArrayList<String> panelInFoList = new ArrayList<>();
    private ArrayList<Participant> cPList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_information);
        constraintLayout = findViewById(R.id.panel_info_cons);

        addNewPannel = findViewById(R.id.add_new_pannel);
        progressBarP = findViewById(R.id.progressBar4);
        progressBarP.setVisibility(View.VISIBLE);


        //Checking for existing panels..............
        try {

            Intent pIntent = getIntent();
            panelName = pIntent.getStringExtra("DN");
            ArrayList<String> ccc = pIntent.getStringArrayListExtra("dList");
            panelInFoList = ccc;
            panelInformatinAdapter.notifyDataSetChanged();
            progressBarP.setVisibility(View.INVISIBLE);


        } catch (Exception e) {

            progressBarP.setVisibility(View.INVISIBLE);
            e.printStackTrace();

        }


        panelInformatinAdapter = new PanelInformatinAdapter(panelInFoList, this, this);
        recyclerView = findViewById(R.id.recycler_pannel_creation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(panelInformatinAdapter);


        databaseP = FirebaseDatabase.getInstance();
        currentAuthP = FirebaseAuth.getInstance();
        currentUserP = currentAuthP.getCurrentUser();
        databaseReferenceP = databaseP.getReference("PanelInformation");


        //Dialog for Adding New Panels.............
        addNewPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PanelInformation.this);
                View v = LayoutInflater.from(PanelInformation.this).inflate(R.layout.pannel_name_adder_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                progressBarP.setVisibility(View.VISIBLE);
                                EditText name = v.findViewById(R.id.editTextTextPersonName222);
                                String nOp = name.getText().toString().toUpperCase();

                                if (!TextUtils.isEmpty(nOp)) {


                                    databaseReferenceP.child(currentUserP.getDisplayName()).child("Panels").child(nOp).setValue("null")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        panelInFoList.add(nOp);
                                                        panelInformatinAdapter.notifyItemInserted(panelInFoList.size() - 1);
                                                        recyclerView.scrollToPosition(panelInFoList.size() - 1);
                                                        progressBarP.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        progressBarP.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(PanelInformation.this, "Please try again !!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                } else {
                                    progressBarP.setVisibility(View.INVISIBLE);
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



        addNewPannel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    addNewPannel.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    addNewPannel.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }





                return false;
            }
        });











        //Delete function using swipe helper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {



                String deletedT = panelInFoList.get(viewHolder.getAdapterPosition());
                panelInFoList.remove(viewHolder.getAdapterPosition());
                panelInformatinAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());



                AlertDialog.Builder builder = new AlertDialog.Builder(PanelInformation.this);
                builder.setMessage("Do you want to delete this panel ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {


                    progressBarP.setVisibility(View.VISIBLE);
                    databaseReferenceP.child(currentUserP.getDisplayName()).child("Panels").child(deletedT).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {




                            if (task.isSuccessful()) {

                                progressBarP.setVisibility(View.INVISIBLE);
                            } else {
                                progressBarP.setVisibility(View.INVISIBLE);
                            }

                        }
                    });



                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {



                    panelInFoList.add(deletedT);
                    panelInformatinAdapter.notifyItemInserted(panelInFoList.size()-1);
                    dialog.cancel();


                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();





            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);





    }


    @Override
    public void onItemClick(int item, String s) {

        progressBarP.setVisibility(View.VISIBLE);

        try {


            databaseReferenceP.child(currentUserP.getDisplayName()).child("Panels").child(s).child("Published").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        progressBarP.setVisibility(View.INVISIBLE);
                        Toast.makeText(PanelInformation.this, "Panel has published and can't be edited", Toast.LENGTH_SHORT).show();
                    } else {


                        try {

                            databaseReferenceP.child(currentUserP.getDisplayName()).child("Panels").child(panelInFoList.get(item)).child("Participants").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    cPList.clear();
                                    if (snapshot.exists()) {


                                        for (DataSnapshot k : snapshot.getChildren()) {

                                            Participant cPPP = k.getValue(Participant.class);
                                            cPList.add(cPPP);

                                        }


                                        Intent pii = new Intent(PanelInformation.this, ParticipantActivity.class);
                                        pii.putExtra("CList", cPList);
                                        pii.putExtra("PanelName", s);
                                        progressBarP.setVisibility(View.INVISIBLE);
                                        startActivity(pii);


                                    } else {

                                        Intent pii = new Intent(PanelInformation.this, ParticipantActivity.class);
                                        pii.putExtra("CList", cPList);
                                        pii.putExtra("PanelName", s);
                                        progressBarP.setVisibility(View.INVISIBLE);
                                        startActivity(pii);
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBarP.setVisibility(View.INVISIBLE);

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            progressBarP.setVisibility(View.INVISIBLE);

        }


    }


}