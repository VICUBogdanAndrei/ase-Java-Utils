import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Aventura> citireJSON (String caleFisier)
    {
        Map<Integer, Aventura> rezultat = new HashMap<>();

        try(var fisier = new FileInputStream(caleFisier))
        {
            var tokener = new JSONTokener(fisier);
            var JSONAventuri = new JSONArray(tokener);

            for(int index=0;index<JSONAventuri.length();index++)
            {
                var jsonAventuri = JSONAventuri.getJSONObject(index);
                rezultat.put(jsonAventuri.getInt("cod_aventura"),
                        new Aventura(jsonAventuri.getInt("cod_aventura"),
                                jsonAventuri.getString("denumire"),
                                jsonAventuri.getDouble("tarif"),
                                jsonAventuri.getInt("locuri_disponibile")));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return rezultat;
    }

    private static List<Rezervare> citireTXT ()
    {
        List<Rezervare> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("date/rezervari.txt")))
        {

            rezultat = fisier.lines()
                    .map(linie -> new Rezervare(Integer.parseInt(linie.split(",")[0]),
                            Integer.parseInt(linie.split(",")[1]),
                            Integer.parseInt(linie.split(",")[2]))).collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static void main(String[] args) {

        System.out.println("Hello world!");


        Map<Integer,Aventura> aventuriJSON = citireJSON("date/aventuri.json");

        aventuriJSON.entrySet().stream().forEach(aventura-> System.out.println(aventura.getValue().toString()));

        System.out.println();

        List<Rezervare> rezervariTXT = citireTXT();

        rezervariTXT.stream().forEach(rezervare -> System.out.println(rezervare.toString()));
        System.out.println();

        System.out.println("\n----CERINTA 1----\n");

        aventuriJSON.entrySet().stream()
                .forEach(aventura -> {
                    if(aventura.getValue().getLocuri_disponibile()>=20)
                        System.out.println(aventura.getValue().toString());

                });

        System.out.println("\n----CERINTA 2----\n");

        aventuriJSON.entrySet().stream()
                .forEach(aventura -> {
                    int total_locuri_rezervate = rezervariTXT.stream()
                            .filter(rezervare -> rezervare.getCod_aventura()==aventura.getValue().getCod_aventura())
                            .collect(Collectors.summingInt(Rezervare::getNumar_locuri_rezervare));
                    if(aventura.getValue().getLocuri_disponibile()-total_locuri_rezervate>5)
                        System.out.println(aventura.getValue().toString() + "       ---- Locuri ramase: " + (aventura.getValue().getLocuri_disponibile()-total_locuri_rezervate));
                });


        try(BufferedWriter writer = new BufferedWriter(new FileWriter("venituri.txt")))
        {

            writer.write(String.format("%30s %30s %30s\n", "Denumire aventura", "Numar Locuri Rezervare", "Valoare venit"));
            aventuriJSON.entrySet().stream()
                    .forEach(aventura -> {
                        int total_locuri_rezervate = rezervariTXT.stream()
                                .filter(rezervare -> rezervare.getCod_aventura() == aventura.getValue().getCod_aventura())
                                .collect(Collectors.summingInt(Rezervare::getNumar_locuri_rezervare));
                        double total_venit_aventura = total_locuri_rezervate * aventura.getValue().getTarif();
                        try {
                            writer.write(String.format("%30s", aventura.getValue().getDenumire()));
                            writer.write(String.format("%30s", total_locuri_rezervate));
                            writer.write(String.format("%30s\n", total_venit_aventura));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}