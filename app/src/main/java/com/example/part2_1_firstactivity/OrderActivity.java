package com.example.part2_1_firstactivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        adapter = new CustomChoiceListViewAdapter() ;
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        adapter.addItem((ArrayList<ListViewItem>) list);

        Button moveButton = findViewById(R.id.payBtn);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent (getApplicationContext(), OrderActivity.class);
//                startActivity(intent);


            }
        });
    }

    private void addEventListener() {
        payBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, OrderActivity.class);
//            startActivity(intent);

            ////////////백//////////////
        });

    }
}

//
//        ArrayList<String> stringList = new ArrayList<>();
//        stringList.add("Value 1");
//        stringList.add("Value 2");
//        stringList.add("Value 3");
//
//        for (String value : stringList) {
//            Log.d("ArrayListValues", value);
//        }

//        Intent intent = getIntent();
//        ArrayList<String> selectedItems = intent.getStringArrayListExtra("SelectedItems");
//        for (String value :selectedItems) {
//            //Log.d("striglist", value);
//            Log.d(TAG, "stringlist : " + value);

//}