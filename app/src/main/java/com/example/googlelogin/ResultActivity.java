package com.example.googlelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ResultActivity extends AppCompatActivity {
    private TextView tv_result;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        String photourl = intent.getStringExtra("photourl");

        tv_result = findViewById(R.id.tv_result);
        tv_result.setText(nickname);

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photourl).into(iv_profile);

    }
}