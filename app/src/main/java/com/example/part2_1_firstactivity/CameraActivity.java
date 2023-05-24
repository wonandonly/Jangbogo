package com.example.part2_1_firstactivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;


public class CameraActivity extends AppCompatActivity{
    Bitmap bitmap;
    ImageView imageView;
    Intent intent;
    TextToSpeech tts;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initData();
        addEventListener();

        imageView = findViewById(R.id.CameraimageView);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultPicture.launch(intent);
    }

    // 사진 찍고 가져오기
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //결과 OK, 데이터 null 아니면
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();
                        //데이터 bitmap에 담기
                        bitmap = (Bitmap) extras.get("data");
                        //이미지뷰에 bit 담기
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void initData() {
        homeBtn = findViewById(R.id.homeBtn);
    }
    private void addEventListener() {
        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
        });
    }

}
