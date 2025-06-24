package com.example.foodrescue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Retete {

    public static class Reteta {
        private String nume;
        private List<String> ingrediente;

        public Reteta(String nume, List<String> ingrediente) {
            this.nume = nume;
            this.ingrediente = ingrediente;
        }

        public String getNume() {
            return nume;
        }

        public List<String> getIngrediente() {
            return ingrediente;
        }
    }

    private static final List<Reteta> RETETE = new ArrayList<>(Arrays.asList(
            new Reteta("Salată Caesar", Arrays.asList("salată", "pui", "parmezan", "crutoane")),
            new Reteta("Burger", Arrays.asList("chiftea", "chiflă", "salată", "roșii", "ceapă")),
            new Reteta("Piept de pui cu orez", Arrays.asList("pui", "orez", "ardei")),
            new Reteta("Paste cu sos de roșii", Arrays.asList("paste", "roșii", "usturoi")),
            new Reteta("Omletă cu legume", Arrays.asList("ouă", "ardei", "ceapă", "roșii")),
            new Reteta(
                    "Sandviș cu avocado Pasează un avocado bine copt cu o linguriță de zeamă de lămâie. Prăjește două felii de pâine. Unge pâinea cu avocado, adaugă felii de roșii și stropește cu ulei de măsline. Condimentează cu sare și piper după gust.",
                    Arrays.asList("pâine", "avocado", "roșii", "lămâie", "ulei de măsline")
            ),
            new Reteta(
                    "Tocăniță de cartofi Curăță și taie 4-5 cartofi cubulețe. Toacă o ceapă și un morcov și călește-le în puțin ulei. Adaugă cartofii, apă cât să acopere totul și fierbe 25-30 minute. Potrivește de sare și piper. Presară pătrunjel proaspăt la final.",
                    Arrays.asList("cartofi", "ceapă", "morcovi", "pătrunjel", "ulei")
            ),
            new Reteta(
                    "Omletă cu brânză Bate 2 ouă într-un bol cu un praf de sare. Adaugă 50g de brânză rasă și amestecă. Toarnă compoziția într-o tigaie încinsă cu puțin ulei. Gătește 2-3 minute pe fiecare parte.",
                    Arrays.asList("ouă", "brânză", "ulei", "sare")
            ),
            new Reteta(
                    "Salată de ton Scurge o conservă de ton și pune conținutul într-un bol. Adaugă salată verde tocată, roșii cherry și porumb. Stropește cu puțin ulei de măsline și zeamă de lămâie. Amestecă bine și servește.",
                    Arrays.asList("ton", "salată verde", "roșii cherry", "porumb", "lămâie", "ulei de măsline")
            ),
            new Reteta(
                    "Cartofi copți cu rozmarin Spală și taie 4-5 cartofi în sferturi. Pune-i într-o tavă, stropește cu ulei de măsline, presară sare, piper și rozmarin. Coace în cuptor la 200°C timp de 40 de minute, până devin aurii și crocanți.",
                    Arrays.asList("cartofi", "ulei de măsline", "rozmarin", "sare", "piper")
            )




            ));


    public static List<Reteta> gasesteRetete(List<String> ingredienteDisponibile) {
        return gasesteRetete(ingredienteDisponibile, 0.5);
    }


    public static List<Reteta> gasesteRetete(List<String> ingredienteDisponibile, double prag) {
        List<Reteta> rezultate = new ArrayList<>();
        for (Reteta reteta : RETETE) {
            int totalIngrediente = reteta.getIngrediente().size();
            int ingredienteGasite = 0;
            for (String ing : reteta.getIngrediente()) {
                if (ingredienteDisponibile.contains(ing)) {
                    ingredienteGasite++;
                }
            }
            double raport = (double) ingredienteGasite / totalIngrediente;
            if (raport >= prag) {
                rezultate.add(reteta);
            }
        }
        return rezultate;
    }
    public static List<Reteta> gasesteReteteComplete(List<String> ingredienteDisponibile) {
        List<Reteta> rezultate = new ArrayList<>();
        for (Reteta reteta : RETETE) {
            if (ingredienteDisponibile.containsAll(reteta.getIngrediente())) {
                rezultate.add(reteta);
            }
        }
        return rezultate;
    }
}
