package com.insitutosanjuandelacruz.vetpet.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.insitutosanjuandelacruz.vetpet.R;

import java.util.Calendar;

public class RegisterBirthdateActivity extends AppCompatActivity {

    DatePicker datePicker;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_birthdate);
        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        datePicker = findViewById(R.id.datePicker);
        imageButton = findViewById(R.id.imageButtonNextDate);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                if (isUserAdult(day, month, year)) {
                    Intent intent = new Intent(RegisterBirthdateActivity.this, RegisterPasswordActivity.class);
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterBirthdateActivity.this, "You must be at least 18 years old.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUserAdult(int day, int month, int year) {
        Calendar currentDate = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        int age = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.DAY_OF_YEAR) < selectedDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age >= 18;
    }

}