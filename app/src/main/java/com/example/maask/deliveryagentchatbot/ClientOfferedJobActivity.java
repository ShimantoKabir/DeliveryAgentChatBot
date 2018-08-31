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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class ClientOfferedJobActivity extends AppCompatActivity {

    Toolbar toolbar;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ArrayList<ClientOfferedJob> clientOfferedJobs = new ArrayList<>();

    private RecyclerView clientOfferedJobRV;
    private ClientOfferedJobAdapter clientOfferedJobAdapter;

    private SwipeRefreshLayout refreshCojSRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_offered_job);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Offered Job");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshCojSRL = findViewById(R.id.refresh_coj_srl);

        clientOfferedJobRV = findViewById(R.id.client_offered_job_rv);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        clientOfferedJobRV.setLayoutManager(lm);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("clientOfferedJob").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientOfferedJobs.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ClientOfferedJob clientOfferedJob = snapshot.getValue(ClientOfferedJob.class);
                    if (clientOfferedJob.getClientId().equals(currentUser.getUid())){

                        clientOfferedJobs.add(clientOfferedJob);
                        Log.e("onDataChange: ", String.valueOf(clientOfferedJob.getStartAndEndLatLon()));

                    }
                }

                Collections.reverse(clientOfferedJobs);
                clientOfferedJobAdapter = new ClientOfferedJobAdapter(clientOfferedJobs,ClientOfferedJobActivity.this);
                clientOfferedJobRV.setAdapter(clientOfferedJobAdapter);

                clientOfferedJobAdapter.setOnLocationClickListener(new ClientOfferedJobAdapter.OnLocationIconClickListener() {
                    @Override
                    public void onLocationClick(String location) {
                        Toast.makeText(ClientOfferedJobActivity.this, location, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled: ",databaseError.getMessage());
            }
        });

        refreshCojSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent refresh = new Intent(ClientOfferedJobActivity.this,ClientOfferedJobActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(refresh);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                Intent goHome = new Intent(ClientOfferedJobActivity.this,MainActivity.class);
                goHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goHome);
                break;

        }

        return super.onOptionsItemSelected(item);

    }

}
