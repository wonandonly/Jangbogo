package com.example.part2_1_firstactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    private TextView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        view1 = findViewById(R.id.nameTextView);
//        if (getIntent() != null) {
//            String getData = getIntent().getStringExtra(MainActivity2.KEY_NAME);
//            view1.setText(getData);
//        }
    }
}