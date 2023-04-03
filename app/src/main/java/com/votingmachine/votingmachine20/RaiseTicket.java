package com.votingmachine.votingmachine20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RaiseTicket extends AppCompatActivity {

    private EditText fName,phoneNumber,descriptionD,issueCount;
    private Button submitB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_ticket);

        fName = findViewById(R.id.editTextTextPersonName3);
        phoneNumber = findViewById(R.id.editTextTextPersonName34);
        descriptionD = findViewById(R.id.editTextTextPersonName4);
        issueCount = findViewById(R.id.editTextTextPersonName341);
        submitB = findViewById(R.id.editTextTextPersonName3412);




        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fNamee = fName.getText().toString();
                String phN = phoneNumber.getText().toString().trim();
                String descri = descriptionD.getText().toString();
                String iN = issueCount.getText().toString().trim();

                if(TextUtils.isEmpty(fNamee)){
                    fName.setError("Name is required");
                    return;
                }else if(TextUtils.isEmpty(phN)){
                    phoneNumber.setError("Phone is required");
                    return;
                }else if(TextUtils.isEmpty(descri)){
                    descriptionD.setError("Required");
                    return;
                }else if(descri.length() < 10){
                    descriptionD.setError("Should be greater than 10 character");
                    return;
                }else if(TextUtils.isEmpty(iN)){
                    issueCount.setError("Required");
                }else{

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"onlinevotingm@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Ticket Regarding Voting Machine");
                    intent.putExtra(Intent.EXTRA_TEXT, descri+"\nName - " + fNamee + "\n Phone No - " + phN + "\nIssue count - " + iN);


                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    finish();


                }



            }
        });





        submitB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    submitB.setBackground(getResources().getDrawable(R.drawable.touch_black_back));


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    submitB.setBackground(getResources().getDrawable(R.drawable.dashboard_linearlayout_roundshape));
                }




                return false;
            }
        });


    }
}