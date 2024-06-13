package com.example.a06_10_klientu_registracija;

public class Klientas {
    String vardas;
    String pavarde;
    String miestas;
    String telNumeris;

    public Klientas(String vardas, String pavarde, String miestas, String telNumeris) {
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.miestas = miestas;
        this.telNumeris = telNumeris;
    }

    public String getVardas() {return vardas;}
    public String getPavarde() {return pavarde;}
    public String getMiestas() {return miestas;}
    public String getTelNumeris() {return telNumeris;}
}
