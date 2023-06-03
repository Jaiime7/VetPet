package com.insitutosanjuandelacruz.vetpet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.controller.ValidateEmailPassword;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editTextTextEmailResetPass;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editTextTextEmailResetPass = findViewById(R.id.editTextTextEmailResetPass);
        imageButton = findViewById(R.id.imageButtonNext3);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateEmailPassword val = new ValidateEmailPassword();
                if (editTextTextEmailResetPass.getText().length() != 0 && val.validateEmail(editTextTextEmailResetPass.getText().toString())) {
                    mAuth.sendPasswordResetEmail(editTextTextEmailResetPass.getText().toString()).addOnCompleteListener(task -> {
                        try {
                            if (task.isSuccessful()) {
                                // Password reset email sent successfully
                                Log.d("TAG", "Password reset email sent to " + editTextTextEmailResetPass.getText().toString());
                                Toast.makeText(ForgotPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                mAuth.signOut();
                            } else {
                                // Error occurred while sending the password reset email
                                Exception exception = task.getException();
                                Log.e("TAG", "Error sending password reset email", exception);
                                Toast.makeText(ForgotPasswordActivity.this, "Error sending password reset email", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // Capture any exception and log it
                            Log.e("TAG", "Exception occurred", e);
                            Toast.makeText(ForgotPasswordActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}