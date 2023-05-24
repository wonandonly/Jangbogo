package com.example.part2_1_firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
//
//        ArrayList<String> stringList = new ArrayList<>();
//        stringList.add("Value 1");
//        stringList.add("Value 2");
//        stringList.add("Value 3");
//
//        for (String value : stringList) {
//            Log.d("ArrayListValues", value);
//        }

        Intent intent = getIntent();
        ArrayList<String> selectedItems = intent.getStringArrayListExtra("SelectedItems");
        for (String value :selectedItems) {
            Log.d("striglist", value);
        }

    }
}