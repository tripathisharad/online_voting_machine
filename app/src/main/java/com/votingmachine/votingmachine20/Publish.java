package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Publish extends AppCompatActivity {

    private Button chooseDate,publishPanel;
    private LottieAnimationView lottieAnimationView;
    private int mYear, mMonth, mDay;
    private DatabaseReference pubDatabaseReference;
    private FirebaseDatabase pubDatabase;
    private FirebaseAuth pubFirebaseAuth;
    private FirebaseUser pubFirebaseUser;
    private String panelNameP;
    final Calendar c = Calendar.getInstance();
    private ProgressBar progressBar;
    private AdView mAdViewOne,mADViewTwo;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        chooseDate = findViewById(R.id.button3);
        publishPanel = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar11);
        progressBar.setVisibility(View.INVISIBLE);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        //
        try {

            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this,"ca-app-pub-7329767452096125/4022021086", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i("Ad Loaded", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("Ad Not Loaded", loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });


        }catch (Exception e){
              e.printStackTrace();
        }




        //
        try{


            mAdViewOne = findViewById(R.id.adViewPublish);
            mADViewTwo = findViewById(R.id.adViewPublishTwo);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdViewOne.loadAd(adRequest);
            mADViewTwo.loadAd(adRequest);

        }catch (Exception e){
            e.printStackTrace();
        }


        lottieAnimationView = findViewById(R.id.publish_panel_animation);
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.setVisibility(View.INVISIBLE);


        pubDatabase = FirebaseDatabase.getInstance();
        pubFirebaseAuth = FirebaseAuth.getInstance();
        pubFirebaseUser = pubFirebaseAuth.getCurrentUser();
        pubDatabaseReference = pubDatabase.getReference("PanelInformation");




        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);




        try {
            Intent ik = getIntent();
            panelNameP = ik.getStringExtra("PanelName");
        }catch (Exception e){
            e.printStackTrace();
        }

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                chooseDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {


                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                            chooseDate.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                            chooseDate.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                        }



                        return false;
                    }
                });




                chooseDate.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Publish.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {



                                if(i > mYear){
                                    chooseDate.setText(String.valueOf(i2) + "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
                                }else if(i == mYear){

                                    if( i1 > mMonth){
                                        chooseDate.setText( String.valueOf(i2) + "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
                                    }else if( i1 == mMonth){
                                        if( i2 > mDay){

                                            if (mInterstitialAd != null) {
                                                mInterstitialAd.show(Publish.this);
                                            } else {
                                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                            }

                                            chooseDate.setText(String.valueOf(i2) + "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
                                        }else if( i2 == mDay){

                                            if (mInterstitialAd != null) {
                                                mInterstitialAd.show(Publish.this);
                                            } else {
                                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                            }

                                            chooseDate.setText(String.valueOf(i2) + "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
//
                                        }else{
                                            Toast.makeText(Publish.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(Publish.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(Publish.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },mYear,mMonth,mDay);
                        datePickerDialog.show();


                    }
                });

            }
        });


        publishPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    publishPanel.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    publishPanel.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }




                return false;
            }
        });



        publishPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                try {

                   pubDatabaseReference.child(pubFirebaseUser.getDisplayName()).child("Panels").child(panelNameP).child("Date of Voting").setValue(chooseDate.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){

                               pubDatabaseReference.child(pubFirebaseUser.getDisplayName()).child("Panels").child(panelNameP).child("Published").setValue("YES").addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {

                                       if(task.isSuccessful()){

                                           publishPanel.setVisibility(View.INVISIBLE);
                                           chooseDate.setVisibility(View.INVISIBLE);
                                           progressBar.setVisibility(View.INVISIBLE);
                                           lottieAnimationView.playAnimation();
                                           lottieAnimationView.setVisibility(View.VISIBLE);

                                           new Handler().postDelayed(new Runnable() {
                                               @Override
                                               public void run() {

                                                   Toast.makeText(Publish.this, "Panel Published", Toast.LENGTH_SHORT).show();
                                                   startActivity(new Intent(Publish.this,DashBoard.class));
                                                   finish();
                                                   finishAffinity();
                                               }
                                           }, 4000);





                                       }else{
                                           progressBar.setVisibility(View.INVISIBLE);
                                           Toast.makeText(Publish.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                           }

                       }
                   });




               }catch (Exception e){
                   e.printStackTrace();
                   progressBar.setVisibility(View.INVISIBLE);
               }



            }
        });
    }
}