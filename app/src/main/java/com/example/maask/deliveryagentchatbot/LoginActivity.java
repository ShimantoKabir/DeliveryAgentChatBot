package com.example.maask.deliveryagentchatbot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup clientDeliveryRG;

    private SignInButton googleLoginBT;
    private static final String TAG = "Google login fail - ";
    private FirebaseAuth auth;
    GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

    private ProgressDialog progressDialog;

    private static final String PREFERENCES_KEY  = "freede_preferences";
    private static final String VISIT_LOGIN      = "visit_login";

    SharedPreferences sharedPreferences;

    public int clientOrDeliveryMan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        googleLoginBT = findViewById(R.id.google_login);
        clientDeliveryRG = findViewById(R.id.client_delivery_rg);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "ERROR : Sorry sir/man some thing wrong ... !", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        clientDeliveryRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton.getText().toString().equals("Client")){
                    clientOrDeliveryMan = 1;
                }else {
                    clientOrDeliveryMan = 2;
                }
                Log.e("onCheckedChanged: ",String.valueOf(clientOrDeliveryMan));
            }
        });

        googleLoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clientOrDeliveryMan == 0){
                    Toast.makeText(LoginActivity.this, "WARNING : Please select are you delivery man or client !", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setMessage("Sign in with google ... ");
                    progressDialog.show();
                    googleSignIn();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }

    }

    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(VISIT_LOGIN, "Y");
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("clientOrDeliveryMan",clientOrDeliveryMan);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {

                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "ERROR : Sorry Sir/Mam some thing wrong .. !", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Click", Toast.LENGTH_SHORT).show();
    }
}
