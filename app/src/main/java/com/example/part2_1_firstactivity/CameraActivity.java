package com.example.part2_1_firstactivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContentValues;
import android.net.Uri;

import java.io.OutputStream;
import java.io.IOException;

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

        // 권한 요청
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // 사진 찍고 가져오기
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // 결과 OK, 데이터 null 아니면
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // 권한이 허용되어 있는지 다시 확인
                        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            // 권한이 허용된 경우 사진 촬영 및 저장 로직 실행
                            Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                            saveImageToGallery(imageBitmap);
                        } else {
                            // 권한이 거부된 경우 처리할 로직 추가
                            Toast.makeText(CameraActivity.this, "외부 저장소에 접근할 수 없어 사진을 저장할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
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

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private void captureAndSaveImage() {
        // 카메라 앱을 실행하여 사진 캡처
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            activityResultPicture.launch(intent);
        } else {
            Toast.makeText(this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String displayName = "Image.jpg";
        String mimeType = "image/jpeg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (imageUri != null) {
            try {
                OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // 권한이 허용된 경우 사진 촬영 및 저장 로직 실행
                    captureAndSaveImage();
                } else {
                    // 권한이 거부된 경우 처리할 로직 추가
                    Toast.makeText(this, "외부 저장소에 접근할 수 없어 사진을 저장할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
    );
}
