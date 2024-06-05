import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer,Specializare> citireBD()
    {
        Map<Integer, Specializare> rezultat = new HashMap<>();

        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:date/facultate.db")) {

            Statement query = conexiune.createStatement();
            query.execute("SELECT * FROM specializari;");

            ResultSet resultSet = query.getResultSet();

            while(resultSet.next())
            {
                rezultat.put(resultSet.getInt(1),
                        new Specializare(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static List<Inscriere> citireTXT()
    {
        List<Inscriere> rezultat = new ArrayList<>();

        try(var fiser = new BufferedReader(new FileReader("date/inscrieri.txt")))
        {

            rezultat=fiser.lines()
                    .map(linie -> new Inscriere(Long.parseLong(linie.split(",")[0]),
                            linie.split(",")[1],
                            Double.parseDouble(linie.split(",")[2]),
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

        Map<Integer, Specializare> specializariBD = citireBD();
        specializariBD.entrySet().stream().forEach(specializare -> System.out.println(specializare.getValue().toString()));
        System.out.println();

        List<Inscriere> inscriereTXT = citireTXT();
        inscriereTXT.stream().forEach(inscriere -> System.out.println(inscriere.toString()));
        System.out.println();

        System.out.println("\n---CERINTA 1---\n");
        System.out.println("-VAR 1-");
        System.out.println("Numarul de locuri disponibile: " + specializariBD.values().stream().mapToInt(Specializare::getNrLocuri).sum());

        int nr_total_locuri=0;
        for(var entry : specializariBD.entrySet())
        {
            nr_total_locuri+=entry.getValue().getNrLocuri();
        }
        System.out.println("-VAR 2-");
        System.out.println("Numarul de locuri disponibile: " + nr_total_locuri);

        System.out.println("\n---CERINTA 2---\n");
        specializariBD.entrySet().stream()
                .forEach(specializare -> {
                    int nr_locuri_ocupate=inscriereTXT.stream()
                            .filter(inscriere -> inscriere.getCod_specializare()==specializare.getValue().getCod())
                            .collect(Collectors.toList()).size();
                    if(specializare.getValue().getNrLocuri()-nr_locuri_ocupate>=10)
                        System.out.println(specializare.getValue().getCod() + " " + specializare.getValue().getDenumire() + " "
                        + specializare.getValue().getNrLocuri());
                });

        System.out.println("\n---CERINTA 3---\n");

        try(var fisier = new PrintWriter("inscriere_specializare.json"))
        {
            JSONArray arraySpecializari = new JSONArray();

            specializariBD.entrySet().stream()
                    .forEach(specializare -> {
                        int nrInscrieri = inscriereTXT.stream().filter(candidat ->
                                candidat.getCod_specializare()==specializare.getValue().getCod())
                                .collect(Collectors.toList()).size();
                        double medie = inscriereTXT.stream().filter(candidat->
                                candidat.getCod_specializare()==specializare.getValue().getCod())
                                .collect(Collectors.averagingDouble(Inscriere::getNotaBAC));

                        Map<String, Object> obj = new LinkedHashMap<>();
                        obj.put("cod_specializare", specializare.getValue().getCod());
                        obj.put("denumire", specializare.getValue().getDenumire());
                        obj.put("numar_inscrieri", nrInscrieri);
                        obj.put("medie",medie);

                        JSONObject jsonObject = new JSONObject(obj);
                        arraySpecializari.put(jsonObject);
                    });
            fisier.write(arraySpecializari.toString());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Fisierul JSON a fost creat cu succes!");

        System.out.println("AFISARE DESCRESCATOR MAP NR LOCURI");
        specializariBD.entrySet().stream().map(s->s.getValue())
                .sorted((s1,s2) -> Integer.compare(s2.getNrLocuri(),s1.getNrLocuri()))
                .forEach(specializare -> System.out.println(specializare.toString()));

        System.out.println("\nAFISARE CRESCATOR LIST NOTA BAC");
        inscriereTXT.stream().sorted((i1,i2) -> Double.compare(i1.getNotaBAC(),i2.getNotaBAC()))
                .forEach(inscriere -> System.out.println(inscriere.toString()));

        System.out.println("\nAFISARE CRESCATOR LIST NUME 1");
        List<Inscriere> sortedInscriere = inscriereTXT.stream().sorted(Comparator.comparing(Inscriere::getNume)).collect(Collectors.toList());
        sortedInscriere.stream().forEach(inscriere -> System.out.println(inscriere.toString()));

        System.out.println("\nAFISARE CRESCATOR LIST NUME 2");
        Collections.sort(inscriereTXT,(i1,i2) -> i1.getNume().compareTo(i2.getNume()));
        inscriereTXT.stream().forEach(inscriere -> System.out.println(inscriere.toString()));


    }
}