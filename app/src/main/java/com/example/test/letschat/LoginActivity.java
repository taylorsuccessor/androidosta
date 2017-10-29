package com.example.test.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout log_email;
    private TextInputLayout log_password;
    private Button mLogibtn;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private ProgressDialog mloginProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inital firbase
        mAuth = FirebaseAuth.getInstance();
//toolbar
        mToolbar = (Toolbar) findViewById(R.id.login_tolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//progess dialog
        mloginProgess = new ProgressDialog(this);

//get data from view
        log_email = (TextInputLayout) findViewById(R.id.input_login_email);
        log_password= (TextInputLayout) findViewById(R.id.input_login_password);
        mLogibtn=(Button) findViewById(R.id.login_btn);

        mLogibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=log_email.getEditText().getText().toString();
                String password= log_password.getEditText().getText().toString();
//check if input is empty
                if (!TextUtils.isEmpty(email)|| !TextUtils.isEmpty((password))){
                    //progess bar
                    mloginProgess .setTitle("Loggin In ");
                    mloginProgess.setMessage("Please Wait ....");
                    mloginProgess.setCanceledOnTouchOutside(false);
                    mloginProgess.show();

//function for login
             loginUser(email,password);



                }
            }
        });
    }



//login function
    private void loginUser(String email, String password) {
   mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
       @Override
       public void onComplete(@NonNull Task<AuthResult> task) {

           if(task.isSuccessful()){
             mloginProgess.dismiss();
               Intent mainintent = new Intent(LoginActivity.this,MainActivity.class);
               mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(mainintent);
               finish();
           }else {
               mloginProgess.hide();
               Toast.makeText(LoginActivity.this,"Cannot Sign in Please Check the Form And Try Again",Toast.LENGTH_SHORT).show();

           }

       }
   });
    }
}
