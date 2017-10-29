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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout name;
    private  TextInputLayout email;
    private TextInputLayout password;
    private Button MCreateBtn;
    private Toolbar mToolbar;
    private ProgressDialog  mRegProgess;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set Toolbar
        mToolbar = (Toolbar) findViewById(R.id.register_tolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Creat Account");
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        //set progress bar

        mRegProgess = new ProgressDialog(this);







        mAuth=FirebaseAuth.getInstance();
        name = (TextInputLayout)findViewById(R.id.reg_display_name);
        email = (TextInputLayout)findViewById(R.id.email);
        password = (TextInputLayout)findViewById(R.id.reg_password);
        MCreateBtn=(Button) findViewById(R.id.reg_create_btn);

        MCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des_name= name.getEditText().getText().toString();
                String mEmail= email.getEditText().getText().toString();
                String mPassword= password.getEditText().getText().toString();

           if(!TextUtils.isEmpty(des_name)||!TextUtils.isEmpty(mEmail)||!TextUtils.isEmpty(mPassword) ){
               mRegProgess.setTitle("Resgestration");
               mRegProgess.setMessage("Loading For Regestration...");
               mRegProgess.setCanceledOnTouchOutside(false);
               mRegProgess.show();
               regeste_user(des_name,mEmail,mPassword);
           }
            }
        });

    }

    private void regeste_user(final String des_name, String mEmail, String mPassword) {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> userMap= new HashMap<>();
                            userMap.put("name",des_name);
                            userMap.put("status","Hi There I'm using Let's Chat App");
                            userMap.put("image","defult");
                            userMap.put("thumb_image","defult");
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){


                            mRegProgess.dismiss();
                            Intent mainIntent = new Intent(RegisterActivity.this ,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                                    }
                                }
                            });




                        }else{

                            mRegProgess.hide();
                              Toast.makeText(RegisterActivity.this,"Cannot Sign in Please Check the Form And Try Again",Toast.LENGTH_SHORT).show();



                        }



                    }
                });
    }
}
