import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static List<Candidat> citireTXT (String caleFisier) {
        List<Candidat> rezultat = new ArrayList<>();

        try (var fisier = new BufferedReader(new FileReader(caleFisier))) {

            rezultat = fisier.lines()
                    .map(linie -> new Candidat(Long.parseLong(linie.split(",")[0]),
                            linie.split(",")[1],
                            Double.parseDouble(linie.split(",")[2]),
                            Integer.parseInt(linie.split(",")[3])
                            )).collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rezultat;
    }

    //citireBD

    public static Map<Integer, Specializare> citireBD (){
        Map<Integer, Specializare> rezultat = new HashMap<>();

        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:date/facultate.db")) {

            Statement query = conexiune.createStatement();
            query.execute("SELECT * FROM specializari;");

            ResultSet resultSet = query.getResultSet();

            while(resultSet.next())
            {
                rezultat.put(
                        resultSet.getInt(1),
                        new Specializare(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3))
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } ;


        return rezultat;
    }

    public static Map<Integer, Raport> citireJOSN()
    {
        Map<Integer, Raport> rezultat = new HashMap<>();

        try(var fisier = new FileInputStream("inscrieri_specializare.json"))
        {
            var tokener = new JSONTokener(fisier);
            var JSONRaport = new JSONArray(tokener);

            for(int index=0;index<JSONRaport.length();index++)
            {
                var jsonElement = JSONRaport.getJSONObject(index);

                rezultat.put(jsonElement.getInt("cod_specializare"),
                        new Raport(jsonElement.getInt("cod_specializare"),
                        jsonElement.getString("denumire"),
                        jsonElement.getInt("numar_inscrieri"),
                        jsonElement.getDouble("medie")));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return rezultat;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println("\nDATE TXT:\n");
        List<Candidat> candidatiTXT = citireTXT("date/inscrieri.txt");
        candidatiTXT.stream().forEach(candidat -> System.out.println(candidat.toString()));

        System.out.println("\nDATE BD:\n");
        Map<Integer, Specializare> specializareBD = citireBD();
        specializareBD.entrySet().stream()
                .forEach(specializare -> System.out.println(specializare.getValue().toString()));

        System.out.println("\n----CERINTA 1----\n");
        int nrTotalLocuriFcaultate = specializareBD.values().stream().mapToInt(Specializare::getNrLocuri).sum();

        int nrLocuriLibere=0;
        for (var entry : specializareBD.entrySet())
        {
            nrLocuriLibere+=entry.getValue().getNrLocuri();
        }

        System.out.println("Numarul total de locuri disponibile este " + nrLocuriLibere + ".");

        System.out.println("\n----CERINTA 2----\n");
        specializareBD.values()
                .forEach(specializare ->
                {
                    int nrLocuriOcupate = candidatiTXT.stream()
                        .filter(cand-> cand.getCodSpecializare() == specializare.getCod())
                        .collect(Collectors.toList()).size();
                    if(specializare.getNrLocuri()-nrLocuriOcupate>=10)
                        System.out.println(specializare.getCod() + " " + specializare.getDenumire() + " " + specializare.getNrLocuri());
                });

        System.out.println("\n----CERINTA 3----\n");

        try( var fisier = new PrintWriter("inscrieri_specializare.json"))
        {

            JSONArray arraySpecializari = new JSONArray();
            specializareBD.values()
                    .forEach(specializare -> {
                        int nrInscrieri = candidatiTXT.stream()
                                .filter(cand -> cand.getCodSpecializare()== specializare.getCod())
                                .collect(Collectors.toList()).size();
                        double medieBAC = candidatiTXT.stream()
                                .filter(cand -> cand.getCodSpecializare()==specializare.getCod())
                                .collect(Collectors.averagingDouble(Candidat::getNotaBAC));
                        Map<String, Object> obj = new LinkedHashMap<>();
                        obj.put("cod_specializare",specializare.getCod());
                        obj.put("denumire",specializare.getDenumire());
                        obj.put("numar_inscrieri", nrInscrieri);
                        obj.put("medie", medieBAC);

                        JSONObject jsonObject = new JSONObject(obj);
                        arraySpecializari.put(jsonObject);

                    });

            fisier.write(arraySpecializari.toString(2));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Fisierul 'inscrieri_specializari.json' a fost creat!");

        Map<Integer,Raport> afisareJSON = citireJOSN();

        afisareJSON.entrySet().stream().forEach(element -> System.out.println(element.getValue().toString()));

    }
}