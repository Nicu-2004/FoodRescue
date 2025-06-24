package com.example.foodrescue;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProduseExpirateActivity extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produse_expirate);

        db = new DBHelper(this);

        Button btnAziMaine = findViewById(R.id.btnAziMaine);
        Button btnSaptamana = findViewById(R.id.btnSaptamana);
        Button btnLuna = findViewById(R.id.btnLuna);
        Button btnExit = findViewById(R.id.btnExit);

        btnAziMaine.setOnClickListener(v -> afiseazaProduse(getDataLimitaZile(1), "Expiră azi sau mâine"));
        btnSaptamana.setOnClickListener(v -> afiseazaProduse(getDataLimitaZile(7), "Expiră săptămâna asta"));
        btnLuna.setOnClickListener(v -> afiseazaProduse(getDataLimitaZile(30), "Expiră luna asta"));

        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void afiseazaProduse(String dataLimita, String titlu) {
        Cursor c = db.getProduseAproapeExpirate(dataLimita);
        if (c == null || !c.moveToFirst()) {
            showDialog(titlu, "Nu există produse care expiră în acest interval.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        do {
            sb.append("• ").append(c.getString(1))  // nume
                    .append(" — expiră pe ").append(c.getString(2))  // dataExpirare
                    .append("\n");
        } while (c.moveToNext());

        showDialog(titlu, sb.toString());
    }

    private void showDialog(String titlu, String mesaj) {
        new AlertDialog.Builder(this)
                .setTitle(titlu)
                .setMessage(mesaj)
                .setPositiveButton("OK", null)
                .show();
    }

    private String getDataLimitaZile(int zile) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, zile);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }
}

