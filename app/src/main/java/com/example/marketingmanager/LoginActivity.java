package com.example.marketingmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {

    EditText email,password;
    Button login;
    String emailTxt,passwordTxt;
    FirebaseAuth mAuth;
    TextView bottom;
    Boolean loginMode = true;
    ProgressBar progressBar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        login = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_circular);
        bottom=findViewById(R.id.textView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Login();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void Login() {
        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();

        if(!isEmailValid(emailTxt)){
            email.setError("Invalid Email Entered");
            progressBar.setVisibility(View.GONE);
            email.requestFocus();
            return;
        }
        if(passwordTxt.isEmpty() || passwordTxt.length()<6){
            password.setError("Invalid Password, Length must be at least 6");
            progressBar.setVisibility(View.GONE);
            password.requestFocus();
            return;
        }

        if(loginMode){
            //user wants to login
            mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Login In Failed, Try Again "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                }
            });

        }
        else{
            //user wants to register
            mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sign Up Failed, Try Again "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                }
            });
        }
    }

    public void ChangeMode(View view) {
        password.setVisibility(View.VISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        if(loginMode){
            loginMode = !loginMode;
            bottom.setText("Registered Already? Login Here.");
            login.setText("Register");
        }
        else{
            loginMode = !loginMode;
            bottom.setText("Not registered? Sign Up here.");
            login.setText("Login");
        }
    }

    public void ResetPass(View view) {
        password.setVisibility(View.GONE);
        login.setText("Reset Password");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();
            }
        });
    }

    private void Reset() {
        progressBar.setVisibility(View.VISIBLE);
        emailTxt = email.getText().toString();
        if(!isEmailValid(emailTxt)){
            email.setError("Invalid Email Entered");
            progressBar.setVisibility(View.GONE);
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(emailTxt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Password sent to Email",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
