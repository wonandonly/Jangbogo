package com.example.part2_1_firstactivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.MessageAdapter;
import model.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




//import android.telecom.Call;

public class MainActivity4 extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "TAG";
    private ImageButton shopBtn;
    Intent intent;
    Intent intent2;
    SpeechRecognizer mRecognizer;
    ImageButton sttBtn;

    Button saverecipeBtn;
    Button gotolistBtn;

    TextView textView;
    String resultStr;
    String resultStr2;
    String foodName;
    final int PERMISSION = 1;

    RecyclerView recycler_view;
    TextView tv_welcome;
    EditText et_msg;
    ImageButton btn_send;

    List<Message> messageList;
    MessageAdapter messageAdapter;
    private FirebaseFirestore secondaryFirestore;
    private FirebaseFirestore db;
    TextToSpeech tts;



    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client;
    private String cookingProcedure;
    private List<String> ingredients;
    String jId = "t9S9FGgV7ygPMNAtX8Oj";

    private static final String MY_SECRET_KEY = "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";



    FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId("1:374943218129:android:87622e9ac90f089fdc88f0")
            .setProjectId("jangbogo-app")
            .setDatabaseUrl("https://jangbogo-app.firebaseio.com")
            .build();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
//        initData();
//        addEventListener();
        sttBtn = findViewById(R.id.imageBtn);
        btn_send = findViewById(R.id.btn_send);
        recycler_view = findViewById(R.id.recycler_view);
        tv_welcome = findViewById(R.id.tv_welcome);
        et_msg = findViewById(R.id.et_msg);


        //jangbogo어플 서버 초기화
        //FirebaseApp.initializeApp(getApplicationContext(), options, "secondary");
        secondaryFirestore = FirebaseFirestore.getInstance(FirebaseApp.getInstance("secondary"));
        db= FirebaseFirestore.getInstance();


        recycler_view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recycler_view.setLayoutManager(manager);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recycler_view.setAdapter(messageAdapter);

//        // 첫 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.setting),
//                "Account Box Black 36dp") ;
//        // 두 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.setting),
//                "Account Circle Black 36dp") ;
//        // 세 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.setting),
//                "Assignment Ind Black 36dp") ;


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = et_msg.getText().toString().trim();
                addToChat(question, Message.SENT_BY_ME);
                et_msg.setText("");
                callAPI(question);
                tv_welcome.setVisibility(View.GONE);
            }
        });

        client = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        // 퍼미션 체크
        if ( Build.VERSION.SDK_INT >= 23 ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        // xml의 버튼과 텍스트 뷰 연결
        textView = (TextView)findViewById(R.id.tv_welcome);
        sttBtn = (ImageButton)findViewById(R.id.imageBtn);
        saverecipeBtn=(Button)findViewById(R.id.save_recipe_button);
        saverecipeBtn.setVisibility(View.INVISIBLE);
        gotolistBtn=(Button)findViewById(R.id.goto_list_button);
        gotolistBtn.setVisibility(View.INVISIBLE);

        // RecognizerIntent 객체 생성
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        //tts 객체 생성, 초기화
        tts = new TextToSpeech(MainActivity4.this, (TextToSpeech.OnInitListener) this);

        // 버튼을 클릭 이벤트 - 객체에 Context와 listener를 할당한 후 실행

        sttBtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity4.this);
                                          mRecognizer.setRecognitionListener(listener);
                                          mRecognizer.startListening(intent);
                                      }
                                  }

        );

        gotolistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent2 = new Intent(getApplicationContext(),ShopActivity.class);
                Intent intent2 = new Intent(getApplicationContext(), ShopActivity.class);
                intent2.putStringArrayListExtra("ArrayList", (ArrayList<String>) ingredients);
                startActivity(intent2);
            }
        });

        saverecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> recipe = new HashMap<>();
                recipe.put("datetime", FieldValue.serverTimestamp());
                recipe.put("ingredient", ingredients);
                recipe.put("jId", jId);
                recipe.put("name", foodName);
                recipe.put("recipe", cookingProcedure);

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
                        });
                Toast.makeText(getApplicationContext(), "레시피가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

//<<<<<<< HEAD
////        ImageButton moveButton = findViewById(R.id.shop_btn);
//=======
//        //ImageButton moveButton = findViewById(R.id.shop_btn);
//>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267
//        moveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (getApplicationContext(), ShopActivity.class);
//                startActivity(intent);
//            }
//        });
    }




    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recycler_view.smoothScrollToPosition(messageAdapter.getItemCount());
                ///////////////////////////////////
                /////////////////////speechLog////////////////
                //////////////////////////////////
            }
        });
    }
    //실행시에도 chatgpt를 부르지 않는 음성인식 모델
    private RecognitionListener listener2 = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for (int i = 0; i < matches.size(); i++) {
                resultStr2 = matches.get(i);
                addToChat(resultStr2, Message.SENT_BY_ME);
                et_msg.setText("");

            }
            if(resultStr2.length() < 1) return;
            resultStr2 = resultStr2.replace(" ", "");
            saverecipeBtn.setVisibility(View.INVISIBLE);
            gotolistBtn.setVisibility(View.INVISIBLE);

            Task<QuerySnapshot> searchTask = secondaryFirestore.collection("recipe")
                    .whereEqualTo("jId", jId)
                    .whereEqualTo("name", resultStr2)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> searchTask) {
                            List list = new ArrayList();
                            String recipeSearchStr = "";
                            if (searchTask.isSuccessful()) {
                                for (QueryDocumentSnapshot document : searchTask.getResult()) {
                                    Map map = document.getData();
                                    map.put("key", document.getId());
                                    list.add(map);
                                    //Log.d(TAG, list.size() + "list map : " + map.get("name"));
                                }
                                Log.d(TAG, "list size : " + list.size());
                                if(list.isEmpty()){
                                    addToChat("해당 레시피가 저장되지 않았습니다.", Message.SENT_BY_BOT);
                                }else {
                                    for (int i = 0; i < list.size(); i++) {
                                        Map tmp = (Map) list.get(i);
                                        recipeSearchStr += tmp.get("name") + " 레시피\n" + tmp.get("recipe") + "\n";
                                        Log.d(TAG, tmp.get("key") + ", list : " + tmp.get("name") + ", " + tmp.get("recipe"));

                                    }
                                    addToChat(recipeSearchStr, Message.SENT_BY_BOT);
                                    addToChat("요리 완성 사진을 찍으려면 카메라라고 말씀해주세요.", Message.SENT_BY_BOT);
                                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity4.this);
                                    mRecognizer.setRecognitionListener(listener2);
                                    try {
                                        TimeUnit.SECONDS.sleep(3);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mRecognizer.startListening(intent);
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", searchTask.getException());
                            }
                        }
                    });

            if(resultStr2.indexOf("카메라") > -1) {
                String guideSt = "사진을 찍겠습니다.";
                Toast.makeText(getApplicationContext(), guideSt, Toast.LENGTH_SHORT).show();
                funcVoiceOut(guideSt);

                Intent Cameraintent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(Cameraintent);
            }


        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };



    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            String[] words = matches.get(0).split(" ");  // 문장을 공백으로 분리하여 단어 배열 생성
            if (words.length > 0) {
                foodName =  words[0];  // 첫 번째 단어 반환
            }
            for (int i = 0; i < matches.size(); i++) {
                resultStr = matches.get(i);
                addToChat(resultStr, Message.SENT_BY_ME);
                et_msg.setText("");

            }
            if(resultStr.length() < 1) return;
            resultStr = resultStr.replace(" ", "");
            saverecipeBtn.setVisibility(View.INVISIBLE);
            gotolistBtn.setVisibility(View.INVISIBLE);


            if(resultStr.indexOf("저장") > -1) {
                tv_welcome.setVisibility(View.GONE);
                String guideSt = "저장한 레시피를 불러옵니다.";
                Toast.makeText(getApplicationContext(), guideSt, Toast.LENGTH_SHORT).show();
                funcVoiceOut(guideSt);
                Task<QuerySnapshot> task = secondaryFirestore.collection("recipe")
                        .whereEqualTo("jId", jId)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List list = new ArrayList();
                                String recipeStr = "";
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
                                        recipeStr += (i + 1) +". " + tmp.get("name")+"\n";
                                        // 함수 넣으면 됨! -
                                        //addToChat(i+". " + tmp.get("name"), Message.SENT_BY_BOT);
                                    }
                                    addToChat(recipeStr, Message.SENT_BY_BOT);
//<<<<<<< HEAD
//=======
                                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity4.this);
                                    mRecognizer.setRecognitionListener(listener2);
                                    try {
                                        TimeUnit.SECONDS.sleep(3);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mRecognizer.startListening(intent);

//
//>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
//<<<<<<< HEAD
//=======
//
//>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267
            }
            else {
                callAPI(resultStr);
                tv_welcome.setVisibility(View.GONE);
                moveActivity(resultStr);
            }

//<<<<<<< HEAD
            /*if(resultStr.indexOf("카메라") > -1) {
=======
            if(resultStr.indexOf("카메라") > -1) {
>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267
                String guideSt = "사진을 찍겠습니다.";
                Toast.makeText(getApplicationContext(), guideSt, Toast.LENGTH_SHORT).show();
                funcVoiceOut(guideSt);

                Intent Cameraintent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(Cameraintent);
<<<<<<< HEAD
            }*/
//=======
//            }
//>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267



        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

    void addResponse(String response){
        //messageList.remove(messageList.size()-1);
        addToChat(response, Message.SENT_BY_BOT);
    }
    void callAPI(String question) {
        //okhttp
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            //AI 속성설정
            baseAi.put("role", "user");
            baseAi.put("content", "요리 관련 정보를 요구할 때 항상 \"재료:\"와 \"만드는 방법:\"이 들어가게 답변해줘");
            //유저 메세지
            userMsg.put("role", "user");
            userMsg.put("content", question);
            //array로 담아서 한번에 보낸다
            arr.put(baseAi);
            arr.put(userMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try {
            //모델명 변경
            object.put("model", "gpt-3.5-turbo");
            object.put("messages", arr);
//            아래 put 내용은 삭제하면 된다
//            object.put("model", "text-davinci-003");
//            object.put("prompt", question);
//            object.put("max_tokens", 4000);
//            object.put("temperature", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                .header("Authorization", "Bearer " + MY_SECRET_KEY)
                .post(body)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }
            List<String> extractIngredients(String recipe) {
                List<String> ingredients = new ArrayList<>();
                //Pattern pattern = Pattern.compile("([가-힣]+): ([0-9/가-힣 ]+)");

                int startIdx = recipe.indexOf("재료:");
                int endIdx = recipe.indexOf("만드는 방법:");
                if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) {
                    return ingredients; // 재료 또는 조리 절차 텍스트가 없는 경우 빈 목록 반환
                }
                String ingredientText = recipe.substring(startIdx + 4, endIdx); // "재료:" 다음 문자부터 추출
                String[] ingredientLines = ingredientText.split("\n");

                for (String line : ingredientLines) {
                    line = line.trim(); // 양쪽 공백 제거
                    if (!line.isEmpty()) {
                        ingredients.add(line.replace("-", "").trim());
                    }
                }

                return ingredients;
            }

            String extractCookingProcedure(String recipe) {
                Pattern pattern2 = Pattern.compile("만드는 방법:\\s+([\\s\\S]+)");
                Matcher matcher2 = pattern2.matcher(recipe);

                if (matcher2.find()) {
                    return matcher2.group(1).trim();
                }

                return ""; // If cooking procedure not found
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        if (jsonArray.length() > 0) {
                            //로그 전송
                            String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                            Map<String, Object> voiceLog = new HashMap<>();
                            voiceLog.put("datetime", FieldValue.serverTimestamp());
                            voiceLog.put("inputText", question);
                            voiceLog.put("outputText", result);
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
                                    });

                            ingredients = extractIngredients(result);
                            addResponse(result.trim());
                            //Log.d(TAG, responseBody);
                            //Log.d(TAG, result);

                            // Add ingredients to chat(하나씩 추가)
                            for (String ingredient : ingredients) {

                                //addResponse(ingredient);
                                //여기서 재료 장바구니로 보내면 됨(변수: ingredient)

                            }
                            
                            //ShopActivity로 넘어감
                            //Intent intent2 = new Intent(getApplicationContext(),ShopActivity.class);
                            //intent.putStringArrayListExtra("ArrayList", (ArrayList<String>) ingredients);

                            if (!ingredients.isEmpty()) {
                                //Log.d(TAG, ingredients.get(0));
                                cookingProcedure = extractCookingProcedure(result);

                                //레시피
                                //Log.d(TAG, cookingProcedure);
                                //addResponse(cookingProcedure);
//<<<<<<< HEAD

                            } else {
                                addResponse("No ingredients found in the recipe.");
//=======
//>>>>>>> db111e8c2dd2fba2442b4533f68032c4673a0267
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }

            }
        });
    }


    public void moveActivity(String resultStr) {

        if(resultStr.indexOf("레시피") > -1) {
            String guideStr = "액티비티를 넘어갑니다.";
            Toast.makeText(getApplicationContext(), guideStr, Toast.LENGTH_SHORT).show();
            funcVoiceOut(guideStr);

            //List<String> ingredients = extractIngredients(resultStr);
            //String cookingProcedure = extractCookingProcedure(resultStr);
            //addToChat("재료를 장바구니에 넣으려면 장바구니, 레시피 저장을 하시려면 레시피저장이라고 말씀해주세요", Message.SENT_BY_BOT);
            //재료, 만드는 방법 잘 받아와지는지 확인

            //Log.d(TAG, ingredients.get(1));
            //Log.d(TAG, cookingProcedure);

            //Log.d(TAG, ingredients.size());




            //Intent intent = new Intent(getApplicationContext(), NextActivity.class);
            //startActivity(intent);

            //장바구니로 가기, 레시피 저장하기 버튼 생김
            saverecipeBtn.setVisibility(View.VISIBLE);
            gotolistBtn.setVisibility(View.VISIBLE);

        }

    }
    public void funcVoiceOut(String OutMsg){
        if(OutMsg.length()<1)return;
        if(!tts.isSpeaking()) {
            tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(1);
        } else {
            //Log.e("TTS", "초기화 실패");
        }
    }


//    private void initData() {
//        shopBtn = findViewById(R.id.shop_btn);
//    }
//    private void addEventListener() {
//        shopBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, ShopActivity.class);
//            startActivity(intent);
//        });
//    }

}
