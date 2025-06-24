package com.example.foodrescue;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class ListaProduseActivity extends AppCompatActivity {

    DBHelper db;
    Spinner spinnerListaProduse;
    TextView tvDetaliiProdus;
    Button btnAfiseazaDetalii, btnExit;
    ImageView imageProdus;

    Map<String, Produs> produsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produse);

        db = new DBHelper(this);

        spinnerListaProduse = findViewById(R.id.spinnerListaProduse);
        tvDetaliiProdus = findViewById(R.id.tvDetaliiProdus);
        btnAfiseazaDetalii = findViewById(R.id.btnAfiseazaDetalii);
        btnExit = findViewById(R.id.btnExit);
        imageProdus = findViewById(R.id.imageProdus);

        List<Produs> produse = db.getToateProdusele();
        List<String> numeProduse = new ArrayList<>();

        for (Produs p : produse) {
            produsMap.put(p.getNume(), p);
            numeProduse.add(p.getNume());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeProduse);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaProduse.setAdapter(adapter);

        btnAfiseazaDetalii.setOnClickListener(v -> {
            String selected = spinnerListaProduse.getSelectedItem().toString();
            Produs p = produsMap.get(selected);

            if (p != null) {
                String detalii = "Nume: " + p.getNume() + "\n"
                        + "Expiră: " + p.getDataExpirare() + "\n"
                        + "Cantitate: " + p.getCantitate() + "\n"
                        + "Note: " + p.getNote();

                tvDetaliiProdus.setText(detalii);
                tvDetaliiProdus.setVisibility(View.VISIBLE);

                int imgResId = getResources().getIdentifier(p.getNume().toLowerCase(), "drawable", getPackageName());
                if (imgResId != 0) {
                    imageProdus.setImageResource(imgResId);
                    imageProdus.setVisibility(View.VISIBLE);
                } else {
                    imageProdus.setVisibility(View.GONE);
                }
            }
        });

        btnExit.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}


