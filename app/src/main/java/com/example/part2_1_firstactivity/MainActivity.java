package com.example.part2_1_firstactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView obj1=new TextView(this);
//        obj1.setText("Welcome! 모바일 프로그래밍(Java Code)");
//        setContentView(obj1);

        //imageview 객체 생성: resource 저장한 img에 대한 객체 생성
//        ImageView iv_pinwheel=(ImageView) findViewById(R.id.pinwheel);
        ImageView iv_pinwheel=(ImageView) findViewById(R.id.logo);

        //pinwheel 이미지를 360도 회전하기 위한 animator class 사용함
        //ObjectAnimator object=ObjectAnimator.ofFloat(iv_pinwheel,"rotation",360); //360도 회전
//        ObjectAnimator object= ObjectAnimator.ofFloat(iv_pinwheel,"rotation",360);
//        object.setInterpolator(new LinearInterpolator()); //일정한 속도로 회전
//        object.setDuration(2000);
//        object.setRepeatCount(ValueAnimator.INFINITE); //loop
//        object.start(); //animation start
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        }, 2000);
    }
}