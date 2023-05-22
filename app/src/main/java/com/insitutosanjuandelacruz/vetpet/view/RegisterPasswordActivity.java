package com.insitutosanjuandelacruz.vetpet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.controller.ValidateEmailPassword;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterPasswordActivity extends AppCompatActivity {

    public EditText editTextPassword1;
    public EditText editTextPassword2;
    public ImageButton imageButtonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        editTextPassword1 = findViewById(R.id.editTextTextPassword1);
        editTextPassword2 = findViewById(R.id.editTextTextPassword2);
        imageButtonNext = findViewById(R.id.imageButtonNext2);
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateEmailPassword val = new ValidateEmailPassword();
                if (editTextPassword1.getText().length() == 0 || editTextPassword2.getText().length() == 0) {
                    Toast.makeText(RegisterPasswordActivity.this, "You must type the password twice", Toast.LENGTH_SHORT).show();
                } else if (!editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString())) {
                    Toast.makeText(RegisterPasswordActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                } else if (!val.validatePassword(editTextPassword1.getText().toString())) {
                    Toast.makeText(RegisterPasswordActivity.this, "The password must meet the following requirements", Toast.LENGTH_SHORT).show();
                } else {
                    String password = editTextPassword1.getText().toString();
                }
            }
        });
    }

    public void writeToDb(String email, String password) {

        String hashedPass = BCrypt.hashpw(password,BCrypt.gensalt());
    }
}