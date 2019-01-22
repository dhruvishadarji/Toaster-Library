package com.example.dhruvisha.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

public class FaceBookLoginActivity extends AppCompatActivity {

    LoginButton faceBppkLogin;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_face_book_login);
        faceBppkLogin = findViewById(R.id.facebooklogin);
        callbackManager = CallbackManager.Factory.create();
        faceBppkLogin.setReadPermissions("email");
        faceBppkLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FaceBookLoginActivity.this, "Login cancle", Toast.LENGTH_SHORT).show();
                Log.d("error","cancle->");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FaceBookLoginActivity.this, "Login failed\n" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error","erorrr->"+error);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Intent intent = new Intent(FaceBookLoginActivity.this, Main2Activity.class);
                        intent.putExtra("userProfile", json_object.toString());
                        Log.d("Login attempt", json_object.toString());
                        startActivity(intent);
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }
}



