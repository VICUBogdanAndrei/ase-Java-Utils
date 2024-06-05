import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Main {

    public static List<Aventura> getAventuri(){
        List<Aventura> aventuri = new ArrayList<>();

        try(var fisier = new BufferedReader(new InputStreamReader(new FileInputStream("Date/aventuri.json")))){

            JSONArray arr = new JSONArray(new JSONTokener(fisier));

            aventuri = StreamSupport.stream(arr.spliterator(), false).map(item -> (JSONObject)item)
                    .map(item -> new Aventura(
                            item.getInt("cod_aventura"),
                            item.getString("denumire"),
                            item.getDouble("tarif"),
                            item.getInt("locuri_disponibile")
                    )).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return aventuri;
    }

    public static List<Rezervare> getRezervari(){
        List<Rezervare> rezervari = new ArrayList<>();

        try(var fisier = new BufferedReader(new InputStreamReader(new FileInputStream("Date/rezervari.txt")))){

            rezervari = fisier.lines().map(Rezervare::new).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rezervari;
    }
    public static void main(String[] args){

        List<Aventura> aventuri = getAventuri();
        List<Rezervare> rezervari = getRezervari();

        System.out.println("{{{ --- Cerinta 1) --- }}}");
        System.out.println("Aventuri cu locuri disponibile >= 20: ");
        aventuri.stream().filter(x -> x.getNrLocuriDisponibile() >= 20).forEach(System.out::println);

        System.out.println("\n{{{ --- Cerinta 2) --- }}}");

        aventuri.forEach(aventura -> {

            int locuriSolicitate = rezervari.stream().filter(rezervare -> rezervare.getCodAventura() == aventura.getCod())
                    .mapToInt(Rezervare::getNrLocuriCerute).sum();

            if(aventura.getNrLocuriDisponibile() - locuriSolicitate >= 5)
                System.out.println(aventura);

        });

        Map<String, Map<Aventura, Integer>> aventuraLocuriRezervate = new HashMap<>();

        System.out.println("\n{{{ --- Cerinta 3) --- }}}");

        String delimiter = ",";
        try(var fisier = new PrintWriter("venituri.txt")){

            aventuri.forEach(aventura -> {

                int nrLocuriRezervate = rezervari.stream().filter(rezervare -> rezervare.getCodAventura()
                        == aventura.getCod()).mapToInt(Rezervare::getNrLocuriCerute).sum();
                double tarif = aventura.getTarif();

                Map<Aventura, Integer> mapAV = new HashMap<>();
                mapAV.put(aventura, nrLocuriRezervate);

                aventuraLocuriRezervate.putIfAbsent(aventura.getDenumire(), mapAV);

                fisier.write(aventura.getDenumire()); fisier.write(delimiter);
                fisier.write(String.valueOf(nrLocuriRezervate)); fisier.write(delimiter);
                fisier.write(String.valueOf(nrLocuriRezervate * tarif)); fisier.write("\n");

            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Fisierul \"venituri.txt\" a fost creat cu succes!");



        System.out.println("\n{{{ --- Cerinta 4) --- }}}");

        final int PORT = 8080;

        try(ServerSocket server = new ServerSocket(PORT)){

            System.out.println("Server-ul asteapta clienti ...");

            while(true){

                Socket socket = server.accept();
                System.out.println("Client nou conectat!");

                new Thread(() -> {
                    try {
                        procesareCerere(socket, aventuraLocuriRezervate);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void procesareCerere(Socket socket, Map<String, Map<Aventura, Integer>> aventuraLocuriRezervate) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while(true){
            String numeAventura = in.readLine();

            int locuriRezervate = aventuraLocuriRezervate.get(numeAventura).values().stream().findFirst().get();
            Aventura a = aventuraLocuriRezervate.get(numeAventura).keySet().stream().findFirst().get();

            out.println(a.getNrLocuriDisponibile() - locuriRezervate);

            System.out.println("Nume aventura primit: " + numeAventura);
            System.out.println("Cererea a fost procesata si a fost trimisa catre client!");
        }



    }
}
