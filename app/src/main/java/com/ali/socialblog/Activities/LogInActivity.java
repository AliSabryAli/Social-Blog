package com.ali.socialblog.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ali.socialblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btLogIn;
    private Button btCreate;
    private EditText eEmail;
    private EditText ePass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setupUI();

        //Check Users States
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                  //  Toast.makeText(LogInActivity.this, "User Sign In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this, PostListActivity.class));
                    finish();

                } else {
                  //  Toast.makeText(LogInActivity.this, "User Failed Sign In", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }


    private void setupUI() {
        btLogIn = findViewById(R.id.btSignInID);
        btCreate = findViewById(R.id.btCreateID);
        eEmail = findViewById(R.id.eTxtEmailID);
        ePass = findViewById(R.id.eTxtPassID);
        btCreate.setOnClickListener(this);
        btLogIn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSignInID:

                if (!TextUtils.isEmpty(eEmail.getText().toString().trim())
                        && !TextUtils.isEmpty(ePass.getText().toString().trim())) {
                    String email = eEmail.getText().toString().trim();
                    String pass = ePass.getText().toString().trim();
                    login(email, pass);
                }else{
                    Toast.makeText(LogInActivity.this, "Text can't be empty", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btCreateID:
                startActivity(new Intent(LogInActivity.this, CreateUserActivity.class));
                finish();
                break;
        }
    }

    private void login(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LogInActivity.this, PostListActivity.class));
//                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Wrong user name or password ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
