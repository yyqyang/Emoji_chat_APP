package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    EditText userETLogin;
    Button loginBtn;
    Button tologinBtn;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    final String password = "aaaaaa";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        userETLogin = findViewById(R.id.editTextLogin);
        loginBtn = findViewById(R.id.loginBtn);
        tologinBtn = findViewById(R.id.TolonginBtn);


        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser !=null){
//            Intent i = new Intent(Login_Activity.this,MainActivity.class);
//            startActivity(i);
//            finish();
//        }

        tologinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Login_Activity.this,RegisterActivity.class);
                startActivity(i);

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String user_text_login = userETLogin.getText().toString();
                if(TextUtils.isEmpty(user_text_login)){
                    Toast.makeText(Login_Activity.this,"Please Fill The Username", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    auth.signInWithEmailAndPassword(user_text_login,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(Login_Activity.this,"Login failed",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}