package com.example.part2_1_firstactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "TAG";
    public static final String KEY_NAME = "key name";

    private EditText nameEditText;
    private Button okBtn;
    private ImageButton micBtn;
    private FirebaseFirestore secondaryFirestore;
    private FirebaseFirestore db;

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId("1:374943218129:android:87622e9ac90f089fdc88f0")
            .setProjectId("jangbogo-app")
            .setDatabaseUrl("https://jangbogo-app.firebaseio.com")
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        TextView obj1=new TextView(this);
//        obj1.setText("second page");
//        setContentView(obj1);
        initData();
        addEventListener();

        FirebaseApp.initializeApp(getApplicationContext(), options, "secondary");
        secondaryFirestore = FirebaseFirestore.getInstance(FirebaseApp.getInstance("secondary"));
        db= FirebaseFirestore.getInstance();    // shop

        shopLogin();
    }

    private void shopLogin(){
        String jId = "t9S9FGgV7ygPMNAtX8Oj";

        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            Log.d(TAG, "wifi");

            Task<QuerySnapshot> task = secondaryFirestore.collection("user")
            .whereEqualTo("jId", jId)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List list = new ArrayList();
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map map = document.getData();
                                    map.put("key", document.getId());
                                    list.add(map);
                                    Log.d(TAG, "wifi" + map.get("shopId"));
                                    // 쇼핑몰 로그인 하기!!
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    /*@Override
    public void onComplete(@NonNull Task task) {
        Log.d(TAG, jId + "test");
                    if(task.isSuccessful()알){
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult();
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
    }*/


    private void initData() {
        //nameEditText = findViewById(R.id.nameEditText);
        okBtn = findViewById(R.id.Button1);
        micBtn=findViewById(R.id.imageButton2);
    }

    private void addEventListener() {
        okBtn.setOnClickListener(view -> {
//            1. 현재 nameEditText 뷰 컴포넌트에 값을 가져온다. (방어적 코드)
//            2. 화면 전환 로직 (인텐트)
            //String name = nameEditText.getText().toString();


            /////////////////////////////////////////////////////////////////////
            ////////////////////아이디 비번 서버로 보내서 맞는지 확인(아이디 값 가져와 저장)
            //////////////////////////////////////////////////////////

            Intent intent = new Intent(this, MainActivity3.class);
            //intent.putExtra(KEY_NAME, name);
            startActivity(intent);
        });
        micBtn.setOnClickListener(view -> {
            // 이벤트 핸들러 로직 작성
            Intent intent = new Intent(this, MainActivity4.class);
            //intent.putExtra(KEY_NAME, name);
            startActivity(intent);
        });
    }


}