package com.example.foodrescue;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ReteteActivity extends AppCompatActivity {

    private TextView tvRetete;
    private Button btnReteteRapide, btnSelectIngrediente, btnExit;
    private boolean[] ingredienteSelectate;
    private String[] toateIngrediente;
    private List<String> ingredienteAproapeExpirate;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retete);

        tvRetete = findViewById(R.id.tvRetete);
        btnReteteRapide = findViewById(R.id.btnReteteRapide);
        btnSelectIngrediente = findViewById(R.id.btnSelectIngrediente);
        btnExit = findViewById(R.id.btnExit);  // Nou

        dbHelper = new DBHelper(this);
        ingredienteAproapeExpirate = dbHelper.getIngredienteAproapeExpirate();
        List<String> listaIngrediente = dbHelper.getToateIngredientele();
        toateIngrediente = listaIngrediente.toArray(new String[0]);
        ingredienteSelectate = new boolean[toateIngrediente.length];

        btnReteteRapide.setOnClickListener(v -> {
            if (ingredienteAproapeExpirate.isEmpty()) {
                tvRetete.setText("Nu există ingrediente aproape expirate.");
                return;
            }
            afiseazaRetetePeBazaIngrediente(ingredienteAproapeExpirate);
        });

        btnSelectIngrediente.setOnClickListener(v -> arataDialogSelectIngrediente());

        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(ReteteActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void afiseazaRetetePeBazaIngrediente(List<String> ingrediente) {
        List<Retete.Reteta> reteteGasite = Retete.gasesteRetete(ingrediente);
        StringBuilder sb = new StringBuilder();
        if (reteteGasite.isEmpty()) {
            sb.append("Nu am găsit rețete care să folosească toate ingredientele: ").append(ingrediente);
        } else {
            sb.append("Rețete găsite pentru ingredientele: ").append(ingrediente).append("\n\n");
            for (Retete.Reteta r : reteteGasite) {
                sb.append("• ").append(r.getNume()).append("\n");
            }
        }
        tvRetete.setText(sb.toString());
    }

    private void arataDialogSelectIngrediente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selectează ingrediente");

        builder.setMultiChoiceItems(toateIngrediente, ingredienteSelectate, (dialog, which, isChecked) -> {
            ingredienteSelectate[which] = isChecked;
        });

        builder.setPositiveButton("Găsește rețete", (dialog, which) -> {
            List<String> ingredienteAlese = new ArrayList<>();
            for (int i = 0; i < toateIngrediente.length; i++) {
                if (ingredienteSelectate[i]) {
                    ingredienteAlese.add(toateIngrediente[i]);
                }
            }
            if (ingredienteAlese.isEmpty()) {
                tvRetete.setText("Nu ai selectat niciun ingredient.");
            } else {
                afiseazaRetetePeBazaIngrediente(ingredienteAlese);
            }
        });

        builder.setNegativeButton("Anulează", null);
        builder.show();
    }
}

