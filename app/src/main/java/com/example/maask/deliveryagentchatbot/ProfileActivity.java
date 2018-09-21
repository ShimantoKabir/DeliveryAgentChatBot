package com.example.maask.deliveryagentchatbot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImgCIV;
    private Button saveEditBT;
    private EditText userNameET,userPhoneNoET;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser currentUser;

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;

    private ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    private static final String USER_TYPE = "client_or_delivery_man";
    private static final String PREFERENCES_KEY = "freede_preferences";

    private int userType = 0;
    private String userTypeString = "null" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        userType = sharedPreferences.getInt(USER_TYPE,0);

        profileImgCIV = findViewById(R.id.profile_img_civ);
        saveEditBT = findViewById(R.id.save_edit_bt);
        userNameET = findViewById(R.id.user_name_et);
        userPhoneNoET = findViewById(R.id.user_phone_no_et);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (userType==1) {
            userTypeString = "clientPic";
        }else {
            userTypeString = "deliveryPic";
        }

        Log.e("clientOrDeliverMan",userTypeString);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting User Information ... ");
        progressDialog.show();

        if (isNetworkAvailable()){

            storageReference.child("ProfilePicture/"+currentUser.getUid()+".jpg").getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    Picasso.with(ProfileActivity.this)
                            .load(imageURL)
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.profile_img)
                            .into(profileImgCIV, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    databaseReference.child("UserInfo").child(currentUser.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String userName = dataSnapshot.child("userName").getValue().toString();
                                            String userPhoneNumber = dataSnapshot.child("userPhoneNumber").getValue().toString();
                                            userNameET.setText(userName);
                                            userPhoneNoET.setText(userPhoneNumber);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("onCancelled: ",databaseError.getMessage());
                                        }
                                    });

                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(ProfileActivity.this, "SORRY : Can't show profile image ! ", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "INFO : Image and information did not updated yet !", Toast.LENGTH_SHORT).show();
                }
            });




        }

        if (!userTypeString.equals("null")){
            databaseReference.child("UserInfo").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("onDataChange: ", String.valueOf(dataSnapshot.getChildrenCount()));
                    if (dataSnapshot.getChildrenCount() != 2){
                        saveEditBT.setText("EDIT");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("onCancelled: ",databaseError.getMessage());
                }
            });
        }

        profileImgCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        saveEditBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable()){

                    String userName = userNameET.getText().toString();
                    String userPhoneNumber = userPhoneNoET.getText().toString();

                    Log.e("onClick: ", String.valueOf(filePath));

                    if (userName.isEmpty() || userPhoneNumber.isEmpty() || filePath == null){

                        Toast.makeText(ProfileActivity.this, "INFO : Please fill up the required filed ! ", Toast.LENGTH_SHORT).show();

                    }else {

                        updateProfile(currentUser.getUid(),userName,userPhoneNumber,userType);

                    }

                }else {

                    Toast.makeText(ProfileActivity.this, "INFO : Network is not available please try again letter ! ", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void updateProfile(final String uid, final String userName, final String userPhoneNumber, final int userType) {

        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference riversRef = storageReference.child("ProfilePicture/"+uid+".jpg");
            riversRef.putFile(filePath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final String profileImageUrl = taskSnapshot.getDownloadUrl().toString();

                    databaseReference.child("UserInfo").child(uid).child("userName").setValue(userName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                databaseReference.child("UserInfo").child(uid).child("userPhoneNumber").setValue(userPhoneNumber)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            databaseReference.child("UserInfo").child(uid).child("imgUrl").setValue(profileImageUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){

                                                        progressDialog.dismiss();
                                                        Toast.makeText(ProfileActivity.this, "SUCCESS : User profile updated successfully .... !", Toast.LENGTH_SHORT).show();
                                                        saveEditBT.setText("EDIT");

                                                    }else {

                                                        progressDialog.dismiss();
                                                        Toast.makeText(ProfileActivity.this, "SUCCESS : User profile not updated successfully .... !", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                            });

                                        }else {

                                            progressDialog.dismiss();
                                            Toast.makeText(ProfileActivity.this, "SUCCESS : User profile not updated successfully .... !", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            }else {

                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "SUCCESS : User profile not updated successfully .... !", Toast.LENGTH_SHORT).show();

                            }

                        }

                    });

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.setMessage("Updating user profile ...");

                }
            });

        }else {
            Toast.makeText(this, "WARNING : Sir/Mam image has not been changed !", Toast.LENGTH_SHORT).show();
        }

    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImgCIV.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            return false;
        } else {
            return true;
        }

    }

}
