package com.example.maask.deliveryagentchatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.maask.deliveryagentchatbot.Adapter.ConversationAdapter;
import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.RequestClass.Request;
import com.example.maask.deliveryagentchatbot.ResponseClass.Response;
import com.example.maask.deliveryagentchatbot.Service.ConversationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY  = "freede_preferences";
    private static final String VISIT_LOGIN      = "visit_login";

    SharedPreferences sharedPreferences;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    android.support.v7.widget.Toolbar toolbar;

    private ImageView sendIV;
    private EditText userQueryET;

    private ConversationService conversationService;
    private ProgressBar botResponseLoader;

    private RecyclerView conversationRV;
    private ArrayList<Conversation> conversationList;
    private ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendIV = findViewById(R.id.send_iv);
        userQueryET = findViewById(R.id.user_query_et);
        botResponseLoader = findViewById(R.id.bot_response_loader);
        conversationRV = findViewById(R.id.conversation_rv);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        conversationRV.setLayoutManager(lm);

        conversationList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.dialogflow.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        conversationService = retrofit.create(ConversationService.class);

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

            sendIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userQuery = userQueryET.getText().toString();
                    if (userQuery.isEmpty()){
                        Toast.makeText(MainActivity.this, "INFO : Do you want to say something !", Toast.LENGTH_SHORT).show();
                    }else {

                        sendIV.setVisibility(View.GONE);
                        botResponseLoader.setVisibility(View.VISIBLE);
                        Conversation conversation = new Conversation(true,userQuery);
                        conversationList.add(conversation);
                        getBotResponse(userQuery);

                    }

                }
            });

        }
    }

    private void getBotResponse(String userQuery) {

        Request request = new Request();
        request.setLang("en");
        request.setQuery(userQuery);
        request.setSessionId("12345");
        request.setTimezone("America/New_York");

        Call<Response> responseCall = conversationService.getResponse(request);

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                userQueryET.setText("");
                sendIV.setVisibility(View.VISIBLE);
                botResponseLoader.setVisibility(View.GONE);
                String botResponse = response.body().getResult().getFulfillment().getSpeech();

                Conversation conversation = new Conversation(false,botResponse);
                conversationList.add(conversation);
                conversationAdapter = new ConversationAdapter(conversationList,MainActivity.this);
                conversationAdapter.instantDataChang(conversationList);
                conversationRV.setAdapter(conversationAdapter);

                conversationRV.scrollToPosition(conversationAdapter.getItemCount() - 1);

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("onFailure: ",t.getMessage());
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

            case R.id.profile:
                Toast.makeText(this, "Profile clicked !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:

                Toast.makeText(this, "Logout clicked !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.changed_pass:

                Toast.makeText(this, "Changed Password clicked !", Toast.LENGTH_SHORT).show();

                break;

            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
