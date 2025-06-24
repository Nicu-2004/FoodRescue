package com.example.foodrescue;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdaugareProdusActivity extends AppCompatActivity {
    EditText etNume, etData, etNotes, etCantitate;
    Button btnAdauga, btnExit;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_produs);

        db = new DBHelper(this);

        etNume = findViewById(R.id.etNume);
        etData = findViewById(R.id.etData);
        etNotes = findViewById(R.id.etNotes);
        etCantitate = findViewById(R.id.etCantitate);
        btnAdauga = findViewById(R.id.btnAdauga);
        btnExit = findViewById(R.id.btnExit);

        etData.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AdaugareProdusActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etData.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        btnAdauga.setOnClickListener(v -> {
            String nume = etNume.getText().toString();
            String data = etData.getText().toString();
            String notes = etNotes.getText().toString();
            String cantStr = etCantitate.getText().toString();

            if (nume.isEmpty() || data.isEmpty() || cantStr.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile obligatorii!", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantitate = Integer.parseInt(cantStr);

            if (db.adaugaProdus(nume, data, notes, cantitate)) {
                Toast.makeText(this, "Produs adăugat!", Toast.LENGTH_SHORT).show();

                etNume.setText("");
                etData.setText("");
                etNotes.setText("");
                etCantitate.setText("");

                etNume.requestFocus();
            } else {
                Toast.makeText(this, "Eroare la adăugare!", Toast.LENGTH_SHORT).show();
            }
        });

        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(AdaugareProdusActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
