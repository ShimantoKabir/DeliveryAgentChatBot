package com.example.maask.deliveryagentchatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.maask.deliveryagentchatbot.Adapter.ConversationAdapter;
import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.RequestClass.Request;
import com.example.maask.deliveryagentchatbot.ResponseClass.EntityResponse;
import com.example.maask.deliveryagentchatbot.ResponseClass.Response;
import com.example.maask.deliveryagentchatbot.Service.ConversationService;
import com.example.maask.deliveryagentchatbot.Service.EntityService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

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

    private HorizontalScrollView quickReplayHSV;
    private LinearLayout quickReplayLL;
    private EntityService entityService;

    public static final String BASE_URL = "https://api.dialogflow.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendIV = findViewById(R.id.send_iv);
        userQueryET = findViewById(R.id.user_query_et);
        botResponseLoader = findViewById(R.id.bot_response_loader);
        conversationRV = findViewById(R.id.conversation_rv);
        quickReplayHSV = findViewById(R.id.quick_replay_hsv);
        quickReplayLL = findViewById(R.id.quick_replay_ll);

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
                .baseUrl(BASE_URL)
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

                if (botResponse.equals("productAttribute")){
                    botResponse = "Let me know your product Attribute";
                    showBotResponse(botResponse);
                    createQuickReplayButton("306d2688-705b-494f-837d-5e3c72c34960");
                }else if (botResponse.equals("productType")){
                    botResponse = "Let me know your product Type";
                    showBotResponse(botResponse);
                    createQuickReplayButton("bed3e76c-029d-45c8-a45f-15248d6a83cb");
                }else {

                    showBotResponse(botResponse);

                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("onFailure: ",t.getMessage());
            }
        });

    }

    private void createQuickReplayButton(String entityId) {

        quickReplayHSV.setVisibility(View.VISIBLE);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);

        String url = String.format("v1/entities/%s?v=20150910/",entityId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        entityService = retrofit.create(EntityService.class);

        Call<EntityResponse> entityResponseCall = entityService.getResponse(url);

        entityResponseCall.enqueue(new Callback<EntityResponse>() {
            @Override
            public void onResponse(Call<EntityResponse> call, retrofit2.Response<EntityResponse> response) {
                if (response.isSuccessful()){

                    Log.e("id:",response.body().getId());
                    List<EntityResponse.Entry> entries = response.body().getEntries();

                    quickReplayLL.removeAllViews();

                    for (int i = 0; i < entries.size(); i++) {

                        Button quickReplayBtn = new Button(MainActivity.this);
                        quickReplayBtn.setLayoutParams(lp);
                        quickReplayBtn.setTextColor(getResources().getColor(R.color.colorPrimary));

                        quickReplayBtn.setText(entries.get(i).getValue());
                        quickReplayBtn.setId(i);

                        quickReplayLL.addView(quickReplayBtn);

                        quickReplayBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                quickReplayHSV.setVisibility(View.GONE);
                                Button clickedButton = (Button) view;
                                String replay = clickedButton.getText().toString();
                                getBotResponse(replay);

                            }

                        });

                    }

                }
            }

            @Override
            public void onFailure(Call<EntityResponse> call, Throwable t) {
                Log.e("onFailure: ",t.getMessage());
            }
        });

    }

    private void showBotResponse(String botResponse) {

        Conversation conversation = new Conversation(false,botResponse);
        conversationList.add(conversation);
        conversationAdapter = new ConversationAdapter(conversationList,MainActivity.this);
        conversationAdapter.instantDataChang(conversationList);
        conversationRV.setAdapter(conversationAdapter);
        conversationRV.scrollToPosition(conversationAdapter.getItemCount() - 1);

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
