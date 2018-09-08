package com.example.maask.deliveryagentchatbot;

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
import com.example.maask.deliveryagentchatbot.PojoClass.ManageJob;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request);

        ManageJob manageJob = (ManageJob) getIntent().getSerializableExtra("manageJob");

        Toast.makeText(this, manageJob.getStatus(), Toast.LENGTH_SHORT).show();

        parentKey = manageJob.getJobId();

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Job");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coverLetterET = findViewById(R.id.cover_letter_et);
        applyJobBT = findViewById(R.id.apply_job_bt);
        jobNotificationLL = findViewById(R.id.job_notification_ll);
        jobNotificationTV = findViewById(R.id.job_notification_tv);


        if (!manageJob.getStatus().equals("Apply ")){
            jobNotificationTV.setText("You have already applied this job, if you want to decline this job write a massage and decline it");
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = auth.getCurrentUser();

        databaseReference.child("clientOfferedJob").child(parentKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

                        if (coverLetter.isEmpty()){
                            Toast.makeText(JobRequestActivity.this, "Please, Write a cover letter ...", Toast.LENGTH_SHORT).show();
                        }else {

                            Conversation conversation = new Conversation("deliveryMan",coverLetter);

                            AppliedDeliveryManInfo appliedDeliveryManInfo = new AppliedDeliveryManInfo(currentUser.getUid());

                            databaseReference.child("clientOfferedJob").child(parentKey).child("AppliedDeliveryManInfo").push().setValue(appliedDeliveryManInfo);

                            databaseReference.child("JobRequest").child(currentUser.getUid()).child(clientId).child("requestType").setValue("snt");
                            databaseReference.child("JobRequest").child(clientId).child(currentUser.getUid()).child("requestType").setValue("rec");

                            databaseReference.child("ClientDeliveryManConversation").child(currentUser.getUid()).child(clientId).push().setValue(conversation);
                            databaseReference.child("ClientDeliveryManConversation").child(clientId).child(currentUser.getUid()).push().setValue(conversation);

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
