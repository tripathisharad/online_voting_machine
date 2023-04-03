package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements OnItemClickListener {

    private FirebaseAuth authResult;
    private FirebaseUser firebaseUserResult;
    private FirebaseDatabase databaseResult;
    private DatabaseReference databaseReferenceResult;
    private ArrayList<String> result_list = new ArrayList<>();
    private PanelInformatinAdapter panelInformatinAdapter;
    private RecyclerView recyclerView;
    private ProgressBar result_progress;
    private String panelNameFR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        
        
        recyclerView = findViewById(R.id.result_recycler);
        result_progress = findViewById(R.id.progressBar8);
        result_progress.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        panelInformatinAdapter = new PanelInformatinAdapter(result_list, ResultsActivity.this,ResultsActivity.this);
        recyclerView.setAdapter(panelInformatinAdapter);

        authResult = FirebaseAuth.getInstance();
        firebaseUserResult = authResult.getCurrentUser();
        databaseResult = FirebaseDatabase.getInstance();
        databaseReferenceResult = databaseResult.getReference("PanelInformation");



        try{

            Intent i = getIntent();
            panelNameFR = i.getStringExtra("PN");

        }catch (Exception e){
            result_progress.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

        
        try{
            
            databaseReferenceResult.child(panelNameFR).child("Panels").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    result_list.clear();
                    if(snapshot.exists()){


                        for( DataSnapshot i : snapshot.getChildren()){
                            String gg = i.getKey().toString();
                            if(i.child("Results").exists()){
                                result_list.add(gg);
                            }

                        }
                        
                        
                        panelInformatinAdapter.notifyDataSetChanged();
                        result_progress.setVisibility(View.INVISIBLE);

                    }else{
                        Toast.makeText(ResultsActivity.this, "No Panel Found", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            
            
        }catch (Exception e){
            result_progress.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClick(int item,String s) {

        result_progress.setVisibility(View.VISIBLE);
        try{

            Intent in = new Intent(ResultsActivity.this,ResultDeclaration.class);
            in.putExtra("PN",panelNameFR);
            in.putExtra("PNO",s);
            result_progress.setVisibility(View.INVISIBLE);
            startActivity(in);

           }catch (Exception e){
            result_progress.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }



    }
}