import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Sectie> citireJSON(String caleFisier){
        Map<Integer, Sectie> rezultat = new HashMap<>();

        try(var fisier = new FileInputStream(caleFisier))
        {
            var tokener = new JSONTokener(fisier);
            var JsonSectii= new JSONArray(tokener);

            for(int index=0;index< JsonSectii.length();index++)
            {
                var jsonSectie = JsonSectii.getJSONObject(index);

                rezultat.put(jsonSectie.getInt("cod_sectie"),
                        new Sectie(jsonSectie.getInt("cod_sectie"),
                                jsonSectie.getString("denumire"),
                                jsonSectie.getInt("numar_locuri")));
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static List<Pacient> citiretext(String caleFisier)
    {
        List<Pacient> rezultat = new ArrayList<>();
        try(var fisier = new BufferedReader(new FileReader(caleFisier)))
        {
            rezultat = fisier.lines()
                    .map(linie -> new Pacient(Long.parseLong(linie.split(",")[0]),
                            linie.split(",")[1],
                            Integer.parseInt(linie.split(",")[2]),
                            Integer.parseInt(linie.split(",")[3])
                            )).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static void main(String[] args) {

        System.out.println("Hello world!");

        System.out.println("------CERINTA 1------");

        Map<Integer, Sectie> sectiiJSON = citireJSON("date/sectii.json");
        List<Pacient> pacientiTXT = citiretext("date/pacienti.txt");

        for(Map.Entry<Integer, Sectie> entry : sectiiJSON.entrySet())
        {
            Sectie sectie = entry.getValue();
            if(sectie.getNrLocuri()>5)
                System.out.println(sectie.toString());
        }

        System.out.println();
        System.out.println("------CERINTA 2------");
        System.out.printf("%20s %30s %20s %20s\n","Cod Sectie", "Denumire Sectie", "Numar locuri","Varsta Medie");

        Map<Integer, List<Pacient>> pacientiPeSectie = pacientiTXT.stream()
                .collect(Collectors.groupingBy(Pacient::getCodSectie));

        Map<Integer, PeSectie> peSectii = new HashMap<>();
        for(var entry : pacientiPeSectie.entrySet())
        {
            double medie = 0.0;
            for(var pacient : entry.getValue())
            {
                medie+=pacient.getVarsta();
            }
            medie= medie/entry.getValue().size();
            peSectii.put(entry.getKey(),new PeSectie(entry.getKey(), medie));
        }

        Map<Integer, Sectie> finalSectii = sectiiJSON;
        peSectii.entrySet().stream()
                .map(entry -> entry.getValue())
                .sorted((s1,s2) -> Double.compare(s2.getVarstaMedie(),s1.getVarstaMedie()))
                .forEach(s ->
                {
                    System.out.printf("%20s",s.getCodSectie());
                    finalSectii.entrySet().stream()
                            .filter(sectie -> sectie.getKey() == s.getCodSectie())
                            .forEach(sectie -> {
                                System.out.printf("%30s", sectie.getValue().getDenumire());
                                System.out.printf("%20s",sectie.getValue().getNrLocuri());
                            });
                    System.out.println(s.toString());
                });

        System.out.println("------CERINTA 3------");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("raport.txt")))
        {
            writer.write(String.format("%20s %30s %20s\n","Cod Sectie","Nume Sectie", "Numar Pacienti"));
            sectiiJSON.entrySet().stream()
                    .map(entry -> entry.getValue())
                    .forEach(sectie -> {
                        try
                        {
                            writer.write(String.format("%20d", sectie.getCod_sectie()));
                            writer.write(String.format("%30s",sectie.getDenumire()));

                            pacientiPeSectie.entrySet().stream()
                                    .filter(entry -> entry.getKey()== sectie.getCod_sectie())
                                    .forEach(s -> {
                                        try{
                                            writer.write(String.format("%20d\n", s.getValue().size()));
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    });
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}