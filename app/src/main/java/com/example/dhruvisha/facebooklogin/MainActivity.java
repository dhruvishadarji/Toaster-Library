package com.example.dhruvisha.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    LoginButton facebooklogin;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        checkForExistingLogin();
        facebooklogin = findViewById(R.id.login_button);
        facebooklogin.setReadPermissions("email");
       // callbackManager = CallbackManager.Factory.create();
     /*   LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(MainActivity.this, "Login suceesful" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this, "Login cancle", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this, "Login failed\n" + exception.toString(), Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();

                    }
                });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void loginWithFB() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,
                        "Facebook login was cancelled.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,
                        "Facebook authentication failed. " +
                                "Please check internet connection. " +
                                "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getUserDetails(LoginResult loginResult) {
        Log.d(TAG, "getUserDetails: access token -> " + loginResult
                .getAccessToken().getToken());
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult
                .getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                try {
                    Intent intent = new Intent(MainActivity.this,
                            Main2Activity.class);
                    /*Log.d(TAG, "name: " + jsonObject.get("name").toString());
                    Log.d(TAG, "email: " + jsonObject.get("email").toString());
                    JSONObject pic = new JSONObject(jsonObject.get("picture").toString());
                    JSONObject url_data = new JSONObject(pic.getString("data"));
                    Log.d(TAG, "url: " + url_data.getString("url"));*/
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,
                            "Facebook authentication failed. " +
                                    "Please check internet connection. " +
                                    "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email," +
                "picture.width(120).height(120)");

        graphRequest.setParameters(permission_param);
        graphRequest.executeAsync();
    }
    private void checkForExistingLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken
                    .getCurrentAccessToken(), new GraphRequest
                    .GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                    try {
                        Intent intent = new Intent(MainActivity.this,
                                Main2Activity.class);

                        Log.d(TAG, "name: " + jsonObject.get("name").toString());
                        Log.d(TAG, "email: " + jsonObject.get("email").toString());
                        JSONObject pic = new JSONObject(jsonObject.get("picture").toString());
                        JSONObject url_data = new JSONObject(pic.getString("data"));
                        Log.d(TAG, "url: " + url_data.getString("url"));


                        Log.d(TAG, "name: " + jsonObject.get("name").toString() +
                                "\n" + "email: " + jsonObject.get("email").toString() +
                                "\n" + "url: " + url_data.getString("url"));

                        intent.putExtra("userProfile", jsonObject.toString());
                        intent.putExtra("SocialMedia", "Facebook");

                        startActivity(intent);


                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Facebook authentication failed. " +
                                        "Please check internet connection. " +
                                        "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                }
            });

            Bundle permission_param = new Bundle();
            permission_param.putString("fields", "id,name,email," +
                    "picture.width(120).height(120)");

            graphRequest.setParameters(permission_param);
            graphRequest.executeAsync();

            //Toast.makeText(this, "login exists", Toast.LENGTH_SHORT).show();
        } else {
            loginWithFB();
        }
    }
}
