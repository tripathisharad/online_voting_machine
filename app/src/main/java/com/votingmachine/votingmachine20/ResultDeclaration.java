package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class ResultDeclaration extends AppCompatActivity {

    private FirebaseAuth authResultD;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase databaseResultD;
    private DatabaseReference databaseReferenceResultD;
    private RecyclerView recyclerViewD;
    private ProgressBar result_d_progress;
    private String panelNameFRD;
    private final Calendar c = Calendar.getInstance();
    private String ooDOV;
    private KonfettiView konfettiView;
    private Shape.DrawableShape drawableShape = null;
    private String panelCODE,panelNAME;
    private ArrayList<ResultData> resultDataList = new ArrayList<>();
    private ResultDeclarationAdapter resultDeclarationAdapter;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_declaration);

        konfettiView = findViewById(R.id.celebration);
        recyclerViewD = findViewById(R.id.recycler_view_declaration);
        resultDeclarationAdapter = new ResultDeclarationAdapter(this,resultDataList);
        recyclerViewD.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewD.setAdapter(resultDeclarationAdapter);
        progressBar = findViewById(R.id.progressBar9);
        progressBar.setVisibility(View.VISIBLE);
        ooDOV = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + String.valueOf(c.get(Calendar.YEAR));


        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_celebration_24);
        drawableShape = new Shape.DrawableShape(drawable, true);


        authResultD = FirebaseAuth.getInstance();
        firebaseUser = authResultD.getCurrentUser();
        databaseResultD = FirebaseDatabase.getInstance();
        databaseReferenceResultD = databaseResultD.getReference("PanelInformation");



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



       try{

           Intent i = getIntent();
           panelCODE = i.getStringExtra("PN");
           panelNAME = i.getStringExtra("PNO");


       }catch (Exception e){
           e.printStackTrace();
       }


       // Load data
        try {

            databaseReferenceResultD.child(panelCODE).child("Panels").child(panelNAME).child("Results").addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    resultDataList.clear();
                    if(snapshot.exists()){


                        for( DataSnapshot d : snapshot.getChildren()){
                            String userN = d.getKey().toString().toUpperCase();
                            String userV = d.getValue().toString().toUpperCase();
                            resultDataList.add(new ResultData(userN,userV));
                            resultDataList.sort(Comparator.comparing(ResultData::getUserVotes).reversed());
                        }

                    }else{
                        Toast.makeText(ResultDeclaration.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                    resultDeclarationAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    showAnimation();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }catch (Exception e){
            e.printStackTrace();
        }




    }
    public void rain() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(200);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build()
        );
    }

    public void parade() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(50);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 75)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 50f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 75)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 50f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build()
        );
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(5000L, TimeUnit.MILLISECONDS).max(300);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    public void normal(){

        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(3000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12,5f,0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();

        konfettiView.start(party);
    }

    public void showAnimation(){

        int min = 0;
        int max = 5;
        int ran = (int) (Math.random()*(max-min+1)+min);
        switch (ran){

            case 1: rain();
                break;

            case 2: parade();
                break;

            case 3: explode();
                break;

            case 4: normal();
                break;

            default: normal();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mInterstitialAd != null) {
            mInterstitialAd.show(ResultDeclaration.this);
        } else {

        }
    }
}