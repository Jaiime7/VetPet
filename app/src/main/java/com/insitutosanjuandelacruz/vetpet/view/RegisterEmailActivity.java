package com.insitutosanjuandelacruz.vetpet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.controller.ValidateEmailPassword;

public class RegisterEmailActivity extends AppCompatActivity {

    public EditText editTextEmail;
    public ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress2);
        imageButton = findViewById(R.id.imageButtonNext);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateEmailPassword val = new ValidateEmailPassword();
                if (editTextEmail.getText().length()!=0 && val.validateEmail(editTextEmail.getText().toString())){
                    Intent intent = new Intent(RegisterEmailActivity.this, RegisterPasswordActivity.class);
                    intent.putExtra("email",editTextEmail.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterEmailActivity.this,"Put a valid email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}