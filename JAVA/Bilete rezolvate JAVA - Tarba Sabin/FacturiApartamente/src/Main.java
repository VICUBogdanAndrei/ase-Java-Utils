/*
Fie datele de intrare (in directorul date):

a) intretinere_apartamente.txt: lista de apartamente (numar apartament - întreg, suprafata - întreg, numar persoane - întreg) - fișier text de forma:
1,100,1
2,50,1
3,75,2
...

b) intretinere_facturi.json: fișier text în format JSON cu următoarea structură:

[
  {
    "denumire": "Apa calda",
    "repartizare": "persoane",
    "valoare": 800
  },
  {
    "denumire": "Caldura",
    "repartizare": "suprafata",
    "valoare": 300
  },
  ...
]

Să se scrie o aplicație Java care să îndeplinească următoarele cerințe:

1) Să se afișeze la consolă numărul apartamentului cu suprafața maximă
Punctaj: 1 punct.
Criteriu de acordare a punctajului: afișarea corectă la consolă

2) Să se afișeze la consolă numărul total de persoane care locuiesc în imobil.
Punctaj: 1 punct.
Criteriu de acordare a punctajului: afișarea corectă la consolă

3) Să se afișeze la consolă valoarea totală a facturilor pe fiecare tip de repartizare.
Punctaj: 1 punct.
Criteriu de acordare a punctajului: afișarea corectă la consolă



4) Să se scrie în fișierul text date\tabel_intretinere.txt tabelul de intreținere în forma:

       Număr apartament, Suprafata, Persoane, Cheltuieli Suprafata, Cheltuieli Persoane, Cheltuieli Apartament, Total de plata
        ...
Tabelul trebuie să fie sortat în funcție de numărul apartamentului.

5) Să implementeze funcționalitățile de server și client TCP/IP și să se execute următorul scenariu:
- componenta client trimite serverului un număr de apartament
- componenta server va întoarce clientului valoarea de plată defalcată pe cele trei tipuri cheltuieli pentru apartamentul respectiv.
Serverul se va opri după servirea unei cereri.

Punctaj:
1 punct - afișarea la consolă de către server a numărului apartamentului
1 punct - afișarea la consolă de către client a celor trei valori calculate
Criteriu de acordare a punctajului: afișarea corectă la consolă
*/


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Main {

    public static List<Facturi> getFacturi(String cale){

        List<Facturi> facturi = new ArrayList<>();

        try(var fisier = new FileReader(cale)){

            JSONArray array = new JSONArray(new JSONTokener(fisier));

            facturi = StreamSupport.stream(array.spliterator(), false).
                    map(item -> (JSONObject)item)
                    .map(item -> new Facturi(
                            item.getString("denumire"),
                            item.getString("repartizare"),
                            item.getInt("valoare")

                    )).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return facturi;
    }

    public static List<Apartament> getApartamente(String cale){
        List<Apartament> apartamente = new ArrayList<>();

        try(var fisier = new BufferedReader(new InputStreamReader(new FileInputStream(cale)))){


            apartamente = fisier.lines().map(Apartament::new).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apartamente;
    }

    public static void main(String[] args){

        List<Facturi> facturi = getFacturi("Date/intretinere_facturi.json");
        List<Apartament> apartamente = getApartamente("Date/intretinere_apartamente.txt");

        System.out.println("{{ --- Cerinta 1) --- }}");

        System.out.println("Apartamentul cu suprafata maxima: " + apartamente.stream().sorted(Comparator.comparingInt(Apartament::getSuprafata).reversed())
                .findFirst().get().getNrApartament());


        System.out.println("\n{{ --- Cerinta 2) --- }}");
        System.out.println("Numarul total de persoane care locuiesc in imobil: " + apartamente.stream()
                .mapToInt(Apartament::getNrPersoane).sum());


        System.out.println("\n{{ --- Cerinta 3) --- }}");
        facturi.stream().collect(Collectors.groupingBy(Facturi::getRepartizare)).forEach((x, y) -> {
            System.out.println("Repartizarea " + x + ": " + y.stream().mapToInt(Facturi::getValoare).sum());
        });

        System.out.println("\n{{ --- Cerinta 4) --- }}");

        Map<String, Integer> plataTotalaPerRepartizare = new HashMap<>(); // denumire_repartizare ; valoare totala facturi pentru
        //repartizarea respectiva

        facturi.stream().collect(Collectors.groupingBy(Facturi::getRepartizare)).forEach((x,y) ->{

            String repartizare = x;
            int valoareTotala = y.stream().mapToInt(Facturi::getValoare).sum();

            plataTotalaPerRepartizare.put(x, valoareTotala);

        });

        String delimiter = ",";

        try(var fisier = new PrintWriter("Date/tabel_intretinere.txt")){

            fisier.write("Număr apartament, Suprafata, Persoane, Cheltuieli Suprafata, Cheltuieli Persoane, Cheltuieli Apartament, Total de plata\n");

            apartamente.stream().sorted(Comparator.comparingInt(Apartament::getNrApartament)).forEach(x ->{

                fisier.write(String.valueOf(x.getNrApartament())); fisier.write(delimiter);
                fisier.write(String.valueOf(x.getSuprafata())); fisier.write(delimiter);
                fisier.write(String.valueOf(x.getNrPersoane())); fisier.write(delimiter);
                fisier.write(String.valueOf(x.getSuprafata() * plataTotalaPerRepartizare.get("suprafata"))); fisier.write(delimiter);
                fisier.write(String.valueOf(x.getNrPersoane() * plataTotalaPerRepartizare.get("persoana"))); fisier.write(delimiter);
                fisier.write(String.valueOf(x.getSuprafata() * plataTotalaPerRepartizare.get("suprafata")
                + x.getNrPersoane() * plataTotalaPerRepartizare.get("persoana"))); fisier.write("\n");

            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("\n{{ --- Cerinta 5) --- }}");


        final int PORT = 8080;

        try(ServerSocket server = new ServerSocket(PORT)){

            System.out.println("Server-ul asteapta clienti...");

            Socket socket = server.accept();

            System.out.println("Client conectat!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int nrApartament = Integer.parseInt(in.readLine());

            System.out.println("Am primit de la client apartamentul: " + nrApartament);

            int nrPersoane = apartamente.stream().filter(x -> x.getNrApartament() == nrApartament)
                    .findFirst().get().getNrPersoane();
            int suprafata = apartamente.stream().filter(x -> x.getNrApartament() == nrApartament)
                    .findFirst().get().getSuprafata();

            double valPersoane = 0;
            double valSuprafata = 0;

            for(var factura : facturi)
            {
                if(factura.getRepartizare().equalsIgnoreCase("persoana"))
                    valPersoane += factura.getValoare();
                else valSuprafata += factura.getValoare();
            }

            valPersoane = valPersoane * nrPersoane;
            valSuprafata = valSuprafata * suprafata;

            JSONObject obj = new JSONObject();
            obj.put("persoane", valPersoane);
            obj.put("suprafata", valSuprafata);

            out.println(obj.toString());

            System.out.println("Am procesat request-ul si am trimis raspunsul catre client!");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
