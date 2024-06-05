import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Specializare> citireBD()
    {
        Map<Integer, Specializare> rezultat =  new HashMap<>();

        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:date/facultate.db"))
        {
            Statement query = conexiune.createStatement();
            query.execute("SELECT* FROM specializari;");

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

    private static Map<Integer, Specializare> citireJSON()
    {
        Map<Integer, Specializare> rezultat = new HashMap<>();

        try(var fiser = new FileInputStream("date/specializari.json"))
        {

            JSONTokener tokener = new JSONTokener(fiser);
            JSONArray JSONSpecializari = new JSONArray(tokener);

            for(int i=0;i<JSONSpecializari.length();i++)
            {
                var JSONspecializare = JSONSpecializari.getJSONObject(i);
                rezultat.put(JSONspecializare.getInt("cod"),
                        new Specializare(JSONspecializare.getInt("cod"),
                                JSONspecializare.getString("denumire"),
                                JSONspecializare.getInt("locuri")));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static List<Inscriere> citireTXT()
    {
        List<Inscriere> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("date/inscrieri.txt")))
        {
            rezultat=fisier.lines()
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

        System.out.println("\n--CITIRE BD--\n");
        Map<Integer, Specializare> specializareBD = citireBD();
        specializareBD.values().forEach(specializare -> System.out.println(specializare.toString()));

        System.out.println("\n--CITIRE JSON--\n");
        Map<Integer, Specializare>specializareJSON= citireJSON();
        specializareJSON.values().forEach(specializare -> System.out.println(specializare.toString()));

        System.out.println("\n--CITIRE TXT--\n");
        List<Inscriere> inscrieriTXT = citireTXT();
        inscrieriTXT.stream().forEach(inscriere -> System.out.println(inscriere.toString()));

        System.out.println("\n----CERINTA 1----\n");
        int nr_total_locuri_fac=0;
        for(var specializare: specializareBD.entrySet())
        {
            nr_total_locuri_fac+=specializare.getValue().getNr_locuri();
        }
        System.out.println("Numar total de locuri disponibile este: " + nr_total_locuri_fac);

        System.out.println("\n----CERINTA 2----\n");

        Map<Integer,Specializare> aux = specializareJSON;

        aux.values()
                .forEach(specializare -> {
                    int nr_locuri = inscrieriTXT.stream().filter(inscriere ->
                                    inscriere.getCod_specializare()==specializare.getCod())
                            .collect(Collectors.toList()).size();
                    if(specializare.getNr_locuri()-nr_locuri>100) {
                        specializare.setNr_locuri(specializare.getNr_locuri() - nr_locuri);
                        System.out.println(specializare.toString());
                    }

                });
        System.out.println();
        //aux.values().forEach(specializare -> System.out.println(specializare.toString()));

        //afisati in consola media obtinuta la fiecare faculatte si numarul de candidati, ordonati dupa medie
        System.out.println("---ORDONARE DUPA MEDIE---");
        System.out.printf("%20s %25s %10s %20s\n", "Cod Specializare","Denumire", "Medie BAC", "Numar candidati");
        specializareBD.values().stream().sorted(Comparator.comparing(Specializare::getDenumire))
                .forEach(specializare ->
                {
                    int nr_candidati = inscrieriTXT.stream().filter(inscriere ->
                            inscriere.getCod_specializare()==specializare.getCod())
                            .collect(Collectors.toList()).size();
                    double medie = inscrieriTXT.stream().filter(inscriere ->
                            inscriere.getCod_specializare()==specializare.getCod())
                            .collect(Collectors.averagingDouble(Inscriere::getNotaBAC));
                    System.out.printf("%20d", specializare.getCod());
                    System.out.printf("%25s",specializare.getDenumire());
                    System.out.printf("%10.2f",medie);
                    System.out.printf("%20d\n",nr_candidati);
                });

    }
}