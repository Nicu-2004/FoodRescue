package com.example.foodrescue;
import java.util.List;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpirareReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper db = new DBHelper(context);

        List<String> produseAproapeExpirate = db.getIngredienteAproapeExpirate();

        if (produseAproapeExpirate.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        for (String numeProdus : produseAproapeExpirate) {
            sb.append("• ").append(numeProdus).append(" (expiră azi sau mâine)\n");
        }

        Intent notificationIntent = new Intent(context, ProduseExpirateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "expirare_channel")
                .setSmallIcon(R.drawable.ic_expired)  // Asigură-te că ai iconul ic_expired în drawable
                .setContentTitle("Atenție! Produse expiră azi")
                .setContentText("Vezi lista completă")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sb.toString()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1001, builder.build());
        }
    }
}

