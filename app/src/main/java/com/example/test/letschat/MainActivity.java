package com.example.test.letschat;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mTolbar;
    private ViewPager mViewPagger;
    private  SectionsPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mTolbar= (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mTolbar);
        getSupportActionBar().setTitle("Lets Chat");
        mTolbar.setTitleTextColor(Color.WHITE);




        //Taps

        mViewPagger=(ViewPager) findViewById(R.id.main_tap_pagger);
        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPagger.setAdapter(mSectionPagerAdapter);
        mTabLayout= (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPagger);




    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
   if(currentUser==null){
       //to return user after login to start page
      sendToStart();
   }
    }

    private void sendToStart() {
        Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
  getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_logout_btn)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }


        if(item.getItemId()==R.id.main_settings_btn){

            Intent Settingintent = new Intent(MainActivity.this,SesttingsActivity.class);
            startActivity(Settingintent);



        }


        if(item.getItemId()==R.id.main_all_btn){

            Intent Settingintent = new Intent(MainActivity.this,UsersAcitivity.class);
            startActivity(Settingintent);


        }
        return true;
    }
}
