package com.example.part2_1_firstactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity3 extends AppCompatActivity {

    private TextView view1;
    private Button Btn;
    private EditText edit_id, edit_pw;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        view1 = findViewById(R.id.nameTextView);
//        if (getIntent() != null) {
//            String getData = getIntent().getStringExtra(MainActivity2.KEY_NAME);
//            view1.setText(getData);
//        }
        initData();
        addEventListener();
    }

    private void initData() {
        Btn = findViewById(R.id.button_login);
        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);
    }

    private void addEventListener() {
        Btn.setOnClickListener(view -> {
            mAuth = FirebaseAuth.getInstance();

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                //reload();
            }

            //Log.d(TAG, edit_id.getText() + ", " + edit_pw.getText());
            //String id = String.valueOf(edit_id.getText());
            //String pw = String.valueOf(edit_pw.getText());

            String id = "test@gmail.com";
            String pw = "admin123";

            mAuth.signInWithEmailAndPassword(id, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                //        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // logout = FirebaseAuth.getInstance().signOut();
                        }
                    });

        });
    }
}