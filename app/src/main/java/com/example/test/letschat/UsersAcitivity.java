package com.example.test.letschat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAcitivity extends AppCompatActivity {


    private Toolbar mToolabr;
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        mToolabr=  (Toolbar) findViewById(R.id.usersappBar);

        setSupportActionBar(mToolabr);
        getSupportActionBar().setTitle("All Users");
        mToolabr.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        mUsersList= (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }//end on crearte


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter <Users,UsersViweHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViweHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViweHolder.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(UsersViweHolder UsersViweHolder, Users users, int position) {

                UsersViweHolder.setName(users.getName());
                UsersViweHolder.setUserStatus(users.getStatus());

                UsersViweHolder.setUserImg(users.getThumb_img(),getApplicationContext());

                final String user_id=  getRef(position).getKey();

                UsersViweHolder.mViwe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(UsersAcitivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }


    public static   class UsersViweHolder extends RecyclerView.ViewHolder{

        View mViwe;
        public UsersViweHolder(View itemView) {
            super(itemView);

            mViwe=itemView;
        }




    public  void  setName(String name){
        TextView useNameView=(TextView) mViwe.findViewById(R.id.user_single_name);
        useNameView.setText(name);
    }

        public void  setUserStatus( String Status){
            TextView useNameView=(TextView) mViwe.findViewById(R.id.user_single_status);
            useNameView.setText(Status);
        }



        public void setUserImg(String thum_img, Context context){
            CircleImageView user_img_viwe = (CircleImageView) mViwe.findViewById(R.id.user_singl_image);
            Picasso.with(context).load(thum_img).into(user_img_viwe);

        }


    }

}
