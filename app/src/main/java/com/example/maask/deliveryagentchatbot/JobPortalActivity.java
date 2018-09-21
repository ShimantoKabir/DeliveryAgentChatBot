package com.example.maask.deliveryagentchatbot;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.maask.deliveryagentchatbot.Adapter.ClientOfferedJobAdapter;
import com.example.maask.deliveryagentchatbot.PojoClass.ClientOfferedJob;
import com.example.maask.deliveryagentchatbot.HelperClass.ManageJob;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class JobPortalActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private ArrayList<ClientOfferedJob> clientOfferedJobs = new ArrayList<>();
    private ClientOfferedJobAdapter clientOfferedJobAdapter;

    private RecyclerView jobPortalRV;
    private SwipeRefreshLayout refreshJobPortalSRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_portal);

        toolbar = findViewById(R.id.custom_toolbar);
        jobPortalRV = findViewById(R.id.job_portal_rv);
        refreshJobPortalSRL = findViewById(R.id.refresh_job_portal_srl);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Job Portal");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        jobPortalRV.setLayoutManager(lm);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = auth.getCurrentUser();

        databaseReference.child("ClientOfferedJob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientOfferedJobs.clear();

                for (DataSnapshot cojSnapShot : dataSnapshot.getChildren()) {
                    ClientOfferedJob clientOfferedJob = cojSnapShot.getValue(ClientOfferedJob.class);
                    clientOfferedJobs.add(clientOfferedJob);
                    Log.e("clientOfferedJob: ",cojSnapShot.toString());
                }

                Collections.reverse(clientOfferedJobs);
                clientOfferedJobAdapter = new ClientOfferedJobAdapter(clientOfferedJobs,JobPortalActivity.this);

                jobPortalRV.setAdapter(clientOfferedJobAdapter);

                clientOfferedJobAdapter.setOnLocationClickListener(new ClientOfferedJobAdapter.OnLocationIconClickListener() {
                    @Override
                    public void onLocationClick(String location) {
                        Toast.makeText(JobPortalActivity.this, location, Toast.LENGTH_SHORT).show();
                    }
                });

                clientOfferedJobAdapter.setOnApplyClickListener(new ClientOfferedJobAdapter.OnApplyClickListener() {
                    @Override
                    public void onApplyClick(ManageJob manageJob) {
                        sendJobRequestToClient(manageJob);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled: ",databaseError.getMessage());
            }
        });

        refreshJobPortalSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent refresh = new Intent(JobPortalActivity.this,JobPortalActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(refresh);
            }
        });

    }

    private void sendJobRequestToClient(ManageJob manageJob) {


        Intent intent = new Intent(JobPortalActivity.this,JobRequestActivity.class);
        intent.putExtra("manageJob",manageJob);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.client_toolbar_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                Intent intent = new Intent(JobPortalActivity.this,MainActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);

    }

}
