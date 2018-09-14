package com.example.maask.deliveryagentchatbot;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;
import com.example.maask.deliveryagentchatbot.Adapter.ConversationAdapter;
import com.example.maask.deliveryagentchatbot.HelperClass.ExtraUserQuery;
import com.example.maask.deliveryagentchatbot.HelperClass.Session;
import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.RequestClass.Request;
import com.example.maask.deliveryagentchatbot.ResponseClass.EntityResponse;
import com.example.maask.deliveryagentchatbot.ResponseClass.Response;
import com.example.maask.deliveryagentchatbot.Service.ConversationService;
import com.example.maask.deliveryagentchatbot.Service.EntityService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY        = "freede_preferences";
    private static final String VISIT_LOGIN            = "visit_login";
    private static final String CLIENT_OR_DELIVERY_MAN = "client_or_delivery_man";

    SharedPreferences sharedPreferences;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    Toolbar toolbar;

    private ImageView sendIV;
    private EditText userQueryET;
    private TextView botQueryTV;

    private ConversationService conversationService;
    private ProgressBar botResponseLoader;

    private RecyclerView conversationRV;
    private ArrayList<Conversation> conversationList;
    private ConversationAdapter conversationAdapter;

    private LinearLayout quickReplayViewLL;
    private HorizontalScrollView quickReplayHSV;
    private LinearLayout quickReplayLL;
    private EntityService entityService;

    public static final String BASE_URL = "https://api.dialogflow.com/";
    private ArrayList<String> yesNo;

    Session session;

    ExtraUserQuery extraUserQuery;

    FirebaseUser currentUser;

    ProgressDialog progressDialog;

    private int clientOrDeliverMan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        sendIV = findViewById(R.id.send_iv);
        userQueryET = findViewById(R.id.user_query_et);
        botResponseLoader = findViewById(R.id.bot_response_loader);
        conversationRV = findViewById(R.id.conversation_rv);
        quickReplayHSV = findViewById(R.id.quick_replay_hsv);
        quickReplayLL = findViewById(R.id.quick_replay_ll);
        quickReplayViewLL = findViewById(R.id.quick_replay_view_ll);
        botQueryTV = findViewById(R.id.bot_query_tv);

        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yesNo = new ArrayList<>();
        yesNo.add("Yes");
        yesNo.add("No");

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        conversationRV.setLayoutManager(lm);

        conversationList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        conversationService = retrofit.create(ConversationService.class);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(VISIT_LOGIN, "").equals("Y")) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            progressDialog.setTitle("Getting old Conversation");
            progressDialog.setMessage("Please wait .... ");
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);

                databaseReference.child("UserBotConversation").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        conversationList.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            Log.e("onDataChange: ",snapshot.toString());
                            Conversation conversation = snapshot.getValue(Conversation.class);
                            conversationList.add(conversation);

                        }

                        conversationAdapter = new ConversationAdapter(conversationList,MainActivity.this);
                        conversationAdapter.instantDataChang(conversationList);
                        conversationRV.setAdapter(conversationAdapter);
                        conversationRV.scrollToPosition(conversationAdapter.getItemCount() - 1);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("onCancelled: ",databaseError.getMessage());
                    }
                });

                session = new Session(this);
                session.setNewSessionId(session.generateRandomSessionId());

                extraUserQuery = new ExtraUserQuery(false,"nothing");

                clientOrDeliverMan = sharedPreferences.getInt(CLIENT_OR_DELIVERY_MAN,0);

                String startLat = getIntent().getExtras().getString("startLat");
                String startLon = getIntent().getExtras().getString("startLon");

                String endLat = getIntent().getExtras().getString("endLat");
                String endLon = getIntent().getExtras().getString("endLon");

                String sessionId = getIntent().getExtras().getString("sessionId");

                if (
                        startLat != null &&
                        startLon != null &&
                        endLat != null &&
                        endLon != null &&
                        sessionId != null &&
                        !startLon.isEmpty() &&
                        !startLat.isEmpty() &&
                        !endLat.isEmpty() &&
                        !endLon.isEmpty() &&
                        !sessionId.isEmpty()

                        ) {

                    session.resetSessionId(sessionId);
                    botResponseLoader.setVisibility(View.VISIBLE);
                    sendIV.setVisibility(View.GONE);
                    String startAndEndPosition = startLat+"/"+startLon+"/"+endLat+"/"+endLon;
                    getBotResponse(startAndEndPosition);

                }

                Log.e("onCreate: ", String.valueOf(clientOrDeliverMan));


            }catch (Exception e){
                e.printStackTrace();
            }

            if (clientOrDeliverMan != 0){

                if (clientOrDeliverMan==1){
                    // delivery man block ....
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    databaseReference.child("UserInfo").child("clientInfo").child(currentUser.getUid()).child("id").setValue(currentUser.getUid());
                    databaseReference.child("UserInfo").child("clientInfo").child(currentUser.getUid()).child("deviceToken").setValue(deviceToken);

                }else {
                    // client block ...
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    databaseReference.child("UserInfo").child("deliveryManInfo").child(currentUser.getUid()).child("id").setValue(currentUser.getUid());
                    databaseReference.child("UserInfo").child("deliveryManInfo").child(currentUser.getUid()).child("deviceToken").setValue(deviceToken);
                }

            }

            sendIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userQuery = userQueryET.getText().toString();
                    if (userQuery.isEmpty()){
                        Toast.makeText(MainActivity.this, "INFO : Do you want to say something !", Toast.LENGTH_SHORT).show();
                    }else {

                        if (extraUserQuery.needExtraUserQuery){
                            userQuery = userQuery+" "+extraUserQuery.getExtraUserQuery();
                        }

                        sendIV.setVisibility(View.GONE);
                        botResponseLoader.setVisibility(View.VISIBLE);
                        showUserQuery(userQuery);

                    }

                }
            });

        }
    }

    private void showUserQuery(String userQuery) {

        Conversation conversation = new Conversation("user",userQuery);
        databaseReference.child("UserBotConversation").child(currentUser.getUid()).push().setValue(conversation);

        conversationList.add(conversation);
        getBotResponse(userQuery);

    }

    private void getBotResponse(String userQuery) {

        Request request = new Request();
        request.setLang("en");
        request.setQuery(userQuery);
        request.setSessionId(session.getSessionId());
        request.setTimezone("America/New_York");

        Call<Response> responseCall = conversationService.getResponse(request);

        responseCall.enqueue(new Callback<Response>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                userQueryET.setText("");
                sendIV.setVisibility(View.VISIBLE);
                botResponseLoader.setVisibility(View.GONE);
                String botResponse = response.body().getResult().getFulfillment().getSpeech();

                Log.e("onResponse: ",response.body().getStatus().getErrorType());

                extraUserQuery = new ExtraUserQuery(false,"nothing");

                switch (botResponse) {

                    case "ProductDetailsSure":

                        botResponse = "Do you want to give us your product details?";

                        quickReplayViewLL.setVisibility(View.VISIBLE);
                        botQueryTV.setText(botResponse);

                        showBotResponse(botResponse);
                        createQuickReplayButton(yesNo);

                        break;

                    case "productAttribute":

                        botResponse = "Let me know your product Attribute";

                        quickReplayViewLL.setVisibility(View.VISIBLE);
                        botQueryTV.setText(botResponse);

                        showBotResponse(botResponse);
                        getAgentEntities("306d2688-705b-494f-837d-5e3c72c34960");

                        break;

                    case "productType":

                        botResponse = "Let me know your product Type";

                        quickReplayViewLL.setVisibility(View.VISIBLE);
                        botQueryTV.setText(botResponse);

                        showBotResponse(botResponse);
                        getAgentEntities("bed3e76c-029d-45c8-a45f-15248d6a83cb");

                        break;

                    case "productWeight":

                        botResponse = "Tell me your product weight";
                        extraUserQuery = new ExtraUserQuery(true,"KG");
                        showBotResponse(botResponse);

                        break;

                    case "productDescription":

                        botResponse = "Let me know your product description";
                        showBotResponse(botResponse);

                        break;

                    case "startAndEndLocation":

                        Intent intent = new Intent(MainActivity.this, GoogleMapActivity.class);
                        intent.putExtra("sessionId",session.getSessionId());
                        startActivity(intent);

                        break;

                    case "postTheJob":

                        botResponse = "I have successfully post your job, whenever a delivery man wants to deliver your product he/she will knock you";
                        showBotResponse(botResponse);

                        break;

                    case "showAvailableJobForDeliveryMan":

                        checkDeliveryManOrNot();

                        break;

                    case "showClientOfferedJob":

                        checkClientOrNot();

                        break;

                    case "showProfile":

                        showProfile();

                        break;

                    default:
                        showBotResponse(botResponse);
                        break;

                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("onFailure: ",t.getMessage());
            }
        });

    }

    private void showProfile() {

        showBotResponse("Now i am going to show your profile");
        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);

    }

    private void checkClientOrNot() {

        databaseReference.child("UserInfo").child("clientInfo").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    showBotResponse("Now i am going to show you the job that offered by me");
                    Intent intent = new Intent(MainActivity.this,ClientOfferedJobActivity.class);
                    startActivity(intent);
                }else {
                    showBotResponse("SORRY : Sim/Mam you currently you are not a client !");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled: ",databaseError.getMessage());
            }
        });

    }

    private void checkDeliveryManOrNot() {

        databaseReference.child("UserInfo").child("deliveryManInfo").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    showBotResponse("Now i am going to redirect you to job portal");
                    Intent intent = new Intent(MainActivity.this,JobPortalActivity.class);
                    startActivity(intent);
                }else {
                    showBotResponse("SORRY : Sim/Mam you currently you are not a delivery man !");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled: ",databaseError.getMessage());
            }
        });

    }

    private void showBotResponse(String botResponse) {

        Conversation conversation = new Conversation("bot",botResponse);
        databaseReference.child("UserBotConversation").child(currentUser.getUid()).push().setValue(conversation);

    }

    private void getAgentEntities(String entityId) {

        final ArrayList<String> entities = new ArrayList<>();

        String url = String.format("v1/entities/%s?v=20150910/",entityId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        entityService = retrofit.create(EntityService.class);

        Call<EntityResponse> entityResponseCall = entityService.getResponse(url);

        entityResponseCall.enqueue(new Callback<EntityResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<EntityResponse> call, retrofit2.Response<EntityResponse> response) {
                if (response.isSuccessful()){

                    entities.clear();

                    List<EntityResponse.Entry> entries = response.body().getEntries();

                    for (int i = 0; i < entries.size(); i++) {
                        entities.add(entries.get(i).getValue());
                    }

                    createQuickReplayButton(entities);

                }
            }

            @Override
            public void onFailure(Call<EntityResponse> call, Throwable t) {
                Log.e("onFailure: ",t.getMessage());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createQuickReplayButton(ArrayList<String> entities) {

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,50);
        lp.setMargins(10,0,0,0);

        quickReplayLL.removeAllViews();

        for (int i = 0; i < entities.size(); i++) {

            Button quickReplayBtn = new Button(MainActivity.this);

            quickReplayBtn.setLayoutParams(lp);
            quickReplayBtn.setPadding(10,5,10,5);
            quickReplayBtn.setBackground(getResources().getDrawable(R.drawable.bot_query_bg));
            quickReplayBtn.setTextColor(getResources().getColor(R.color.white));

            quickReplayBtn.setAllCaps(false);
            quickReplayBtn.setText(entities.get(i));
            quickReplayBtn.setId(i);

            quickReplayLL.addView(quickReplayBtn);

            quickReplayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    quickReplayViewLL.setVisibility(View.GONE);
                    Button clickedButton = (Button) view;
                    String replay = clickedButton.getText().toString();
                    showUserQuery(replay);

                }

            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int cod = sharedPreferences.getInt(CLIENT_OR_DELIVERY_MAN,0);

        if (cod == 1){
            getMenuInflater().inflate(R.menu.client_toolbar_menu,menu);
            return true;
        }else {
            getMenuInflater().inflate(R.menu.delivery_man_toolbar_menu,menu);
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.profile:
                Intent goProfileActivity = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(goProfileActivity);
                break;

            case R.id.client_offered_job:
                Intent goClientOfferedJobActivity = new Intent(MainActivity.this,ClientOfferedJobActivity.class);
                startActivity(goClientOfferedJobActivity);
                break;

            case R.id.show_available_job:
                Intent goJobPortalActivity = new Intent(MainActivity.this,JobPortalActivity.class);
                startActivity(goJobPortalActivity);
                break;

            case R.id.logout:
                ((ActivityManager)this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                break;

        }

        return super.onOptionsItemSelected(item);

    }

}
