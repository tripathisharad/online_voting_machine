package com.votingmachine.votingmachine20;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ImageView menuIcon;
    private Menu menu;
    private TextView createNew, profileNumber, voteFExisPannel, viewResults, help_s, raiseAT;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase databasePP;
    private DatabaseReference databaseReferencePP;
    private boolean login = false;
    private boolean panelExistance = false;
    private ProgressBar pFV;
    private ArrayList<String> dList = new ArrayList<>();
    private ArrayList<String> currentUserList = new ArrayList<>();
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        bindViews();
        

        
        mAuth = FirebaseAuth.getInstance();
        databasePP = FirebaseDatabase.getInstance();
        databaseReferencePP = databasePP.getReference("PanelInformation");


        //
        try {

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            mAdView = findViewById(R.id.adViewDashboard);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }catch (Exception e){
            e.printStackTrace();
        }





        //
        try {
            firebaseUser = mAuth.getCurrentUser();
            menu = navigationView.getMenu();
            if (firebaseUser != null) {
                login = true;
                profileNumber.setText("+91 " + firebaseUser.getDisplayName());
                menu.clear();
                MenuItem item1 = menu.add("Logout");
                item1.setIcon(R.drawable.ic_baseline_logout_24);
                MenuItem itemClose = menu.add("Close Account");
                itemClose.setIcon(R.drawable.ic_baseline_close_24);
                MenuItem itemHelp = menu.add("Contact us");
                itemHelp.setIcon(R.drawable.ic_baseline_contact_support_24);
                MenuItem itemPrivacy = menu.add("Privacy Policy");
                itemPrivacy.setIcon(R.drawable.ic_baseline_policy_24);





            } else {
                login = false;
                menu.clear();
                MenuItem item2 = menu.add("Login");
                item2.setIcon(R.drawable.ic_baseline_login_24);
                MenuItem item3 = menu.add("Register");
                item3.setIcon(R.drawable.ic_baseline_app_registration_24);
                MenuItem itemHelp = menu.add("Contact us");
                itemHelp.setIcon(R.drawable.ic_baseline_contact_support_24);
                MenuItem itemPrivacy = menu.add("Privacy Policy");
                itemPrivacy.setIcon(R.drawable.ic_baseline_policy_24);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //setup the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isOpen()) {
                    drawerLayout.close();
//
                } else {
                    drawerLayout.open();
//
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle() == "Login") {
                    drawerLayout.close();
                    pFV.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(DashBoard.this, LoginRegisterActivity.class));


                } else if (item.getTitle() == "Register") {
                    drawerLayout.close();
                    pFV.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(DashBoard.this, LoginRegisterActivity.class));

                } else if (item.getTitle() == "Logout") {
                    mAuth.signOut();
                    drawerLayout.close();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);


                } else if (item.getTitle() == "Contact us") {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                    builder.setIcon(R.drawable.ic_baseline_contact_support_24);
                    builder.setMessage("Email - onlinevotingm@gmail.com");
                    builder.setTitle("Contact us");
                    builder.setCancelable(true);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }else if(item.getTitle() == "Privacy Policy"){

                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://funmazaseries.blogspot.com/2022/09/privacy-policy.html"));
                        startActivity(browserIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(item.getTitle() == "Close Account"){

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                    builder.setMessage("Are you sure you want to close this account ?");
                    builder.setTitle("Alert !");
                    builder.setIcon(R.drawable.ic_baseline_close_24);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    try {
                        pFV.setVisibility(View.VISIBLE);
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                databaseReferencePP.child(firebaseUser.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()){




                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                if(task.isSuccessful()){
                                    databaseReferencePP.child(firebaseUser.getDisplayName()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                pFV.setVisibility(View.INVISIBLE);
                                                drawerLayout.close();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition(0, 0);
                                                Toast.makeText(DashBoard.this, "Account Closed", Toast.LENGTH_SHORT).show();
                                            }else {
                                                pFV.setVisibility(View.INVISIBLE);
                                                Toast.makeText(DashBoard.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else{
                                    pFV.setVisibility(View.INVISIBLE);
                                    Toast.makeText(DashBoard.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    });
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {

                        drawerLayout.close();
                        dialog.cancel();


                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

                return true;
            }
        });

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pFV.setVisibility(View.VISIBLE);
                if (login == true) {

                    panelSender(1, firebaseUser.getDisplayName());

                } else {
                    pFV.setVisibility(View.INVISIBLE);
                    Toast.makeText(DashBoard.this, "To Create A Panel You Must Logged In", Toast.LENGTH_SHORT).show();
                }

            }
        });

        createNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    createNew.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    createNew.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });

        voteFExisPannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                View v = LayoutInflater.from(DashBoard.this).inflate(R.layout.dashboard_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                pFV.setVisibility(View.VISIBLE);
                                EditText name = v.findViewById(R.id.dashboard_panelChecker);
                                String nOp = name.getText().toString();

                                if (!TextUtils.isEmpty(nOp)) {
                                    checkPanelExis(nOp, 2);
                                } else {


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

        voteFExisPannel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    voteFExisPannel.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    voteFExisPannel.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });

        viewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                View v = LayoutInflater.from(DashBoard.this).inflate(R.layout.dashboard_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                pFV.setVisibility(View.VISIBLE);
                                EditText name = v.findViewById(R.id.dashboard_panelChecker);
                                String nOp = name.getText().toString();

                                if (!TextUtils.isEmpty(nOp)) {
                                    checkPanelExis(nOp, 3);

                                } else {


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

        viewResults.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    viewResults.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    viewResults.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });

        help_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pFV.setVisibility(View.INVISIBLE);
                startActivity(new Intent(DashBoard.this,HelpSection.class));
            }
        });

        help_s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    help_s.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    help_s.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }


                return false;
            }
        });

        raiseAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pFV.setVisibility(View.INVISIBLE);
                startActivity(new Intent(DashBoard.this,RaiseTicket.class));


            }
        });

        raiseAT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    raiseAT.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    raiseAT.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    void bindViews() {

        menuIcon = findViewById(R.id.menu_icon);
        createNew = findViewById(R.id.create_a_new);
        navigationView = findViewById(R.id.navigation_view);
        View v = navigationView.getHeaderView(0);
        pFV = findViewById(R.id.progressBar5);
        profileNumber = v.findViewById(R.id.textView2);
        voteFExisPannel = findViewById(R.id.vote_for_exis);
        viewResults = findViewById(R.id.view_results);
        help_s = findViewById(R.id.help_sec);
        raiseAT = findViewById(R.id.Raise_a_ticket);
        pFV.setVisibility(View.INVISIBLE);

    }

    boolean checkPanelExis(String s, int iu) {

        try {

            databaseReferencePP.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        panelSender(iu, s);

                    } else {
                        pFV.setVisibility(View.INVISIBLE);
                        Toast.makeText(DashBoard.this, "Not Found", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception er) {
            pFV.setVisibility(View.INVISIBLE);
            er.printStackTrace();
        }


        return false;


    }

    void panelSender(int i, String displayName) {


        pFV.setVisibility(View.VISIBLE);
        try {

            if (i == 1) {


                databaseReferencePP.child(displayName).child("Panels").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        dList.clear();
                        if (snapshot.exists()) {



                            for (DataSnapshot i : snapshot.getChildren()) {
                                String gg = i.getKey().toString().toUpperCase();
                                dList.add(gg);
                            }

                            Intent di = new Intent(DashBoard.this, PanelInformation.class);
                            di.putStringArrayListExtra("dList", dList);
                            di.putExtra("DN", displayName);
                            pFV.setVisibility(View.INVISIBLE);
                            startActivity(di);


                        } else {


                            Intent di = new Intent(DashBoard.this, PanelInformation.class);
                            di.putStringArrayListExtra("dList", dList);
                            di.putExtra("DN", displayName);
                            pFV.setVisibility(View.INVISIBLE);
                            startActivity(di);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            } else if (i == 2) {


                databaseReferencePP.child(displayName).child("Panels").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        if (snapshot.exists()) {


                            Intent di = new Intent(DashBoard.this, PanelInfoForVoting.class);
                            di.putExtra("DN", displayName);
                            pFV.setVisibility(View.INVISIBLE);
                            startActivity(di);


                        } else {
                            Toast.makeText(DashBoard.this, "No Panel is Available for voting", Toast.LENGTH_SHORT).show();
                            pFV.setVisibility(View.INVISIBLE);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {

                Intent ijk = new Intent(DashBoard.this, ResultsActivity.class);
                ijk.putExtra("PN", displayName);
                pFV.setVisibility(View.INVISIBLE);
                startActivity(ijk);

            }


        } catch (Exception ew) {
            pFV.setVisibility(View.INVISIBLE);
            ew.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}