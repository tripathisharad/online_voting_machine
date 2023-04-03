package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class VoterList extends AppCompatActivity {

    private int REQUEST_CODE = 101;
    private ArrayList<String> curentList = new ArrayList<>();
    private Button addVM,saveAndNext;
    private DatabaseReference databaseReferenceVo;
    private FirebaseDatabase firebaseDatabaseVo;
    private FirebaseAuth firebaseAuthVo;
    private FirebaseUser firebaseUserVo;
    private String panelNameH;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private VoterListAdapter voterListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voterlist);
        recyclerView = findViewById(R.id.voter_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        saveAndNext = findViewById(R.id.save_voter_and_next);
        addVM = findViewById(R.id.add_voter_manually);
        progressBar = findViewById(R.id.progressBar10);
        progressBar.setVisibility(View.VISIBLE);



        //FireBase Stuff..................
        firebaseDatabaseVo = FirebaseDatabase.getInstance();
        databaseReferenceVo = firebaseDatabaseVo.getReference("PanelInformation");
        firebaseAuthVo = FirebaseAuth.getInstance();
        firebaseUserVo = firebaseAuthVo.getCurrentUser();



        //Toolbar Setup..............
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_voterList);
        setSupportActionBar(myToolbar);



        try {

            Intent iii = getIntent();
            curentList = (ArrayList<String>) iii.getSerializableExtra("VoterList");
            panelNameH = iii.getStringExtra("PanelName");
            progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            e.printStackTrace();
            progressBar.setVisibility(View.INVISIBLE);
        }




        if(curentList == null){
            curentList = new ArrayList<>();
        }



       try {

           voterListAdapter = new VoterListAdapter(VoterList.this,curentList);
           recyclerView.setAdapter(voterListAdapter);
           voterListAdapter.notifyDataSetChanged();


       }catch (Exception e){
           e.printStackTrace();
       }



       addVM.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {


               if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                   addVM.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


               } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                   addVM.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
               }


               return false;
           }
       });

        addVM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(VoterList.this);
                View v = LayoutInflater.from(VoterList.this).inflate(R.layout.voter_list_dialog,null);
                builder.setView(v)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                EditText name = v.findViewById(R.id.voter_list_edit_text);
                                String nOp = name.getText().toString();

                                if(!TextUtils.isEmpty(nOp)){

                                    if(!curentList.contains(nOp)){
                                        curentList.add(nOp);
                                        voterListAdapter.notifyItemInserted(curentList.size()-1);
                                        recyclerView.scrollToPosition(curentList.size()-1);
                                    }


                                }else{


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



        //Delete function using swipe helper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                curentList.remove(viewHolder.getAdapterPosition());
                voterListAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),viewHolder.getAdapterPosition());

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        saveAndNext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    saveAndNext.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    saveAndNext.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });

        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(curentList.size() >= 1){

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReferenceVo.child(firebaseUserVo.getDisplayName()).child("Panels").child(panelNameH).child("VoterList").setValue(curentList)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Intent ij = new Intent(VoterList.this,Publish.class);
                                        ij.putExtra("PanelName",panelNameH);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(ij);

                                    }
                                }
                            });

                }else{
                    Toast.makeText(VoterList.this, "Minimum 1 Voter is Required", Toast.LENGTH_SHORT).show();
                }

            }
        });



        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               
                if(item.getItemId() == R.id.export_xml){

                    Intent ih = new Intent(Intent.ACTION_GET_CONTENT);
                    ih.addCategory(Intent.CATEGORY_OPENABLE);
                    ih.setType("*/*");
                    Intent ic = Intent.createChooser(ih,"Text File");
                    startActivityForResult(ic,REQUEST_CODE);

                }
               
               
                return false;
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_voter_list,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if(requestCode == 101){

            try {

                Uri mU = data.getData();
                FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(mU);


                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                Scanner sc = new Scanner(inputStreamReader);

                String sdf = "";
                while (sc.hasNext()){

                    sdf = sdf + sc.nextLine();
                    curentList.add(sdf);
                    Log.d("Voters",sdf);
                    sdf = "";
                }
                voterListAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}