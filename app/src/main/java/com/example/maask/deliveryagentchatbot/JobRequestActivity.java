package com.example.maask.deliveryagentchatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.maask.deliveryagentchatbot.PojoClass.AppliedDeliveryManInfo;
import com.example.maask.deliveryagentchatbot.PojoClass.ClientOfferedJob;
import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.HelperClass.ManageJob;
import com.example.maask.deliveryagentchatbot.PojoClass.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobRequestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText coverLetterET;
    private Button applyJobBT;
    private LinearLayout jobNotificationLL;
    private TextView jobNotificationTV;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private String clientId;
    private String parentKey;

    SharedPreferences sharedPreferences;
    private static final String USER_TYPE = "client_or_delivery_man";
    private static final String PREFERENCES_KEY        = "freede_preferences";

    private int userType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        userType = sharedPreferences.getInt(USER_TYPE,0);

        final ManageJob manageJob = (ManageJob) getIntent().getSerializableExtra("manageJob");

        parentKey = manageJob.getJobId();

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Cover letter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coverLetterET = findViewById(R.id.cover_letter_et);
        applyJobBT = findViewById(R.id.apply_job_bt);
        jobNotificationLL = findViewById(R.id.job_notification_ll);
        jobNotificationTV = findViewById(R.id.job_notification_tv);

        if (manageJob.getStatus() != 1){
            jobNotificationTV.setText("You have already applied this job, if you want to decline this job write a massage and decline it");
            applyJobBT.setText("Decline");
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = auth.getCurrentUser();

        databaseReference.child("ClientOfferedJob").child(parentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("onDataChange: ",dataSnapshot.toString());
                ClientOfferedJob clientOfferedJob = dataSnapshot.getValue(ClientOfferedJob.class);
                clientId = clientOfferedJob.getClientId();

                applyJobBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String coverLetter = coverLetterET.getText().toString();
                        coverLetterET.setText("");

                        Log.e("manageJob: ", String.valueOf(manageJob.getStatus()));

                        if (coverLetter.isEmpty()){
                            Toast.makeText(JobRequestActivity.this, "Please, Write a cover letter ...", Toast.LENGTH_SHORT).show();
                        }else {

                            Conversation conversation = new Conversation("deliveryMan",coverLetter);

                            databaseReference.child("ClientDeliveryManConversation").child(currentUser.getUid()).child(clientId).push().setValue(conversation);
                            databaseReference.child("ClientDeliveryManConversation").child(clientId).child(currentUser.getUid()).push().setValue(conversation);

                            if (manageJob.getStatus() == 1) {

                                AppliedDeliveryManInfo appliedDeliveryManId = new AppliedDeliveryManInfo(currentUser.getUid());
                                databaseReference.child("ClientOfferedJob").child(parentKey).child("AppliedDeliveryManInfo").push().setValue(appliedDeliveryManId);

                                databaseReference.child("JobRequest").child(currentUser.getUid()).child(clientId).child("requestType").setValue("snt");
                                databaseReference.child("JobRequest").child(clientId).child(currentUser.getUid()).child("requestType").setValue("rec")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(JobRequestActivity.this, "Request sent successful ... ", Toast.LENGTH_SHORT).show();

                                        // -------------------- get client device token -------------------- //
                                        databaseReference.child("UserInfo").child(clientId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                String deviceToken = dataSnapshot.child("deviceToken").getValue().toString();

                                                Log.e("getDeviceToken 1: ",deviceToken);

                                                Notification notification = new Notification(clientId,"jobRequest",deviceToken,"SomeOne");

                                                databaseReference.child("Notification").child(clientId).push().setValue(notification)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            Log.e("isSuccessful 1: ","isSuccessful");

                                                            Toast.makeText(JobRequestActivity.this, "SUCCESS : Data inserted successfully ....", Toast.LENGTH_SHORT).show();
                                                            Intent gotJobPortal = new Intent(JobRequestActivity.this,JobPortalActivity.class);
                                                            gotJobPortal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(gotJobPortal);

                                                        }else {
                                                            Toast.makeText(JobRequestActivity.this, "ERROR : Something wrong .... ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("getDeviceTokenErr: ",databaseError.getMessage());
                                            }
                                        });

                                    }
                                });

                            }else {

                                databaseReference.child("ClientOfferedJob").child(parentKey).child("AppliedDeliveryManInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            AppliedDeliveryManInfo appliedDeliveryManInfo = snapshot.getValue(AppliedDeliveryManInfo.class);
                                            if (appliedDeliveryManInfo.getDeliveryManId().equals(currentUser.getUid())){
                                                Log.e("jobRequestClickEvent: ",snapshot.getKey());
                                                databaseReference.child("ClientOfferedJob").child(parentKey).child("AppliedDeliveryManInfo").child(snapshot.getKey()).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("onCancelled: ",databaseError.getMessage());
                                    }
                                });

                                databaseReference.child("JobRequest").child(currentUser.getUid()).child(clientId).removeValue();
                                databaseReference.child("JobRequest").child(clientId).child(currentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(JobRequestActivity.this, "Request remove successful ... ", Toast.LENGTH_SHORT).show();
                                        Intent gotJobPortal = new Intent(JobRequestActivity.this,JobPortalActivity.class);
                                        gotJobPortal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(gotJobPortal);
                                    }
                                });

                            }

                        }

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled: ",databaseError.getMessage());
            }
        });


    }

}