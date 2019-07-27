package com.example.marketingmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    EditText email,password;
    Button login;
    String emailTxt,passwordTxt;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView bottom;
    Boolean loginMode = true;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        login = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        bottom=findViewById(R.id.textView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void Login() {
        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();

        if(!isEmailValid(emailTxt)){
            email.setError("Invalid Email Entered");
            email.requestFocus();
            return;
        }
        if(passwordTxt.isEmpty() || passwordTxt.length()<6){
            password.setError("Invalid Password, Length must be at least 6");
            password.requestFocus();
            return;
        }

        if(loginMode){
            //user wants to login
            mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Login In Failed, Try Again",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"Sign Up Failed, Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                }
            });
        }
    }

    public void ChangeMode(View view) {
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
    }
}
