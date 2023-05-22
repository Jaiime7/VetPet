package com.insitutosanjuandelacruz.vetpet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.insitutosanjuandelacruz.vetpet.R;

public class MainActivity extends AppCompatActivity {

    public Button buttonLogin;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLogin = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setPaintFlags(textViewRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterEmailActivity.class);
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

                   /* User person = realm.where(PersonRegister.class)
                            .equalTo("email", email)
                            .equalTo("password", password)
                            .findFirst();
                    if (person != null) {
                        Intent intent = new Intent(MainActivity.this, PetActivity.class);
                        intent.putExtra("email", email);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    }
                    */
                }
            }
        });
    }
}
