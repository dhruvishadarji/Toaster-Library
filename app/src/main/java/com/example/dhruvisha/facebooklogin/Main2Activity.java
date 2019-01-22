package com.example.dhruvisha.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {
    TextView name;
    TextView email;
    ImageView profilePic;
    Button logout;
    JSONObject response, profile_pic_data, profile_pic_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=getIntent();
        String jsonData=intent.getStringExtra("userProfile");
        name=findViewById(R.id.username);
        email=findViewById(R.id.email);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    LoginManager.getInstance().logOut();
                    Intent login = new Intent(Main2Activity.this, FaceBookLoginActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);
                    finish();

            }
        });
        profilePic=findViewById(R.id.profilepic);
        try {
            response = new JSONObject(jsonData);
            email.setText(response.get("email").toString());
            name.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(profilePic);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
