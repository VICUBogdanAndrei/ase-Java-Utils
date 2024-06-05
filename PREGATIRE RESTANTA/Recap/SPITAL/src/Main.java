import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Sectie> citireJSON()
    {
        Map<Integer, Sectie> rezultat = new HashMap<>();

        try(var fisier = new FileInputStream("data/sectii.json"))
        {
            var tokener = new JSONTokener(fisier);
            var JSONSectii = new JSONArray(tokener);

            for(int index=0;index<JSONSectii.length();index++) {
                var JSONSectie = JSONSectii.getJSONObject(index);
                rezultat.put(JSONSectie.getInt("cod_sectie"),
                        new Sectie(JSONSectie.getInt("cod_sectie"),
                                JSONSectie.getString("denumire"),
                                JSONSectie.getInt("numar_locuri")));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }


    public static List<Pacient> citireTXT()
    {
        List<Pacient>rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("data/pacienti.txt")))
        {
            rezultat = fisier.lines()
                    .map(linie -> new Pacient(Long.parseLong(linie.split(",")[0]),
                            linie.split(",")[1],
                            Integer.parseInt(linie.split(",")[2]),
                            Integer.parseInt(linie.split(",")[3]))).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        Map<Integer, Sectie> sectiiJSON = citireJSON();
        sectiiJSON.entrySet().stream().forEach(sectie-> System.out.println(sectie.getValue().toString()));

        System.out.println();

        List<Pacient> pacientiTXT = citireTXT();
        pacientiTXT.stream().forEach(pacient -> System.out.println(pacient.toString()));

        System.out.println();

        System.out.println("\n----CERINTA 1----\n");
        sectiiJSON.entrySet().stream().forEach(sectie -> {
            if(sectie.getValue().getNr_locuri()>5)
                System.out.println(sectie.getValue().toString());
        });

        System.out.println("\n----CERINTA 2----\n");
        System.out.printf("%20s %30s %20s %20s\n","Cod Sectie", "Denumire Sectie", "Numar locuri","Varsta Medie");


        Map<Integer, PacinetPeSectie> peSectie = new HashMap<>();

        Map<Integer, List<Pacient>> pacientipeSectii = pacientiTXT.stream().
                collect(Collectors.groupingBy(Pacient::getCod_sectie));

        for(var entry: pacientipeSectii.entrySet())
        {
            double medie = 0.0;
            for(var pacienti: entry.getValue())
            {
                medie+= pacienti.getVarsta();
            }
            medie/=entry.getValue().size();

            peSectie.put(entry.getKey(),new PacinetPeSectie(entry.getKey(), medie));
        }

        Map<Integer, Sectie> finalSectii = sectiiJSON;
        peSectie.entrySet().stream()
                .map(entry -> entry.getValue())
                .sorted((s1,s2) -> Double.compare(s2.getMedie_varsta(),s1.getMedie_varsta()))
                .forEach(s ->
                {
                    System.out.printf("%20s", s.getCod_sectie());
                    finalSectii.entrySet().stream()
                            .filter(sectie -> sectie.getKey()==s.getCod_sectie())
                            .forEach(sectie ->{
                                System.out.printf("%31s",sectie.getValue().getDenumire());
                                System.out.printf("%21s", sectie.getValue().getNr_locuri());
                            });
                    System.out.println(s.toString());
                });



    }
}
