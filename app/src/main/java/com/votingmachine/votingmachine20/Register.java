package com.votingmachine.votingmachine20;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class Register extends Fragment {

    private EditText inputOTP,email,password,phone;
    private Button signup,getOTP;
    private FirebaseAuth mAuth;
    private ProgressBar regisProgressBar;
    public static String SID = "1001";
    private boolean passVisible;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reigter_layout,container,false);
        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextNumberPassword);
        signup = view.findViewById(R.id.button);
        inputOTP = view.findViewById(R.id.email_otp_section);
        getOTP = view.findViewById(R.id.send_otp_section);
        phone = view.findViewById(R.id.editTextTextPhone);
        regisProgressBar = view.findViewById(R.id.progressBar);
        regisProgressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        signup.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    signup.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                    signup.setTextColor(Color.parseColor("#FF000000"));
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    signup.setTextColor(Color.parseColor("#FFFFFFFF"));
                    signup.setBackground(getResources().getDrawable(R.drawable.register_round_button));

                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String emailNew = email.getText().toString().trim();
                String passNew = password.getText().toString().trim();
                String optNew = inputOTP.getText().toString().trim();
                String phoneH = phone.getText().toString().trim();

                if(TextUtils.isEmpty(emailNew)){

                    email.setEnabled(true);
                    email.setError("Email is required");
                    return;

                }else if (TextUtils.isEmpty(passNew)){

                    password.setError("Password is required");
                    return;

                }else if(TextUtils.isEmpty(optNew)){

                    inputOTP.setError("OTP is required");
                    return;

                }else if(passNew.length() < 6){

                    password.setError("Password must be greater than 6 character");
                    return;

                }else if(TextUtils.isEmpty(phoneH) || phoneH.length() < 10) {

                    phone.setEnabled(true);
                    phone.setError("Phone is required !");
                    return;

                }else {
                    regisProgressBar.setVisibility(View.VISIBLE);
                    verifyEmailOTP(optNew,SID,emailNew,passNew,phoneH);

                }
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

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regisProgressBar.setVisibility(View.VISIBLE);
                String emailL = email.getText().toString().trim();
                if(TextUtils.isEmpty(emailL)){
                    regisProgressBar.setVisibility(View.INVISIBLE);
                    email.setError("Email is required !");
                }else{
                    loginByEmailOTP(emailL);
                }



            }
        });

        getOTP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    getOTP.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                    getOTP.setTextColor(Color.parseColor("#FF000000"));
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    getOTP.setTextColor(Color.parseColor("#FFFFFFFF"));
                    getOTP.setBackground(getResources().getDrawable(R.drawable.register_round_button));

                }



                return false;
            }
        });

    }

    public void loginByEmailOTP(String eMail) {

        // Email Authentication Api...........
        MojoAuthSDK.Initialize init = new MojoAuthSDK.Initialize();
        init.setApiKey("4298d09b-57f4-47d2-8643-d1bdb0983ceb");

        MojoAuthApi mojoAuthApi = new MojoAuthApi();
        mojoAuthApi.loginByEmailOTP(eMail, new AsyncHandler<LoginResponse>() {
            @Override
            public void onFailure(ErrorResponse error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        regisProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Failed !! Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onSuccess(LoginResponse data) {
                SID = data.getStateId();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        regisProgressBar.setVisibility(View.INVISIBLE);
                        email.setEnabled(false);
                        phone.setEnabled(false);
                        Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void verifyEmailOTP(String o, String sId,String emailNew,String passNew,String phoneH) {

        // Email Authentication Api...........
        MojoAuthSDK.Initialize init = new MojoAuthSDK.Initialize();
        init.setApiKey("4298d09b-57f4-47d2-8643-d1bdb0983ceb");

        MojoAuthApi mojoAuthApi = new MojoAuthApi();
        mojoAuthApi.verifyEmailOTP(o, sId, new AsyncHandler<UserResponse>() {
            @Override
            public void onFailure(ErrorResponse error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        regisProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), error.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(UserResponse data) {

                try {

                    mAuth.createUserWithEmailAndPassword(emailNew,passNew).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()

                                        .setDisplayName(phoneH)
                                        .build();
                                firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            regisProgressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(getContext(),DashBoard.class));

                                        }else {
                                            regisProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });

                            }else{
                                regisProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                    regisProgressBar.setVisibility(View.INVISIBLE);
                }

            }
        });


    }
}
