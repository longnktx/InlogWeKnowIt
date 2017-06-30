package com.example.along.inloggweknowit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.storage.StorageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.Sharer;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginBtn;
    private Button notifyBtn;
    private TextView fbNameTV;
    private CallbackManager callbackManager;
    private ProfilePictureView profilePV;
    private AccessTokenTracker tokenTracker;
    private int noNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        loginBtn.setReadPermissions("email","public_profile");

        updateProfile();

        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("SUCCESS");
                Toast.makeText(MainActivity.this, "Loged in",Toast.LENGTH_SHORT).show();
                updateProfile();
            }

            @Override
            public void onCancel() {
                System.out.println("Canceled");
                Toast.makeText(MainActivity.this, "Canceled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Error");
                Toast.makeText(MainActivity.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        });

        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateProfile();
            }
        };

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PUSHED NOTI");
                NotificationManager notiMNG=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify = new NotificationCompat.Builder
                        (getApplicationContext()).setContentTitle("NOTIFICATION").setContentText(noNoti + "").
                        setContentTitle("PUSHED NOTI").setSmallIcon(R.drawable.com_facebook_button_icon).build();
                notiMNG.notify(noNoti, notify);
                noNoti++;
            }
        });
    }

    private void initialize(){
        callbackManager = CallbackManager.Factory.create();
        fbNameTV = (TextView) findViewById(R.id.fbNameTV);
        loginBtn = (LoginButton) findViewById(R.id.loginBtn);
        profilePV = (ProfilePictureView) findViewById(R.id.profilePV);
        notifyBtn = (Button) findViewById(R.id.notifyBtn);
        noNoti = 1;
    }

    private void updateProfile()
    {
        if(Profile.getCurrentProfile() == null)
        {
            fbNameTV.setText("NOT LOGGED IN");
            profilePV.setProfileId(null);
        }
        else {
            fbNameTV.setText(Profile.getCurrentProfile().getName());
            profilePV.setProfileId(Profile.getCurrentProfile().getId());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
