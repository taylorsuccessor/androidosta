package com.example.test.letschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
private TextView mDesplayid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         String user_id= getIntent().getStringExtra("user_id");
        mDesplayid = (TextView) findViewById(R.id.profile_desplayname);
        mDesplayid.setText(user_id);
    }
}
