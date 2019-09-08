package com.example.sathi.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sathi.MainActivity;
import com.example.sathi.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    LoginButton facebookLogin;

    CallbackManager callbackManager;
    String firstName, lastName, email, userId, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);


        facebookLogin.setReadPermissions("email", "public_profile");

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this, "User Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();

            }
        });


        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    Toast.makeText(LoginActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                } else {
                    loadUserProfile(currentAccessToken);
                }

            }
        };


    }

    private void loadUserProfile(AccessToken currentAccessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");
                    email = object.getString("email");
                    userId = object.getString("id");
                    imageUrl = "https://graph.facebook.com/" + userId + "/picture?type=normal";

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("image", imageUrl);
                    System.out.println("First Name" +firstName);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,id,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode,resultCode,data);

        super.onActivityResult(requestCode, resultCode, data);
    }
}

