import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static List<Factura> citireTXT()
    {
        List<Factura> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("date/intretinere_apartamente.txt")))
        {
            rezultat = fisier.lines()
                    .map(linie->new Factura(linie.split(",")[0],
                           linie.split(",")[1],
                            Double.parseDouble(linie.split(",")[2]))).collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rezultat;
    }

    private static Map<Integer, Apartament> citireJSON ()
    {
        Map<Integer, Apartament> rezultat = new HashMap<>();
        try(var fisier = new FileInputStream("intretinere_facturi.json"))
        {
            var tokener = new JSONTokener(fisier);
            var JSONApartamente = new JSONArray(tokener);

            for (int i=0;i<JSONApartamente.length();i++)
            {
                var JSONApartamanet = JSONApartamente.getJSONObject(i);
                rezultat.put(JSONApartamanet.getInt(""))
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        System.out.println("Hello world!");
        List<Factura> facturiTXT = citireTXT();
        facturiTXT.stream().forEach(factura -> System.out.println(factura.toString()));


    }
}