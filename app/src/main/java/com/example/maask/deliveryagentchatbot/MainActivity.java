package com.example.maask.deliveryagentchatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY  = "freede_preferences";
    private static final String VISIT_LOGIN      = "visit_login";

    SharedPreferences sharedPreferences;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(VISIT_LOGIN, "").equals("Y")) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            try {

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                String clientOrDeliverMan = getIntent().getExtras().getString("clientOrDeliveryMan");

                Log.e("onCreate: ",clientOrDeliverMan);

                if (clientOrDeliverMan.equals("2131230780")){
                    // delivery man block ....
                    databaseReference.child("UserInfo").child("deliveryManInfo").child(currentUser.getUid()).child("id").setValue(currentUser.getUid());
                }else {
                    // client block ...
                    databaseReference.child("UserInfo").child("clientInfo").child(currentUser.getUid()).child("id").setValue(currentUser.getUid());
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
