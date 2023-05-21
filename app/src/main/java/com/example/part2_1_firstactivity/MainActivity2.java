package com.example.part2_1_firstactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "TAG";
    public static final String KEY_NAME = "key name";

    private EditText nameEditText;
    private Button okBtn;
    private ImageButton micBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        TextView obj1=new TextView(this);
//        obj1.setText("second page");
//        setContentView(obj1);
        initData();
        addEventListener();
    }

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