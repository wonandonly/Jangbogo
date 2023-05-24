package com.example.part2_1_firstactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import adapter.CustomChoiceListViewAdapter;


public class ShopActivity extends AppCompatActivity {

    private Button backBtn;

    ListView listview ;
    ArrayList<String> ReceiveArr;
    CustomChoiceListViewAdapter adapter;

    // Adapter 생성


    // 리스트뷰 참조 및 Adapter달기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent intent2 = getIntent();

        adapter = new CustomChoiceListViewAdapter() ;
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        ArrayList<String> ReceiveArr = intent2.getStringArrayListExtra("ArrayList");
        adapter.addItem(ReceiveArr);


        Button moveButton = findViewById(R.id.backBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        backBtn = findViewById(R.id.backBtn);

    }
    private void addEventListener() {
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        });

    }

}
