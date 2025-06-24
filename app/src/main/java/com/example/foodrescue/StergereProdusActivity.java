package com.example.foodrescue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.view.View;
public class StergereProdusActivity extends AppCompatActivity {
    DBHelper db;
    Spinner spinnerProduse, spinnerCantitate;
    TextView tvCantitateTotala;
    Button btnSterge;

    Map<String, Integer> produseMap = new HashMap<>();
    String produsSelectat = null;
    int cantitateTotala = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stergere_produs);

        db = new DBHelper(this);

        spinnerProduse = findViewById(R.id.spinnerProduse);
        spinnerCantitate = findViewById(R.id.spinnerCantitate);
        tvCantitateTotala = findViewById(R.id.tvCantitateTotala);
        btnSterge = findViewById(R.id.btnSterge);

        // Load produse
        List<Produs> lista = db.getToateProdusele();
        List<String> numeProduse = new ArrayList<>();

        for (Produs p : lista) {
            produseMap.put(p.getNume(), p.getCantitate());
            numeProduse.add(p.getNume());
        }

        ArrayAdapter<String> produseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeProduse);
        produseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduse.setAdapter(produseAdapter);

        spinnerProduse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                produsSelectat = parent.getItemAtPosition(pos).toString();
                cantitateTotala = produseMap.get(produsSelectat);
                tvCantitateTotala.setText("Cantitate: " + cantitateTotala);
                updateCantitateSpinner();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSterge.setOnClickListener(v -> {
            if (produsSelectat == null) return;

            String selectie = spinnerCantitate.getSelectedItem().toString();
            int deScazut = selectie.equals("Toate") ? cantitateTotala : Integer.parseInt(selectie);

            if (deScazut >= cantitateTotala) {
                if (db.stergeProdus(produsSelectat)) {
                    Toast.makeText(this, "Produs șters complet!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Eroare la ștergere!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (db.actualizeazaCantitate(produsSelectat, cantitateTotala - deScazut)) {
                    Toast.makeText(this, "Cantitate actualizată!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Eroare la actualizare!", Toast.LENGTH_SHORT).show();
                }
            }
            recreate();
        });
        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(StergereProdusActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void updateCantitateSpinner() {
        List<String> optiuni = new ArrayList<>();
        for (int i = 1; i < cantitateTotala; i++) {
            optiuni.add(String.valueOf(i));
        }
        if (cantitateTotala > 0)
            optiuni.add("Toate");

        ArrayAdapter<String> cantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optiuni);
        cantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantitate.setAdapter(cantAdapter);
    }

}

