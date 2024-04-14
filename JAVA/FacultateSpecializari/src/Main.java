import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static java.util.Collections.sort;

public class Main {

    public static Map<Integer,Specializari> getSpecializari() {
        Map<Integer, Specializari> specializari = new HashMap<>();

        //deschidem conexiunea cu baza de date
        try(Connection conexiune = DriverManager.getConnection("jdbc:sqlite:Date/facultate.db")) {

            //Dupa ce am deschis conexiunea, ne cream un obiect Statement prin care transmitem o comanda SQL catre fisierul baza de date
            Statement statement = conexiune.createStatement();
            statement.execute("Select * FROM specializari"); // in variabila statement am memorat comanda dintre paranteze, care va fi executata prin metoda .execute();

            //pentru a accesa rezultatele de vom folosi de o variabila ResultSet, care returneaza setul de valori returnate de statement
            ResultSet rezultat = statement.getResultSet();

            while(rezultat.next()) { /*cat timp exista un camp rezultat in setul de valori rezultate*/
                specializari.put(
                        rezultat.getInt(1),  //codul specializarii drept cheie primara
                        new Specializari(
                                rezultat.getInt(1), //codul specializarii
                                rezultat.getString(2), //numele specializarii
                                rezultat.getInt(3) //numarul de locuri libere al specializarii
                                        )
                                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return specializari;
    }

    public static List<Candidati> getCandidati(String fileName) {
        List<Candidati> candidati = new ArrayList<>();

        try( var fisier = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {

            candidati = fisier.lines().map(linie -> new Candidati(
                        Long.parseLong(linie.split(",")[0]),
                        linie.split(",")[1],
                        Double.parseDouble(linie.split(",")[2]),
                        Integer.parseInt(linie.split(",")[3]))
                    ).collect(Collectors.toList());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return candidati;
    }


    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Specializari> specializariMap = new HashMap<>();
        specializariMap = getSpecializari();
        specializariMap.entrySet().stream()
                .forEach(specializare -> System.out.println(specializare.getValue().toString()));

        List<Candidati> candidatiList = getCandidati("Date/inscrieri.txt");
        candidatiList.stream()
                .forEach(candidat -> System.out.println(candidat.toString()));

        System.out.println("\n------Cerinta 1------");

        int nrTotalLocuriLibere = specializariMap.values().stream().mapToInt(Specializari::getNrLocuri).sum();

        System.out.println("La facultate sunt in total " + nrTotalLocuriLibere + " locuri libere.");

        System.out.println("\n------Cerinta 2------");

        System.out.println("Urmatoarele specializari au cel putin 10 locuri ramase dupa procesarea inscrierilor:");

        //procesam inscrierile: parcurgem lista de candidati si in functie de optiunea lor pentru specializarea aleasa vom modifica
        //nr de locuri ramase pentru fiecare specializare

        Collections.sort(candidatiList);
        Collections.reverse(candidatiList);

        specializariMap.values()
                .forEach(specializare -> {
                    int nrLocuriOcupate = candidatiList.stream()
                            .filter(cand -> cand.getCodSpecializare() == specializare.getCod())
                            .collect(Collectors.toList()).size();
                    //specializare.setNrLocuri(specializare.getNrLocuri()-nrLocuriOcupate);
                    if(specializare.getNrLocuri()-nrLocuriOcupate >= 10)
                        System.out.println(specializare.getCod() + " " + specializare.getDenumire() + " " + specializare.getNrLocuri());

                });

        System.out.println("\n------Cerinta 3------");
        try ( var fisier = new PrintWriter("inscriere_specializari.json")) {

            JSONArray arraySpecializari = new JSONArray();

            specializariMap.values()
                    .forEach(specializare -> {
                        int nrInscrieri = candidatiList.stream().filter(candidati -> candidati.getCodSpecializare() == specializare.getCod())
                                .collect(Collectors.toList()).size(); //aflu numarul de candidati care au aplicat la specializarea cu codul curent
                        double medie = candidatiList.stream().filter(candidati -> candidati.getCodSpecializare() == specializare.getCod())
                                .collect(Collectors.averagingDouble(Candidati::getNotaBac));

                        Map<String,Object> obj = new LinkedHashMap<>(); //initializez un nou obiect de tip JSON
                        obj.put("cod_specializare", specializare.getCod());
                        obj.put("denumire", specializare.getDenumire());
                        obj.put("numar_inscrieri", nrInscrieri);
                        obj.put("medie", medie);
                        //am initializat obiectul cu mai multe campuri, formate dintr-un string KEY si valoarea pentru fiecare

                        JSONObject jsonObject = new JSONObject(obj);

                        arraySpecializari.put(jsonObject); //am introdus obiectul JSON in vectorul de obiecte JSON
                    });

            fisier.write(arraySpecializari.toString(2));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fisierul 'inscriere_specializari.josn' a fost creat!");

        System.out.println("\n------Cerinta 4------");

//        final int PORT_NUMBER = 8383;
//
////        System.out.println("S:Am pornit serverul. Asteptam conexiunea cu clientu...");
//
//        try(ServerSocket server = new ServerSocket(PORT_NUMBER)) {
//
//            System.out.println("S: Am pornit serverul. Asteptam conexiunea cu clientul...");
//            try(Socket client = server.accept();
//                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
//
//                System.out.println("S: Conexiunea cu clinetul a fost realizata!");
//
//                String specializare_dorita = in.readLine();
//                System.out.println("S: Cererea a fost primita! Procesam informatiile pentru specializarea " + specializare_dorita);
//                sleep(2500);
//                specializariMap.values()
//                        .stream().filter(specializare -> specializare.getDenumire().equalsIgnoreCase(specializare_dorita))
//                        .forEach(specializare -> {
//                            int nrLocuriOcupate = candidatiList.stream()
//                                    .filter(cand -> cand.getCodSpecializare() == specializare.getCod())
//                                    .collect(Collectors.toList()).size();
//
//                            out.println("La specializarea "
//                                    + specializare_dorita
//                                    + " mai sunt "
//                                    + (specializare.getNrLocuri()-nrLocuriOcupate)
//                                    + " locuri disponibile.");
//                        });
//                System.out.println("S: Cererea a fost procesata! Serverul se va inchide.");
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("\n------Cerinta 5------");

        Map<Integer, Specializari> finalSpecializariMap = specializariMap;
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int PORT_NUMBER = 8383;

//        System.out.println("S:Am pornit serverul. Asteptam conexiunea cu clientu...");

                try(ServerSocket server = new ServerSocket(PORT_NUMBER)) {

                    System.out.println("S: Am pornit serverul. Asteptam conexiunea cu clientul...");
                    try(Socket client = server.accept();
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

                        System.out.println("S: Conexiunea cu clinetul a fost realizata!");

                        String specializare_dorita = in.readLine();
                        System.out.println("S: Cererea a fost primita! Procesam informatiile pentru specializarea " + specializare_dorita);
                        sleep(2500);
                        finalSpecializariMap.values()
                                .stream().filter(specializare -> specializare.getDenumire().equalsIgnoreCase(specializare_dorita))
                                .forEach(specializare -> {
                                    int nrLocuriOcupate = candidatiList.stream()
                                            .filter(cand -> cand.getCodSpecializare() == specializare.getCod())
                                            .collect(Collectors.toList()).size();

                                    out.println("La specializarea "
                                            + specializare_dorita
                                            + " mai sunt "
                                            + (specializare.getNrLocuri()-nrLocuriOcupate)
                                            + " locuri disponibile.");
                                });
                        System.out.println("S: Cererea a fost procesata! Serverul se va inchide.");

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        serverThread.start();

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int PORT_NUMBER = 8383;

                try (Socket client = new Socket("127.0.0.1",PORT_NUMBER);
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                    System.out.println("C: Am deschis conexiunea cu serverul.");
                    System.out.println("C: Introdu denumirea specializarii dorite: ");

                    Scanner scanner = new Scanner(System.in);
                    String specializare = scanner.nextLine();

                    out.println(specializare);
                    System.out.println("C: Am trimis solicitarea catre server. Asteptam raspuns...");

                    System.out.println("C: Raspuns primit de la server: " + in.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}