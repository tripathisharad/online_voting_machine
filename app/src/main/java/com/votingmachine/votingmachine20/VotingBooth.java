package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VotingBooth extends AppCompatActivity implements OnParticipantClickVB {


    private RecyclerView rVB;
    private FirebaseDatabase databasePFVB;
    private DatabaseReference databaseReferencePFVB;
    private VotinBoothAdapter pAA;
    private ProgressBar progressBarAV;
    private ArrayList<Participant> cListVB = new ArrayList<>();
    private ArrayList<String> updatedVL = new ArrayList<>();
    private String pNVB;
    private String uDVB;
    private String cNOP;
    private LottieAnimationView lottieAnimationView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_booth);
        rVB = findViewById(R.id.poling_booth_recy);
        lottieAnimationView = findViewById(R.id.voting_booth_anim);
        progressBarAV = findViewById(R.id.progressBar7);
        progressBarAV.setVisibility(View.INVISIBLE);
        rVB.setLayoutManager(new LinearLayoutManager(this));
        databasePFVB = FirebaseDatabase.getInstance();
        databaseReferencePFVB = databasePFVB.getReference("PanelInformation");




        lottieAnimationView.setVisibility(View.INVISIBLE);
        lottieAnimationView.pauseAnimation();

        pAA = new VotinBoothAdapter(cListVB,this,this);
        rVB.setAdapter(pAA);

        //Fetching the existing participant.........
        try{

            Intent iu = getIntent();
            pNVB = iu.getStringExtra("PN");
            uDVB = iu.getStringExtra("UD");
            cNOP = iu.getStringExtra("CV");

            databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("Participants").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot df : snapshot.getChildren()){

                        Participant cPPP = df.getValue(Participant.class);
                        cListVB.add(cPPP);
                    }

                    pAA.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }





        //
        try {

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {}
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this,"ca-app-pub-7329767452096125/4022021086", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }


        if (mInterstitialAd != null) {
            mInterstitialAd.show(VotingBooth.this);
        }else{

        }


    }

    @Override
    public void onPartiClickVB(int item) {

        progressBarAV.setVisibility(View.VISIBLE);


        try {

            String pNFVC = cListVB.get(item).nameOFP;
            databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("Results").child(pNFVC).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if(snapshot.exists()){

                        String nOV = snapshot.getValue().toString();
                        int aVotes = Integer.parseInt(nOV) + 1;
                        databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("Results").child(pNFVC).setValue(String.valueOf(aVotes)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){



                                    databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("VoterList").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot ij : snapshot.getChildren()){
                                                updatedVL.add(ij.getValue().toString());
                                            }

                                            int in = updatedVL.indexOf(cNOP);
                                            updatedVL.remove(in);


                                            databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("VoterList").setValue(updatedVL)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){

                                                                rVB.setVisibility(View.INVISIBLE);
                                                                progressBarAV.setVisibility(View.INVISIBLE);
                                                                lottieAnimationView.setVisibility(View.VISIBLE);
                                                                lottieAnimationView.playAnimation();

                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(VotingBooth.this, "Happy Voting !", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(VotingBooth.this,DashBoard.class));
                                                                        finish();
                                                                        finishAffinity();
                                                                    }
                                                                }, 2000);





                                                            }else {
                                                                Toast.makeText(VotingBooth.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });








                                }else{
                                    progressBarAV.setVisibility(View.INVISIBLE);
                                    Toast.makeText(VotingBooth.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }else{

                        databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("Results").child(pNFVC).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("VoterList").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot ij : snapshot.getChildren()){
                                                updatedVL.add(ij.getValue().toString());
                                            }

                                            int in = updatedVL.indexOf(cNOP);
                                            updatedVL.remove(in);


                                            databaseReferencePFVB.child(uDVB).child("Panels").child(pNVB).child("VoterList").setValue(updatedVL)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){

                                                                rVB.setVisibility(View.INVISIBLE);
                                                                progressBarAV.setVisibility(View.INVISIBLE);
                                                                lottieAnimationView.setVisibility(View.VISIBLE);
                                                                lottieAnimationView.playAnimation();

                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(VotingBooth.this, "Happy Voting !", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(VotingBooth.this,DashBoard.class));
                                                                        finish();
                                                                        finishAffinity();
                                                                    }
                                                                }, 2000);



                                                            }else {
                                                                Toast.makeText(VotingBooth.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }else{
                                    progressBarAV.setVisibility(View.INVISIBLE);
                                    Toast.makeText(VotingBooth.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                }







                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception er){
            progressBarAV.setVisibility(View.INVISIBLE);
            er.printStackTrace();
        }
    }
}