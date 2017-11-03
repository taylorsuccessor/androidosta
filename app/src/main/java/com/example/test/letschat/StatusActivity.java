package com.example.test.letschat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
private Toolbar mToolbar;
//fire base

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mcuruntuse;

    private TextInputLayout mstatus;
    private Button mSaveStatus;
    private ProgressDialog mStatusProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Firebase
        mcuruntuse= FirebaseAuth.getInstance().getCurrentUser();
        String crun_uid=mcuruntuse.getUid();
        mStatusDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(crun_uid);


//Progress Dialog
        mStatusProgess =new ProgressDialog(this);


        mToolbar = (Toolbar) findViewById(R.id.status_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String statusvalue=getIntent().getStringExtra("statusvalue");


        mstatus = (TextInputLayout) findViewById(R.id.status_input);
        mSaveStatus= (Button) findViewById(R.id.status_save_btn);

        mstatus.getEditText().setText(statusvalue);


        mSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Progess Dialog
                mStatusProgess = new ProgressDialog(StatusActivity.this);
                mStatusProgess.setTitle("Save Changes");
                mStatusProgess.setMessage("Please Wait for save Changes");
                mStatusProgess.show();
                String status= mstatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mStatusProgess.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"There was some Error in update Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
