package com.example.part2_1_firstactivity;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.CustomChoiceListViewAdapter;


public class ShopActivity extends AppCompatActivity {

    private Button backBtn;
    private FirebaseFirestore db;

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

        adapter = new CustomChoiceListViewAdapter();
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
//        ArrayList<String> ReceiveArr = intent2.getStringArrayListExtra("ArrayList"); //***
//        adapter.addItem(ReceiveArr);
        ArrayList<String> ReceiveArr = intent2.getStringArrayListExtra("ArrayList");
        ArrayList<ListViewItem> itemList = new ArrayList<>();

        for (String ingredient : ReceiveArr) {
            ListViewItem item = new ListViewItem();
            item.setText(ingredient);
            itemList.add(item);
        }

        adapter.addItem(itemList);


//        SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
        //checkedItems.get(0); <checked면 0, not checked면 1을 반환
        //int count = adapter.getCount() ;


        db = FirebaseFirestore.getInstance();    // shop

        Button moveButton = findViewById(R.id.backBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ListViewItem> selectedItems = new ArrayList<>();
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();

                for (int i = 0; i < checkedItems.size(); i++) {
                    int position = checkedItems.keyAt(i);
                    if (checkedItems.valueAt(i)) {
                        //String item =  adapter.getItem(position);
                        ListViewItem item = (ListViewItem) adapter.getItem(position);
                        selectedItems.add(item);

                    }
                }
                ArrayList<String> selectedStrings = new ArrayList<>();
                for (ListViewItem item : selectedItems) {
                    selectedStrings.add(item.getText());
                    Log.d(TAG, "Selected item: " + item.getText());
                }

                //OrderActivity로 넘어감
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                intent.putStringArrayListExtra("SelectedItems", selectedStrings);
                startActivity(intent);
                ////intent.putStringArrayListExtra("ArrayList", (ArrayList<String>) ingredients);
                //startActivity(intent);

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
            String jId = "t9S9FGgV7ygPMNAtX8Oj";

            int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                // 상품 검색
                Task<QuerySnapshot> task = db.collection("product").where(Filter.or(
                        Filter.equalTo("search", "미역 30g"),
                        Filter.equalTo("search", "돼지고기 혹은 소고기 100g"),
                        Filter.equalTo("search", "다시마 5x5cm 크기 한 조각"),
                        Filter.equalTo("search", "김치국물 1/2컵"),
                        Filter.equalTo("search", "된장 1큰술"),
                        Filter.equalTo("search", "다진마늘 1작은술"),
                        Filter.equalTo("search", "소금 약간"),
                        Filter.equalTo("search", "후추 약간"),
                        Filter.equalTo("search", "참기름 1작은술"),
                        Filter.equalTo("search", "녹말물 2큰술 (녹말 1큰술 + 물 2큰술)"),
                        Filter.equalTo("search", ""),
                        Filter.equalTo("search", "")
                )).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List list = new ArrayList();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map map = document.getData();
                                map.put("key", document.getId());
                                list.add(map);
                                //Log.d(TAG, list.size() + "list map : " + map.get("name"));
                            }
                            //Log.d(TAG, "list size : " + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                Map tmp = (Map) list.get(i);
                                Log.d(TAG, "list ->" + tmp.get("search") + " : " + tmp.get("name") + ", " + tmp.get("size") + ", " + tmp.get("price") + "원");

                                // add to cart (일단 주석처리하께요오)
                                /*Map<String, Object> cart = new HashMap<>();
                                cart.put("userId", jId);
                                cart.put("datetime", FieldValue.serverTimestamp());
                                cart.put("productId", tmp.get("key"));
                                cart.put("productName", tmp.get("name"));
                                cart.put("productPrice", tmp.get("price"));
                                cart.put("productSize", tmp.get("size"));
                                cart.put("productType", tmp.get("type"));

                                db.collection("cart")
                                        .add(cart)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });*/
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }

            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
//            Intent intent2 = new Intent(getApplicationContext(), ShopActivity.class);
//            intent2.putStringArrayListExtra("ArrayList", (ArrayList<String>) ingredients);
//            startActivity(intent2);
        });

    }
}