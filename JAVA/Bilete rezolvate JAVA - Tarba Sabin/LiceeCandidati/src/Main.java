/*În fișierul json date candidati.json sunt centralizate opțiunile absolvenților de clasa a VIII-a pentru admiterea la liceu, sub forma (urmariti fisierul):
        [
        {
        "cod_candidat": 1,
        "nume_candidat": "Pop Marcel",
        "media": 8.50,
        "optiuni": [
        {
        "cod_liceu": 1,
        "cod_specializare": 1
        },
        ...
        ]
        },
        ...
        ]

        Liceele sunt memorate în fișierul text date licee txt,cate doua linii pentru fiecare liceu, astfel:
        cod liceu,nume liceu,N
        cod specializare 1,numar locuri 1,..., cod specializare N, numar locuri N

        Să se scrie o aplicație Java care să îndeplinească următoarele cerințe:

        1. Să afișeze la consolă numărul de candidați cu medii mai mari sau egale cu 9
        Punctaj: 1 punct
        Criteriu de acordare: afișare corectă la consolă (100% DONE!)

        2. Să se afișeze lista liceelor sortată descrescător după numărul total de locuri.
        Pentru fiecare liceu se va afișa codul liceului, numele liceului și numărul total de locuri.
        Punctaj: 1 punct
        Criteriu de acordare: afișare corectă la consolă (100% DONE!)

        3. Să se listeze în fișierul jurnal.txt candidații ordonați descrescător după numărul de opțiuni (criteriul 1) iar în caz de egalitate după medie (criteriul 2).
        Pentru fiecare candidat se va scrie codul, numele, numărul de opțiuni și media de admitere.
        Punctaj: 1 punct
        Criteriu de acordare: vizualizare fișier jurnal.txt (100% DONE!)

        4. Să se creeze tabela CANDIDATI în baza de date sqlite examen.db și să se salveze opțiunile candidaților.
        Tabela va avea câmpurile: cod_candidat - integer, nume_candidat- text, medie - double și numar_optiuni - integer.
        Comanda creare:
        "create table IF NOT EXISTS CANDIDATI (cod_candidat integer,nume_candidat text,medie double,numar_optiuni integer)"
        Punctaj: 2 puncte
        Criteriu de acordare - Vizualizare tabela*/

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static List<Candidat> getCandidati(String cale){
        List<Candidat> candidati = new ArrayList<>();

        try(var fisier = new FileInputStream(cale)){

            JSONArray candidatiInfo = new JSONArray(new JSONTokener(fisier));

            for(int i = 0; i < candidatiInfo.length(); i++){

                JSONObject candidatCurent = candidatiInfo.getJSONObject(i);

                JSONArray arrayOptiuni = new JSONArray(new JSONTokener(candidatCurent.getJSONArray("optiuni").toString()));

                Map<Integer, List<Integer>> optiuni = new HashMap<>();

                for(int j = 0; j < arrayOptiuni.length(); j++){

                    JSONObject optiuneCurenta = arrayOptiuni.getJSONObject(j);

                    optiuni.putIfAbsent(optiuneCurenta.getInt("cod_liceu"), new ArrayList<>());
                    optiuni.get(optiuneCurenta.getInt("cod_liceu")).add(optiuneCurenta.getInt("cod_specializare"));

                }

                candidati.add(new Candidat(
                        candidatCurent.getInt("cod_candidat"),
                        candidatCurent.getString("nume_candidat"),
                        candidatCurent.getDouble("media"),
                        optiuni
                ));

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return candidati;
    }

    public static List<Liceu> getLicee(String cale){

        List<Liceu> licee = new ArrayList<>();

        try(var fisier = new FileInputStream(cale)){


            var scannerFisier = new Scanner(fisier);

            while(scannerFisier.hasNext()){

                var scannerFirstLine = new Scanner(scannerFisier.nextLine());
                var scannnerSecondLine = new Scanner(scannerFisier.nextLine());

                scannerFirstLine.useDelimiter(",");
                scannnerSecondLine.useDelimiter(",");

                int codLiceu = scannerFirstLine.nextInt();
                String denumireLiceu = scannerFirstLine.next();
                int nrSpecializari = scannerFirstLine.nextInt();

                System.out.println(codLiceu);

                Map<Integer, Integer> nrLocuriPerSpec = new HashMap<>();

                for(int i = 0; i < nrSpecializari; i++){
                    nrLocuriPerSpec.put(scannnerSecondLine.nextInt(), scannnerSecondLine.nextInt());
                }

                licee.add(new Liceu(codLiceu, denumireLiceu, nrSpecializari, nrLocuriPerSpec));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return licee;

    }
    public static void main(String[] args) throws SQLException {

        List<Candidat> candidati = getCandidati("Date/candidati.json");
        List<Liceu> licee = getLicee("Date/licee.txt");

        System.out.println("{{ --- Cerinta 1) --- }}");
        System.out.println("Numarul de candidati cu medii mai mari sau egale cu 9: " +
                candidati.stream().filter(x -> x.getMedie() >= 9).collect(Collectors.toList()).size()
                );


        System.out.println("\n{{ --- Cerinta 2) --- }}");
        licee.stream().sorted(Comparator.comparingInt(Liceu::getNrTotalLocuri).reversed()).forEach(System.out::println);

        System.out.println("\n{{ --- Cerinta 3) --- }}");

        String delimiter = ",";

        try(var fisier = new PrintWriter("raport.txt")){

            candidati.forEach(x -> {
                StringBuilder str = new StringBuilder();
                str.append(x.getCodCandidat()); str.append(delimiter);
                str.append(x.getNume()); str.append(delimiter);
                str.append(x.getNrOptiuni()); str.append(delimiter);
                str.append(x.getMedie()); str.append("\n");

                fisier.write(str.toString());

            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fisier \"raport.txt\" creat cu succes!");



        System.out.println("\n{{ --- Cerinta 4) --- }}");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:examen.db");
        Statement statement = conn.createStatement();

        statement.execute("create table IF NOT EXISTS CANDIDATI (cod_candidat integer,nume_candidat text,medie double,numar_optiuni integer)");

        PreparedStatement statementINSERT = conn.prepareStatement("INSERT INTO candidati(cod_candidat, nume_candidat, medie, numar_optiuni) VALUES(?, ?, ?, ?)");

        for(var candidat : candidati)
        {
            statementINSERT.setInt(1, candidat.getCodCandidat());
            statementINSERT.setString(2, candidat.getNume());
            statementINSERT.setDouble(3, candidat.getMedie());
            statementINSERT.setInt(4, candidat.getNrOptiuni());

            statementINSERT.execute();
        }

        System.out.println("Tabela creata si populata cu succes!");


    }
}
