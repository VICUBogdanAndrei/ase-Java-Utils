import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static Map<Integer, Licee> getLicee(String fileName) {
        Map<Integer, Licee> licee = new HashMap<>();

        try(var fisier = new BufferedReader(new FileReader(fileName))) {

            try (var scannerFisier = new Scanner(fisier)) {
                while(scannerFisier.hasNextLine()) {

                    var scannerFirstLine = new Scanner(scannerFisier.nextLine());
                    var scannerSecLine = new Scanner(scannerFisier.nextLine());

                    scannerFirstLine.useDelimiter(",");
                    scannerSecLine.useDelimiter(",");

                    int codLiceu = scannerFirstLine.nextInt();
                    String denumireLiceu = scannerFirstLine.next();
                    int nrSpecializari = scannerFirstLine.nextInt();

                    Map<Integer, Integer> nrLocuriPerSpec = new HashMap<>();
                    for(int i = 0; i<nrSpecializari; i++) {
                        nrLocuriPerSpec.put(scannerSecLine.nextInt(), scannerSecLine.nextInt());
                    }

                    licee.put(codLiceu,new Licee(codLiceu,denumireLiceu,nrSpecializari,nrLocuriPerSpec));
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return licee;
    }

    public static List<Candidati> getCandidati(String fileName) {
        List<Candidati> candidati = new ArrayList<>();

        try(var fisier = new FileInputStream(fileName)) {

            JSONArray candidatInfo = new JSONArray(new JSONTokener(fisier));

            for (int i = 0; i< candidatInfo.length(); i++) {
                JSONObject candidatCurent = candidatInfo.getJSONObject(i);

                JSONArray arrayOptiuni = new JSONArray(new JSONTokener(candidatCurent.getJSONArray("optiuni").toString()));
                Map<Integer, List<Integer>> optiuni = new HashMap<>();

                for (int j = 0; j<arrayOptiuni.length(); j++) {
                    JSONObject optiuneCurenta = arrayOptiuni.getJSONObject(j);

                    optiuni.putIfAbsent(optiuneCurenta.getInt("cod_liceu"), new ArrayList<>());
                    optiuni.get(optiuneCurenta.getInt("cod_liceu")).add(optiuneCurenta.getInt("cod_specializare"));

                }

                candidati.add(new Candidati(
                        candidatCurent.getInt("cod_candidat"),
                        candidatCurent.getString("nume_candidat"),
                        candidatCurent.getDouble("media"),
                        optiuni
                ));

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return candidati;
    }
    public static void main(String[] args) throws SQLException {
        Map<Integer, Licee> licee = getLicee("Date/licee.txt");
        licee.values()
                .forEach(liceu -> System.out.println(liceu.toString()));
        List<Candidati> candidati = getCandidati("Date/candidati.json");
        candidati.stream()
                .forEach(candidat -> System.out.println(candidat.toString()));

        System.out.println("------CERINTA 1------");
        System.out.println("Numarul de candidati cu media mai mare sau egala cu 9 este: " +
                candidati.stream()
                .filter(candidat -> candidat.getMedie()>=9.0)
                .collect(Collectors.toList()).size() +
                " candidati.");


        System.out.println("------CERINTA 2------");
        licee.values()
                .stream().sorted(Comparator.comparingInt(Licee::getNrTotalLocuri).reversed())
                .forEach(liceu -> System.out.println(liceu.cerinta2()));

        System.out.println("------CERINTA 3------");

        try(var fisier = new PrintWriter("Date/jurnal.txt")) {

            candidati.stream().sorted(Comparator.comparingInt(Candidati::getNrOptiuni)
                    .thenComparingDouble(Candidati::getMedie).reversed())
                    .forEach(candidat-> {
                        fisier.println(candidat.getCod() + " " + candidat.getNume() + " " + candidat.getNrOptiuni() + " " + candidat.getMedie());
                    });

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("------CERINTA 4------");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:Date/examen.db");
        Statement statement = conn.createStatement();

        statement.execute("create table IF NOT EXISTS CANDIDATI (cod_candidat integer,nume_candidat text,medie double,numar_optiuni integer)");

        PreparedStatement statementInsert = conn.prepareStatement("INSERT INTO candidati(cod_candidat, nume_candidat, medie, numar_optiuni) VALUES (?,?,?,?)");

        for(var candidat : candidati ) {
            statementInsert.setInt(1,candidat.getCod());
            statementInsert.setString(2, candidat.getNume());
            statementInsert.setDouble(3,candidat.getMedie());
            statementInsert.setInt(4,candidat.getNrOptiuni());

            statementInsert.execute();
        }
        System.out.println("Tabela a fost creata si populata");
    }
}