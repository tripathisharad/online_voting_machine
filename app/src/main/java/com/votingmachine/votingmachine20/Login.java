package com.votingmachine.votingmachine20;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Fragment {

    private EditText email, password;
    private Button login;
    private TextView forgot_pass;
    private FirebaseAuth mAuthL;
    private ProgressBar progressBarL;
    private boolean passVisible;
    private LoginSucessClickListener loginSucessClickListener;


    public Login(LoginSucessClickListener loginSucessClickListener) {
        this.loginSucessClickListener = loginSucessClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_layout, container, false);
        mAuthL = FirebaseAuth.getInstance();
        email = v.findViewById(R.id.editTextTextEmailAddress2);
        password = v.findViewById(R.id.editTextNumberPassword2);
        login = v.findViewById(R.id.button2);
        forgot_pass = v.findViewById(R.id.forgot_password);
        progressBarL = v.findViewById(R.id.progressBar2);
        progressBarL.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    login.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                    login.setTextColor(Color.parseColor("#FF000000"));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    login.setTextColor(Color.parseColor("#FFFFFFFF"));
                    login.setBackground(getResources().getDrawable(R.drawable.register_round_button));

                }

                return false;
            }
        });



        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= password.getRight() - password.getCompoundDrawables()[right].getBounds().width()){

                        int selection = password.getSelectionEnd();
                        if(passVisible){
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_visibility_off_24,0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        }else {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24,0,R.drawable.ic_baseline_remove_red_eye_24,0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }

                    }
                }






                return false;
            }
        });


        forgot_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    forgot_pass.setTextColor(Color.parseColor("#FF0266"));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    forgot_pass.setTextColor(Color.parseColor("#424242"));

                }

                return false;
            }
        });



        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v = LayoutInflater.from(getContext()).inflate(R.layout.forgot_pass_dialog, null);
                builder.setView(v)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                EditText emailL = v.findViewById(R.id.forgor_pass_dialog_edit);
                                String emailH = emailL.getText().toString().trim();

                                if(!TextUtils.isEmpty(emailH)){

                                    progressBarL.setVisibility(View.VISIBLE);
                                    mAuthL.sendPasswordResetEmail(emailH).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                dialogInterface.cancel();
                                                progressBarL.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(), "Password reset mail sent successfully", Toast.LENGTH_SHORT).show();
                                            }else{
                                                dialogInterface.cancel();
                                                progressBarL.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogInterface.cancel();
                                            progressBarL.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }else {
                                    Toast.makeText(getContext(), "Please enter valid E-mail !", Toast.LENGTH_SHORT).show();
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eMail = email.getText().toString().trim();
                String pAssword = password.getText().toString().trim();

                if (TextUtils.isEmpty(eMail)) {
                    email.setError("Email is Required");
                } else if (TextUtils.isEmpty(pAssword)) {
                    password.setError("Password is Required");
                } else {
                    progressBarL.setVisibility(View.VISIBLE);
                    mAuthL.signInWithEmailAndPassword(eMail, pAssword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                startActivity(new Intent(getContext(), DashBoard.class));
                                progressBarL.setVisibility(View.INVISIBLE);
                                loginSucessClickListener.onLoginClick();

                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBarL.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }


}
