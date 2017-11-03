package com.example.test.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.R.attr.bitmap;


public class SesttingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;



    //vareabile for layout


    private CircleImageView mDisplayImage;
    private TextView mUsername;
    private TextView mStatus;
    private Button mStatusbtn;
    private Button mChangeimg;
    private  static  final int Galary_PICK=1;

 // Strorage Firbase

    private StorageReference mImageStroge;



    //progreass barFor image


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesttings);

        mDisplayImage=(CircleImageView)findViewById(R.id.Settings_img) ;
        mUsername=(TextView) findViewById(R.id.seting_display_name);
        mStatus=(TextView) findViewById(R.id.setings_status);
        mStatusbtn= (Button) findViewById(R.id.setting_status_btn);
        mChangeimg= (Button) findViewById(R.id.setting_img_btn);
        mImageStroge= FirebaseStorage.getInstance().getReference();

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
                if(!image.equals("defult")){
                    Picasso.with(SesttingsActivity.this).load(image).placeholder(R.drawable.user1600).into(mDisplayImage);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusvalue= mStatus.getText().toString();

                Intent statusintent  = new Intent(SesttingsActivity.this , StatusActivity.class);
                statusintent.putExtra("statusvalue",statusvalue);
                startActivity(statusintent);

            }
        });


        mChangeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallaryIntent,"select Image"),Galary_PICK);

//Image With option camera



            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

   if(requestCode==Galary_PICK && resultCode==RESULT_OK){

       Uri imageUri= data.getData();
       CropImage.activity(imageUri)
               .setAspectRatio(1,1)
               .start(this);


//       CropImage.activity(imageUri)
//               .setGuidelines(CropImageView.Guidelines.ON)
//               .setAspectRatio(1,1)
//               .start(SesttingsActivity.this);


       //Toast.makeText(SesttingsActivity.this,imageUri,Toast.LENGTH_LONG).show();

   }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialog=new ProgressDialog(SesttingsActivity.this);
                mProgressDialog.setTitle("Uploading Image..");
                mProgressDialog.setMessage("Please Wait while we upload the image ");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                Uri resultUri = result.getUri();
                String current_uid= mCurrentUser.getUid();
                File thumb_filePath= new File(resultUri.getPath());


                Bitmap thmb_bitmap = null;
                try {
                    thmb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                           .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    thmb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thum_bayt = baos.toByteArray();



                StorageReference filepath= mImageStroge.child("profile_images").child(current_uid+".jpg");
                final StorageReference thumb_filepath= mImageStroge.child("profile_images").child("thumbs").child(current_uid+".jpg");




                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                           @SuppressWarnings("VisibleForTests") final String download_url= task.getResult().getDownloadUrl().toString();


                             //upload task

                            UploadTask uploadTask = thumb_filepath.putBytes(thum_bayt);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    @SuppressWarnings("VisibleForTests") String thum_download_url=thumb_task.getResult().getDownloadUrl().toString();

                                     if(thumb_task.isSuccessful()){

                                         Map updatahashMap= new HashMap();
                                         updatahashMap.put("image",download_url);
                                         updatahashMap.put("thumb_image",thum_download_url);

                                         mUserDatabase.updateChildren(updatahashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful()){

                                                     mProgressDialog.dismiss();
                                                     Toast.makeText(SesttingsActivity.this,"Upload Success",Toast.LENGTH_LONG).show();
                                                 }
                                             }
                                         });

                                     }else {
                                         Toast.makeText(SesttingsActivity.this,"Error in uploading thmnnail",Toast.LENGTH_LONG).show();
                                         mProgressDialog.dismiss();
                                     }
                                }
                            });


                        }else{
                            Toast.makeText(SesttingsActivity.this,"Error in uploading",Toast.LENGTH_LONG).show();
                         mProgressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}
