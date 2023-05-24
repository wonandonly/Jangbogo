package com.example.part2_1_firstactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "TAG";
    public static final String KEY_NAME = "key name";

    private EditText nameEditText;
    private Button loginBtn;
    private ImageButton startBtn;
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

        // 로그인 코드 필요
        shopLogin();
    }

    private void shopLogin(){
        String jId = "t9S9FGgV7ygPMNAtX8Oj";

        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

            // 레시피 검색
            Task<QuerySnapshot> task = secondaryFirestore.collection("recipe")
                    .whereEqualTo("jId", jId)
                    .whereEqualTo("name", "전채요리")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                Log.d(TAG, "list size : " + list.size());
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("name") + ", " + tmp.get("recipe"));
                                    // addToChat(question, Message.SENT_BY_BOT);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

            /*// 장바구니 리스트
            Task<QuerySnapshot> task = db.collection("cart")
                    .whereEqualTo("userId", jId)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                Log.d(TAG, "list size : " + list.size());
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("productName"));
                                    // addToChat(question, Message.SENT_BY_BOT);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/


            /*// 상품 검색
            Task<QuerySnapshot> task = db.collection("product").where(Filter.or(
                            Filter.equalTo("type", "간장"),
                            Filter.equalTo("type", "두부"),
                            Filter.equalTo("type", ""),
                            Filter.equalTo("type", ""),
                            Filter.equalTo("type", ""),
                            Filter.equalTo("type", "")
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
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, "list : " + tmp.get("type") + " : " + tmp.get("name") + ", " + tmp.get("size"));
                                    // addToChat(question, Message.SENT_BY_BOT);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/

            /*// 상품 추가
            Map<String, Object> product = new HashMap<>();
            product.put("type", "김치");
            product.put("name", "종가집 김치");
            product.put("size", "1kg");

            db.collection("product")
                    .add(product)
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

            /*// 장바구니 추가
            Map<String, Object> cart = new HashMap<>();
            cart.put("userId", jId);
            cart.put("datetime", FieldValue.serverTimestamp());
            cart.put("productId", "7SVb5NDWCOCZksbPPlrv6");
            cart.put("productName", "풀무원 맛있는 간장");
            cart.put("productCount", 1);

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

            /*// 로그 추가
            Map<String, Object> voiceLog = new HashMap<>();
            voiceLog.put("datetime", FieldValue.serverTimestamp());
            voiceLog.put("inputText", "사용자 질문");
            voiceLog.put("outputText", "chat GPT 답변");
            voiceLog.put("jId", jId);
            voiceLog.put("resultType", 1);

            secondaryFirestore.collection("voiceLog")
                    .add(voiceLog)
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

            /*// 레시피 추가
            Map<String, Object> recipe = new HashMap<>();
            recipe.put("datetime", FieldValue.serverTimestamp());
            recipe.put("ingredient", "재료, 수량\\ 재료,수량\\");
            recipe.put("jId", jId);
            recipe.put("name", "후식");
            recipe.put("recipe", "testtest");

            secondaryFirestore.collection("recipe")
                    .add(recipe)
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

            /*// 레시피 리스트
            Task<QuerySnapshot> task = secondaryFirestore.collection("recipe")
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
                                    //Log.d(TAG, list.size() + "list map : " + map.get("name"));
                                }
                                Log.d(TAG, "list size : " + list.size());
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, "list : " + tmp.get("name"));
                                    // 함수 넣으면 됨! -
                                    // addToChat(question, Message.SENT_BY_BOT);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/

            /*Task<QuerySnapshot> task = secondaryFirestore.collection("user")
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
            });*/
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
        loginBtn = findViewById(R.id.Button1);
        startBtn=findViewById(R.id.imageButton2);
    }

    private void addEventListener() {
        loginBtn.setOnClickListener(view -> {
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
        startBtn.setOnClickListener(view -> {
            // 이벤트 핸들러 로직 작성
            Intent intent = new Intent(this, MainActivity4.class);
            //intent.putExtra(KEY_NAME, name);
            startActivity(intent);
        });
    }


}