import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Aventura> citireJSON ()
    {
        Map<Integer, Aventura> rezultat = new HashMap<>();

        try( var fisier = new FileInputStream("Date/aventuri.json"))
        {
            var tokener = new JSONTokener(fisier);
            var JSONAventuri = new JSONArray(tokener);

            for(int i=0;i< JSONAventuri.length();i++)
            {
                var JSONAventura = JSONAventuri.getJSONObject(i);
                rezultat.put(JSONAventura.getInt("cod_aventura"),
                        new Aventura(JSONAventura.getInt("cod_aventura"),
                                JSONAventura.getString("denumire"),
                                JSONAventura.getDouble("tarif"),
                                JSONAventura.getInt("locuri_disponibile")));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static List<Rezervare> citireTXT()
    {
        List<Rezervare> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("Date/rezervari.txt")))
        {
            rezultat= fisier.lines()
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

        Map<Integer, Aventura> aventuriJSON = citireJSON();
        aventuriJSON.entrySet().stream().forEach(aventura -> System.out.println(aventura.getValue().toString()));
        System.out.println();

        List<Rezervare> rezervariTXT = citireTXT();
        rezervariTXT.stream().forEach(rezervar -> System.out.println(rezervar.toString()));
        System.out.println();


        System.out.println("\n---- CERINTA 1 ----\n");
        aventuriJSON.entrySet().stream()
                .forEach(aventura ->
                {
                    if(aventura.getValue().getLocuri_disponibile()>20)
                        System.out.println(aventura.getValue().toString());
                });
        System.out.println("\n---- CERINTA 2 ----\n");
        aventuriJSON.entrySet().stream()
                .forEach(aventura ->{

                    int sumaRezervari = rezervariTXT.stream().filter(rezervare -> rezervare.getCod_aventura()==aventura.getValue().getCod_aventura()).collect(Collectors.toList()).size();

                    if(aventura.getValue().getLocuri_disponibile()-sumaRezervari >=5 )
                        System.out.println(aventura.getValue().toString());

                });

        System.out.println("\n---- CERINTA 3 ----\n");


        try(BufferedWriter writer = new BufferedWriter(new FileWriter("raport.txt")))
        {
            writer.write(String.format("%30s %30s %30s\n","Denumire", "Numar Locuri rezervate", "Valoare venit"));
            aventuriJSON.entrySet().stream()
                    .sorted((a1,a2)-> a1.getValue().getDenumire().compareTo(a2.getValue().getDenumire()))
                    .forEach(aventura ->
                    {
                        int total_locuri_rezervate = rezervariTXT.stream()
                                .filter(rezervare -> rezervare.getCod_aventura() == aventura.getValue().getCod_aventura())
                                .collect(Collectors.summingInt(Rezervare::getNr_locuri_rezervate));
                        double total_venit_aventura = total_locuri_rezervate*aventura.getValue().getTarif();

                        try{
                            writer.write(String.format("%30s",aventura.getValue().getDenumire()));
                            writer.write(String.format("%30s",total_locuri_rezervate));
                            writer.write(String.format("%30s\n",total_venit_aventura));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}