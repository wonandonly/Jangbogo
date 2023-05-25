package com.example.part2_1_firstactivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import adapter.CustomListAdapter;

public class OrderActivity extends AppCompatActivity {

    Intent intent;
    private Button payBtn;

    private FirebaseFirestore db;
    CustomListAdapter customlistAdapter;
    RecyclerView recyclerView;
    ListView listview;
    CustomChoiceListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        db = FirebaseFirestore.getInstance();
        String jId = "t9S9FGgV7ygPMNAtX8Oj";
        List list = new ArrayList();

        Task<QuerySnapshot> task = db.collection("cart")
                .whereEqualTo("userId", jId)
                //.whereEqualTo("productType", true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //List list = new ArrayList();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map map = document.getData();
                                map.put("key", document.getId());
                                list.add(map);
                                //Log.d(TAG, list.size() + "list map : " + map.get("name"));
                            }
                            Log.d(TAG, "list size : " + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                Map tmp = (Map) list.get(i);
                                Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("productName"));
                                // addToChat(question, Message.SENT_BY_BOT);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

//        recyclerView = findViewById(R.id.recyclerView);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        customlistAdapter = new CustomListAdapter(list);
//        recyclerView.setAdapter(customlistAdapter);

        Intent intent2 = getIntent();

        adapter = new CustomChoiceListViewAdapter() ;
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        //Log.d(TAG, ShopActivity.getStrings().get(0));

//        adapter.addItem((ArrayList<ListViewItem>) list);
        ArrayList<String> ReceiveItems = intent2.getStringArrayListExtra("SelectedItems");
        ArrayList<ListViewItem> itemList = new ArrayList<>();

        if (ShopActivity.getStrings != null) {
            for (String item : ShopActivity.getStrings) {
                ListViewItem listItem = new ListViewItem();
                listItem.setText(item);
                itemList.add(listItem);
            }
        }

        adapter.addItem(itemList);

        Button moveButton = findViewById(R.id.payBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jId = "t9S9FGgV7ygPMNAtX8Oj";

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    // 상품 검색
                    List<Filter> filters = new ArrayList<>();
                    for (String selectedString : ShopActivity.selectedStrings) {
                        filters.add(Filter.equalTo("search", selectedString));
                    }

                    if (!filters.isEmpty()) {
                        db.collection("cart")
                                .whereEqualTo("userId", jId)
                                .whereEqualTo("productType", true)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List list = new ArrayList();
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map map = document.getData();
                                                map.put("key", document.getId());
                                                list.add(map);
                                            }
                                            long totalPrice = 0;
                                            Log.d(TAG, "list size : " + list.size());
                                            for (int i = 0; i < list.size(); i++) {
                                                Map tmp = (Map) list.get(i);
                                                Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("productName") + ", " + tmp.get("productPrice"));
                                                totalPrice += (long) tmp.get("productPrice");
                                            }
                                            Log.d(TAG, "list totalPrice : " + totalPrice);
                                            Map tmp1 = (Map) list.get(0);
                                            String str = tmp1.get("productName") + "외 " + (list.size() - 1) + "건, " + totalPrice + "원";
                                            Log.d(TAG, "list product : " + str);
                                            Toast.makeText(getApplicationContext(), "결제되었습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }

                                    }
                                });
                    }
                }
            }
        });
    }
}
