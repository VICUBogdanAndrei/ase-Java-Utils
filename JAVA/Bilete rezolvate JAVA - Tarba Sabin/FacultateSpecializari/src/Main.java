import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static List<Specializare> getSpecializari(){

        List<Specializare> specializari = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:Date/facultate.db")){

            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM specializari");

            ResultSet rs = statement.getResultSet();

            while(rs.next()){

            specializari.add(new Specializare(rs.getInt(1), rs.getString(2), rs.getInt(3)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return specializari;
    }

    public static List<Inscriere> getInscrieri(){

        List<Inscriere> inscrieri = new ArrayList<>();

        try(var fisier = new BufferedReader(new InputStreamReader(new FileInputStream("Date/inscrieri.txt")))){

            inscrieri = fisier.lines().map(Inscriere::new).collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inscrieri;
    }


    public static void main(String[] args){

        List<Specializare> specializari = getSpecializari();
        List<Inscriere> inscrieri = getInscrieri();

        System.out.println("{{{ --- Cerinta 1) --- }}}");
        System.out.println("Numarul total de locuri la facultate: " + specializari.stream().mapToInt(Specializare::getNrLocuri).sum());


        System.out.println("\n{{{ --- Cerinta 2) --- }}}");


        specializari.forEach(specializare -> {

            int nrLocuriOcupate = inscrieri.stream().filter(inscriere -> inscriere.getCodSpecializare() == specializare.getCod())
                    .collect(Collectors.toList()).size();

            if(specializare.getNrLocuri() - nrLocuriOcupate >= 10)
                System.out.println(specializare.getCod() + " " + specializare.getDenumire() + " " + (specializare.getNrLocuri() - nrLocuriOcupate));

        });


        System.out.println("\n{{{ --- Cerinta 3) --- }}}");

        try(var fisier = new PrintWriter("inscrieri_specializari.json")){

            JSONArray arraySpecializari = new JSONArray();

            specializari.forEach(specializare -> {

                int nrInscrieri = inscrieri.stream().filter(inscriere -> inscriere.getCodSpecializare() == specializare.getCod())
                        .collect(Collectors.toList()).size();

                double medie = inscrieri.stream().filter(inscriere -> inscriere.getCodSpecializare() == specializare.getCod())
                        .collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Inscriere::getMedie));

                JSONObject obj = new JSONObject();
                obj.put("cod_specializare", specializare.getCod());
                obj.put("denumire", specializare.getDenumire());
                obj.put("numar_inscrieri", nrInscrieri);
                obj.put("medie", medie);

                arraySpecializari.put(obj);

            });

            fisier.write(arraySpecializari.toString(4));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Fisierul \"inscrieri_specializari.json\" a fost creat cu succes!");


        System.out.println("\n{{{ --- Cerinta 4) --- }}}");


        final int PORT = 8080;

        try(ServerSocket server = new ServerSocket(PORT)){

            System.out.println("Astept conexiune de la clienti...");

            while(true){

                Socket socket = server.accept();
                System.out.println("Client conectat!");

                new Thread(() -> {
                    try {
                        proceseazaCerere(socket, specializari, inscrieri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void proceseazaCerere(Socket socket, List<Specializare> specializari, List<Inscriere> inscrieri) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while(true){

            String numeSpecializare = in.readLine();

            Specializare specializare = specializari.stream().filter(x -> x.getDenumire().equalsIgnoreCase(numeSpecializare))
                    .collect(Collectors.toList()).stream().findFirst().get();

            int nrInscrieri = inscrieri.stream().filter(inscriere -> inscriere.getCodSpecializare() == specializare.getCod())
                        .collect(Collectors.toList()).size();

            out.println(specializare.getNrLocuri() - nrInscrieri);

            System.out.println("Cererea a fost procesata, iar raspunsul a fost trimis la client!");

        }


    }

}
