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

            // 주문!
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
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("productName") + ", " + tmp.get("productPrice"));
                                    totalPrice += (long)tmp.get("productPrice");
                                    // 이건 업데이트 cart 비우기
                                    /*db.collection("cart").document("BLEuHZyMKTlpoAKPA1EM")
                                            .update("productType", false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                }
                                            });*/

                                    // 이건 삭제 cart 비우기
                                    // 장바구니 업데이트 (지금은 삭제)
                                    /*db.collection("cities").document("DC")
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });*/
                                }
                                Log.d(TAG, "list totalPrice : " + totalPrice);
                                Map tmp = (Map) list.get(0);
                                String str = tmp.get("productName") + "외 " + (list.size() - 1) + "건";
                                Log.d(TAG, "list product : " + str);
                                //Log.d(TAG, );
                                /*Map<String, Object> credit = new HashMap<>();
                                    credit.put("usreId", jId);
                                    credit.put("datetime", FieldValue.serverTimestamp());
                                    credit.put("totalPrice", totalPrice);
                                    credit.put("product", "리스트로? 합쳐서??");


                                    db.collection("credit")
                                            .add(credit)
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
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

            /*// 상품 검색
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
                                for (int i = 0; i < list.size(); i++){
                                    Map tmp = (Map) list.get(i);
                                    Log.d(TAG, "list ->" + tmp.get("search") + " : " + tmp.get("name") + ", " + tmp.get("size") + ", " + tmp.get("price") + "원");
                                    // addToChat(question, Message.SENT_BY_BOT);
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/

            /*// 상품 추가
            Map<String, Object> product = new HashMap<>();
            product.put("search", "녹말물 2큰술 (녹말 1큰술 + 물 2큰술)");
            product.put("name", "[노브랜드] 감자맛전분99.9");
            product.put("size", "350g");
            product.put("price", 2580);

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

            /*/ 레시피 검색
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
                    });*/

            /*// 장바구니 리스트
            Task<QuerySnapshot> task = db.collection("cart")
                    .whereEqualTo("userId", jId)
                    //.whereEqualTo("productType", true)
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

            /*// 장바구니 추가
            Map<String, Object> cart = new HashMap<>();
            cart.put("userId", jId);
            cart.put("datetime", FieldValue.serverTimestamp());
            cart.put("productId", tmp.get("key"));
            cart.put("productName", tmp.get("name"));
            cart.put("productPrice", tmp.get("price"));
            cart.put("productSize", tmp.get("size"));
            cart.put("productType", true);

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