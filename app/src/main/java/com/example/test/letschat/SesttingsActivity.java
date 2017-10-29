package com.example.test.letschat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class SesttingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;



    //vareabile for layout


    private CircleImageView mDisplayImage;
    private TextView mUsername;
    private TextView mStatus;
    private Button mStatusbtn;
    private Button mChangeimg;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesttings);

        mDisplayImage=(CircleImageView)findViewById(R.id.Settings_img) ;
        mUsername=(TextView) findViewById(R.id.seting_display_name);
        mStatus=(TextView) findViewById(R.id.setings_status);
        mStatusbtn= (Button) findViewById(R.id.setting_status_btn);
        mChangeimg= (Button) findViewById(R.id.setting_img_btn);


        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String currunt_uid=mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currunt_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// retreve the data from database
                String name=dataSnapshot.child("name").getValue().toString();
                String image= dataSnapshot.child("image").getValue().toString();
                String status= dataSnapshot.child("status").getValue().toString();
                String thumb_image= dataSnapshot.child("thumb_image").getValue().toString();
                //set value to viwe
                mUsername.setText(name);
                mStatus.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusintent  = new Intent(SesttingsActivity.this , StatusActivity.class);
                startActivity(statusintent);

            }
        });


    }
}
