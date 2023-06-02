package com.insitutosanjuandelacruz.vetpet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.insitutosanjuandelacruz.vetpet.R;

public class LoginActivity extends AppCompatActivity {

    public Button buttonLogin;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public TextView textViewRegister;
    CheckBox checkBoxAutoLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        checkBoxAutoLogin = findViewById(R.id.checkBoxAutoLogin);
        textViewRegister.setPaintFlags(textViewRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("autoLogin", false)) {
            editTextEmail.setText(sharedPreferences.getString("email", ""));
            editTextPassword.setText(sharedPreferences.getString("password", ""));
        }
        boolean autoLoginEnabled = sharedPreferences.getBoolean("autoLogin", false);
        checkBoxAutoLogin.setChecked(autoLoginEnabled);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterEmailActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassword.getText().length() == 0 || editTextEmail.getText().length() == 0) {
                    if (editTextEmail.getText().length() == 0) {
                        editTextEmail.setError("Enter email");
                        editTextEmail.requestFocus();
                    }
                    if (editTextPassword.getText().length() == 0) {
                        editTextPassword.setError("Enter password");
                        editTextEmail.requestFocus();
                    }
                } else {
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()){
                        if (checkBoxAutoLogin.isChecked()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("autoLogin", true);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();
                        } else {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("autoLogin", false);
                            editor.putString("email", "");
                            editor.putString("password", "");
                            editor.apply();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "You must confirm your email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
