package com.example.foodrescue;
public class Produs {
    private String nume;
    private String dataExpirare;
    private String note;
    private int cantitate;

    public Produs(String nume, String dataExpirare, String note, int cantitate) {
        this.nume = nume;
        this.dataExpirare = dataExpirare;
        this.note = note;
        this.cantitate = cantitate;
    }


    public String getNume() { return nume; }
    public String getDataExpirare() { return dataExpirare; }
    public String getNote() { return note; }
    public int getCantitate() { return cantitate; }
}
