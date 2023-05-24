package com.example.part2_1_firstactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import adapter.CustomChoiceListViewAdapter;


public class ShopActivity extends AppCompatActivity {

    private Button backBtn;

    ListView listview;
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
        ArrayList<String> ReceiveArr = intent2.getStringArrayListExtra("ArrayList"); //***
        adapter.addItem(ReceiveArr);


//        SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
        //checkedItems.get(0); <checked면 0, not checked면 1을 반환
        //int count = adapter.getCount() ;
        SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<>();

        for (int i = 0; i < checkedItems.size(); i++) {
            int position = checkedItems.keyAt(i);
            if (checkedItems.valueAt(i)) {
                //String item =  adapter.getItem(position);
                String item = (String) adapter.getItem(position);
                selectedItems.add(item);
            }
        }


        Button moveButton = findViewById(R.id.backBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OrderActivity로 넘어감
                Intent intent = new Intent (getApplicationContext(), OrderActivity.class);
                intent.putStringArrayListExtra("SelectedItems", selectedItems);
                ////intent.putStringArrayListExtra("ArrayList", (ArrayList<String>) ingredients);
                startActivity(intent);

//                Intent intent = new Intent(ShopActivity.this, OrderActivity.class);
                    //startActivity(intent);

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
