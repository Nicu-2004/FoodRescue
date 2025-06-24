package com.example.foodrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "produse.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE produse (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nume TEXT," +
                "dataExpirare TEXT," +
                "notes TEXT," +
                "cantitate INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS produse");
        onCreate(db);
    }

    public boolean adaugaProdus(String nume, String dataExpirare, String notes, int cantitate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nume", nume);
        cv.put("dataExpirare", dataExpirare);
        cv.put("notes", notes);
        cv.put("cantitate", cantitate);
        long result = db.insert("produse", null, cv);
        return result != -1;
    }

    public boolean stergeProdus(String nume) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete("produse", "nume = ?", new String[]{nume});
        return deleted > 0;
    }

    public boolean actualizeazaCantitate(String nume, int nouaCantitate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cantitate", nouaCantitate);
        int rows = db.update("produse", values, "nume = ?", new String[]{nume});
        return rows > 0;
    }

    public List<Produs> getToateProdusele() {
        List<Produs> produse = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nume, dataExpirare, notes, cantitate FROM produse", null);

        if (cursor.moveToFirst()) {
            do {
                String nume = cursor.getString(0);
                String expirare = cursor.getString(1);
                String note = cursor.getString(2);
                int cantitate = cursor.getInt(3);
                produse.add(new Produs(nume, expirare, note, cantitate));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return produse;
    }

    public List<String> getToateIngredientele() {
        List<String> ingrediente = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT nume FROM produse WHERE cantitate > 0 ORDER BY nume ASC",
                null);

        if (cursor.moveToFirst()) {
            do {
                ingrediente.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingrediente;
    }

    public List<String> getIngredienteAproapeExpirate() {
        List<String> ingrediente = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        String azi = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR, 1);
        String maine = sdf.format(cal.getTime());

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT nume FROM produse WHERE cantitate > 0 AND dataExpirare <= ? ORDER BY nume ASC",
                new String[]{maine});

        if (cursor.moveToFirst()) {
            do {
                ingrediente.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingrediente;
    }

    public Cursor getAllProduse() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM produse", null);
    }
    public Cursor getProduseAproapeExpirate(String dataLimita) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM produse WHERE dataExpirare <= ? AND cantitate > 0 ORDER BY dataExpirare ASC", new String[]{dataLimita});
    }

}

