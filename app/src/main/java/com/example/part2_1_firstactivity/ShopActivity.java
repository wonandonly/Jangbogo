package com.example.part2_1_firstactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopActivity extends AppCompatActivity {

    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        initData();
        addEventListener();
    }

    private void initData() {
        backBtn = findViewById(R.id.backBtn);
    }
    private void addEventListener() {
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
        });
    }

}
