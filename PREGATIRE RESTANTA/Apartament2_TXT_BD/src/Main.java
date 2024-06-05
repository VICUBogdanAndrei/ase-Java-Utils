import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.AnnotatedParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static List<Factura> citireTXT ()
    {
        List<Factura> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("data/intretinere_facturi.txt")))
        {
            rezultat = fisier.lines()
                    .map(linie -> new Factura(linie.split(",")[0],
                            linie.split(",")[1],
                            Double.parseDouble(linie.split(",")[2])))
                    .collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static Map<Integer, Apartament> citireBD ()
    {
        Map<Integer, Apartament> rezultat =  new HashMap<>();

        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:intretinere.db"))
        {
            Statement query = conexiune.createStatement();
            query.execute("SELECT * FROM Apartamente;");

            ResultSet resultSet = query.getResultSet();

            while(resultSet.next())
            {
                rezultat.put(resultSet.getInt(1),
                        new Apartament(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3),
                                resultSet.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        List<Factura> facturiTXT = citireTXT();
        facturiTXT.stream().forEach(factura -> System.out.println(factura.toString()));

        System.out.println();

        Map<Integer, Apartament> apartamenteBD = citireBD();
        apartamenteBD.entrySet().stream().forEach(apartament -> System.out.println(apartament.getValue().toString()));

        System.out.println();




    }
}