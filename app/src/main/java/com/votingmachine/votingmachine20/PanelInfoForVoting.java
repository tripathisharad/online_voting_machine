package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojoauth.android.api.MojoAuthApi;
import com.mojoauth.android.handler.AsyncHandler;
import com.mojoauth.android.helper.ErrorResponse;
import com.mojoauth.android.helper.MojoAuthSDK;
import com.mojoauth.android.models.responsemodels.LoginResponse;
import com.mojoauth.android.models.responsemodels.UserResponse;

import java.util.ArrayList;
import java.util.Calendar;

public class PanelInfoForVoting extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView reFV;
    private FirebaseAuth firebaseAuth;
    private PanelInformatinAdapter panelInformatinAdapter;
    private FirebaseDatabase databasePFV;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReferencePFV;
    private String uDN;
    private ProgressBar pFVV,pFVDP;
    private Button getOtp;
    private ArrayList<String> panelInFoListFV = new ArrayList<>();
    private ArrayList<String> voterListFV = new ArrayList<>();
    private final Calendar c = Calendar.getInstance();
    private int cDay, cMonth, cYear;
    private String oDOV;
    private View v;
    public static String SID = "1001";
    public static boolean VERIFIED = false;
    private EditText userET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_info_for_voting);
        reFV = findViewById(R.id.recy_panel_for_voting);
        pFVV = findViewById(R.id.progressBar6FV);
        pFVV.setVisibility(View.VISIBLE);
        reFV.setLayoutManager(new LinearLayoutManager(this));
        databasePFV = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferencePFV = databasePFV.getReference("PanelInformation");
        cDay = c.get(Calendar.DAY_OF_MONTH);
        cMonth = c.get(Calendar.MONTH);
        cYear = c.get(Calendar.YEAR);
        oDOV = String.valueOf(cDay) + "/" + String.valueOf(cMonth + 1) + "/" + String.valueOf(cYear);


        // Email Authentication Api...........
        MojoAuthSDK.Initialize init = new MojoAuthSDK.Initialize();
        init.setApiKey("4298d09b-57f4-47d2-8643-d1bdb0983ceb");


        try {

            Intent pIntent = getIntent();
            uDN = pIntent.getStringExtra("DN");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Fetching Existence list..................
        if(firebaseUser != null){
            try{

                databaseReferencePFV.child(uDN).child("Panels").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot d : snapshot.getChildren()){

                            String pN = d.getKey().toString().toUpperCase();
                            if (d.child("Published").exists()){
                                panelInFoListFV.add(pN);
                            }

                        }

                        panelInformatinAdapter.notifyDataSetChanged();
                        pFVV.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }catch (Exception e){
                e.printStackTrace();
                pFVV.setVisibility(View.INVISIBLE);
            }
        }else{

            try{

                databaseReferencePFV.child(uDN).child("Panels").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot d : snapshot.getChildren()){

                            String pN = d.getKey().toString().toUpperCase();
                            if (d.child("Published").exists() && d.child("Date of Voting").getValue().equals(oDOV)){
                                panelInFoListFV.add(pN);
                            }

                        }

                        panelInformatinAdapter.notifyDataSetChanged();
                        pFVV.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }catch (Exception e){
                e.printStackTrace();
                pFVV.setVisibility(View.INVISIBLE);
            }



        }

        panelInformatinAdapter = new PanelInformatinAdapter(panelInFoListFV, this, this);
        reFV.setAdapter(panelInformatinAdapter);


    }

    @Override
    public void onItemClick(int item, String s) {


        try {


            AlertDialog.Builder builder = new AlertDialog.Builder(PanelInfoForVoting.this);
            v = LayoutInflater.from(PanelInfoForVoting.this).inflate(R.layout.panel_info_for_voting_dialog, null);
            getOtp = v.findViewById(R.id.button5);
            pFVDP = v.findViewById(R.id.progressBar6);
            pFVDP.setVisibility(View.INVISIBLE);
            userET = v.findViewById(R.id.panel_info_for_voting);
            builder.setView(v)
                    .setCancelable(false)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            pFVV.setVisibility(View.VISIBLE);
                            EditText otpp = v.findViewById(R.id.panel_info_for_voting2);
                            String nOp = userET.getText().toString().trim();
                            String otPPP = otpp.getText().toString().trim();


                            if (!TextUtils.isEmpty(nOp) && !TextUtils.isEmpty(otPPP)) {

                                verifyEmailOTP(otPPP, SID,item,nOp);

                            } else if(TextUtils.isEmpty(nOp) && !TextUtils.isEmpty(otPPP)) {

                                Toast.makeText(PanelInfoForVoting.this, "Invalid E-mail", Toast.LENGTH_SHORT).show();
                                pFVV.setVisibility(View.INVISIBLE);
                                pFVDP.setVisibility(View.INVISIBLE);

                            }else if(!TextUtils.isEmpty(nOp) && TextUtils.isEmpty(otPPP)){
                                Toast.makeText(PanelInfoForVoting.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                pFVV.setVisibility(View.INVISIBLE);
                                pFVDP.setVisibility(View.INVISIBLE);
                            }else{
                                Toast.makeText(PanelInfoForVoting.this, "Invalid Request !!!", Toast.LENGTH_SHORT).show();
                                pFVV.setVisibility(View.INVISIBLE);
                                pFVDP.setVisibility(View.INVISIBLE);
                            }


                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                        }
                    }).create().show();


        } catch (Exception ed) {
            ed.printStackTrace();
            pFVDP.setVisibility(View.INVISIBLE);
        }


        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userET.setEnabled(false);
                pFVDP.setVisibility(View.VISIBLE);
                String email = userET.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {


                    databaseReferencePFV.child(uDN).child("Panels").child(panelInFoListFV.get(item)).child("VoterList").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if(snapshot.exists()){
                                voterListFV.clear();
                                for (DataSnapshot di : snapshot.getChildren()) {
                                    voterListFV.add(di.getValue().toString());
                                }

                                if(voterListFV.contains(email)){
                                    loginByEmailOTP(email);
                                }else{
                                    pFVDP.setVisibility(View.INVISIBLE);
                                    Toast.makeText(PanelInfoForVoting.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                pFVDP.setVisibility(View.INVISIBLE);
                                Toast.makeText(PanelInfoForVoting.this, "Invalid Request !!!", Toast.LENGTH_SHORT).show();
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    pFVDP.setVisibility(View.INVISIBLE);
                    Toast.makeText(PanelInfoForVoting.this, "Enter valid E-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getOtp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getOtp.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                    getOtp.setTextColor(Color.parseColor("#FF000000"));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    getOtp.setTextColor(Color.parseColor("#FFFFFFFF"));
                    getOtp.setBackground(getResources().getDrawable(R.drawable.register_round_button));

                }


                return false;
            }
        });
    }

    public void verifyEmailOTP(String o, String sId,int item,String userEmail) {
        MojoAuthApi mojoAuthApi = new MojoAuthApi();
        mojoAuthApi.verifyEmailOTP(o, sId, new AsyncHandler<UserResponse>() {
            @Override
            public void onFailure(ErrorResponse error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pFVV.setVisibility(View.INVISIBLE);
                        Toast.makeText(PanelInfoForVoting.this, error.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(UserResponse data) {

                try {

                    databaseReferencePFV.child(uDN).child("Panels").child(panelInFoListFV.get(item)).child("Date of Voting").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String dOV = snapshot.getValue().toString();
                            if (oDOV.equals(dOV)) {

                                pFVV.setVisibility(View.INVISIBLE);
                                Intent ij = new Intent(PanelInfoForVoting.this, VotingBooth.class);
                                ij.putExtra("UD", uDN);
                                ij.putExtra("PN", panelInFoListFV.get(item));
                                ij.putExtra("CV", userEmail);
                                pFVDP.setVisibility(View.INVISIBLE);
                                pFVV.setVisibility(View.INVISIBLE);
                                startActivity(ij);

                            } else {
                                pFVDP.setVisibility(View.INVISIBLE);
                                pFVV.setVisibility(View.INVISIBLE);
                                Toast.makeText(PanelInfoForVoting.this, "Invalid Request !!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                    pFVV.setVisibility(View.INVISIBLE);
                }

            }
        });


    }

    public void loginByEmailOTP(String eMail) {

        MojoAuthApi mojoAuthApi = new MojoAuthApi();
        mojoAuthApi.loginByEmailOTP(eMail, new AsyncHandler<LoginResponse>() {
            @Override
            public void onFailure(ErrorResponse error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pFVDP.setVisibility(View.INVISIBLE);
                        Toast.makeText(PanelInfoForVoting.this, "Failed !! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onSuccess(LoginResponse data) {
                SID = data.getStateId();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pFVDP.setVisibility(View.INVISIBLE);
                        Toast.makeText(PanelInfoForVoting.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    static void fetchVoterList() {

    }
}