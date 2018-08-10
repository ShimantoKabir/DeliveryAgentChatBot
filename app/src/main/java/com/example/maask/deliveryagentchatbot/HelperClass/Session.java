package com.example.maask.deliveryagentchatbot.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Random;

/**
 * Created by Maask on 8/10/2018.
 */

public class Session {

    private Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = auth.getCurrentUser();

    public Session(Context context) {
        this.context = context;
    }

    private static final String PREFERENCES_KEY  = "freede_preferences";
    private static final String SESSION_ID       = "session_id";

    SharedPreferences sharedPreferences;

    public void setNewSessionId(String sessionId) {

        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_ID,sessionId);
        editor.commit();

    }

    public void resetSessionId(String sessionId) {

        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_ID,sessionId);
        editor.commit();

    }

    public String getSessionId() {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_ID,"");
    }

    public String generateRandomSessionId() {

        Random random = new Random();
        int randomNumber = -1*random.nextInt();
        return String.valueOf(randomNumber)+"|"+currentUser.getUid();

    }

}
