import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static List<Persoana> getPersoane(){

        List<Persoana> persoane = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:Date/bursa.db")){

            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM persoane");
            ResultSet rs = statement.getResultSet();

            while(rs.next()){

                persoane.add(new Persoana(rs.getInt(1), rs.getString(2), rs.getString(3)));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persoane;

    }

    public static List<Tranzactie> getTranzactii(){
        List<Tranzactie> tranzactii = new ArrayList<>();

        try(var fisier = new BufferedReader(new InputStreamReader(new FileInputStream("Date/bursa_tranzactii.txt")))){

            tranzactii = fisier.lines().map(Tranzactie::new).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tranzactii;
    }

    public static void main(String[] args){

        List<Persoana> persoane = getPersoane();
        List<Tranzactie> tranzactii = getTranzactii();


        System.out.println("{{{ --- Cerinta 1) --- }}}");

        System.out.println("Numar total de nerezidenti: " +
                persoane.stream().filter(x -> x.getCnp().substring(0, 1).equalsIgnoreCase("8") ||
                        x.getCnp().substring(0,1).equalsIgnoreCase("9")).collect(Collectors.toList()).size());


        System.out.println("\n{{{ --- Cerinta 2) --- }}}");

        tranzactii.stream().collect(Collectors.groupingBy(Tranzactie::getSimbol)).forEach((x, y) ->{
            System.out.println(x + " : " + y.size());
        });

        System.out.println("\n{{{ --- Cerinta 3) --- }}}");

        try(var fisier = new FileWriter("Date/simboluri.txt")){

            tranzactii.stream().collect(Collectors.groupingBy(Tranzactie::getSimbol)).forEach((x, y) ->{
                if(y.size() > 0 ) {

                    try {
                        fisier.write(x.toString() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fisierul \"simboluri.txt\" a fost creat cu succes!");


        System.out.println("\n{{{ --- Cerinta 4) --- }}}");

        Map<String, List<Tranzactie>> tranzactiiPeNume = new HashMap<>();

        persoane.forEach(x -> {

            tranzactiiPeNume.putIfAbsent(x.getNume(), new ArrayList<Tranzactie>());

            tranzactii.stream().filter(y -> y.getCod() == x.getCod()).forEach(z -> {
                tranzactiiPeNume.get(x.getNume()).add(z);
            });

        });

        Map<String, Double> profitTotalPersoana = new HashMap<>();


        tranzactiiPeNume.forEach((x, y) -> {

            System.out.println(x);

            profitTotalPersoana.putIfAbsent(x, 0.0);

            y.stream().collect(Collectors.groupingBy(Tranzactie::getSimbol)).forEach((a, b) ->{

                System.out.print("  " + a + ": ");

                double profit = 0;

                for(var tranzactie : b){
                    profit += tranzactie.getCantitate() * tranzactie.getPret() * tranzactie.getTip().getSemn();
                }
                System.out.println(profit);

                profitTotalPersoana.put(x, Double.sum(profitTotalPersoana.get(x), profit));

            });

        });


        //Cerinta 5) suplimentar by me:
        //Sa se implementeze un Server TCP/IP multiclient care are urmatoriul scenariu:
        // clientul trimite un nume de persoane, iar server-ul raspunde cu profitul / pierderea totala (pentru toate actiunile cu toate simbolurile)


        final int PORT = 8080;

        try(ServerSocket server = new ServerSocket(PORT)){

            System.out.println("Server-ul asteapta clienti ...");

            while(true){
                Socket socket = server.accept();

                System.out.println("Client conectat!");

                new Thread(() -> {
                    try {
                        proceseazaCerere(socket, profitTotalPersoana);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static int nrCerere = 0;
    static void proceseazaCerere(Socket socket,  Map<String, Double> profitTotalPersoana) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String nume = in.readLine();

        out.println(String.valueOf(profitTotalPersoana.get(nume)));
        System.out.println("Cererea " + ++nrCerere + " a fost procesata si raspunsul a fost trimit catre client!");

    }
}
