import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Integer, Persoana> citireBD()
    {
        Map<Integer, Persoana> rezultat = new HashMap<>();

        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:date/bursa.db"))
        {
            Statement query = conexiune.createStatement();
            query.execute("SELECT * FROM Persoane;");

            ResultSet resultSet = query.getResultSet();

            while(resultSet.next())
            {
                rezultat.put(resultSet.getInt(1),
                        new Persoana(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3)));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static List<Tranzactie> citireTXT ()
    {
        List<Tranzactie> rezultat = new ArrayList<>();
        try(var fisier = new BufferedReader(new FileReader("date/bursa_tranzactii.txt")))
        {
            rezultat = fisier.lines()
                    .map(linie -> new Tranzactie(Integer.parseInt(linie.split(",")[0]),
                            linie.split(",")[1],
                            linie.split(",")[2],
                            Integer.parseInt(linie.split(",")[3]),
                            Float.parseFloat(linie.split(",")[4]))).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        Map<Integer, Persoana> persoaneBD = citireBD();

        persoaneBD.entrySet().stream().forEach(persoana -> System.out.println(persoana.getValue().toString()));

        System.out.println();

        List<Tranzactie> tranzactiiTXT=citireTXT();

        tranzactiiTXT.stream().forEach(tranzactie -> System.out.println(tranzactie.toString()));

        System.out.println("\n----Cerinta 1----\n");

        System.out.println("Numarul total de nerezidenti: " +
        persoaneBD.entrySet().stream()
                .filter(persoana -> persoana.getValue().getCnp().substring(0,1).equalsIgnoreCase("8")
                        || persoana.getValue().getCnp().substring(0,1).equalsIgnoreCase("9")).collect(Collectors.toList()).size()
        );

        System.out.println("\n----Cerinta 2----\n");
        tranzactiiTXT.stream().collect(Collectors.groupingBy(Tranzactie::getSimbol))
                .forEach((simbol, sizeSimbol) -> {
                    System.out.println(simbol + " : " + sizeSimbol.size());
                });



    }
}