package com.example.foodrescue;
import android.os.Handler;
import android.content.pm.PackageManager;

import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        seteazaNotificareZilnica();

        Button b0 = findViewById(R.id.btn0); // Exit
        Button b1 = findViewById(R.id.btn1); // Adaugare
        Button b2 = findViewById(R.id.btn2); // Stergere
        Button b3 = findViewById(R.id.btn3); // Lista produse
        Button b4 = findViewById(R.id.btn4); // Produse expirate
        Button b5 = findViewById(R.id.btn5); // Retete

        b0.setOnClickListener(v -> finishAffinity());
        b1.setOnClickListener(v -> startActivity(new Intent(this, AdaugareProdusActivity.class)));
        b2.setOnClickListener(v -> startActivity(new Intent(this, StergereProdusActivity.class)));
        b3.setOnClickListener(v -> startActivity(new Intent(this, ListaProduseActivity.class)));
        b4.setOnClickListener(v -> startActivity(new Intent(this, ProduseExpirateActivity.class)));
        b5.setOnClickListener(v -> startActivity(new Intent(this, ReteteActivity.class)));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "expirare_channel",
                    "Produse aproape expirate",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifică atunci când produsele se apropie de expirare.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void seteazaNotificareZilnica() {
        Intent intent = new Intent(this, ExpirareReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }
}
