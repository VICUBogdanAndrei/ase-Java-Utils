import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


public class mainProces {

    public static Map<Integer, Sectie> citireJSON(String caleFisier) {
        Map<Integer, Sectie> rezultat = new HashMap<>();

        try (var fisier = new FileInputStream(caleFisier)) {
            var tokener = new JSONTokener(fisier);
            var jsonSectii = new JSONArray(tokener);

            for (int index = 0; index < jsonSectii.length(); index++) {
                var jsonSectie = jsonSectii.getJSONObject(index);

                rezultat.put(jsonSectie.getInt("cod_sectie"),
                        new Sectie(jsonSectie.getInt("cod_sectie"),
                                jsonSectie.getString("denumire"),
                                jsonSectie.getInt("numar_locuri")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rezultat;
    }

    public static List<Pacient> citireText(String caleFisier) {
        List<Pacient> rezultat = new ArrayList<>();

        try (var fisier = new BufferedReader(new FileReader(caleFisier))) {
            rezultat = fisier.lines()
                    .map(linie -> new Pacient(Long.parseLong(linie.split(",")[0]),
                            linie.split(",")[1],
                            Integer.parseInt(linie.split(",")[2]),
                            Integer.parseInt(linie.split(",")[3])
                    ))
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rezultat;
    }

    public static void main(String[] args) throws InterruptedException {
        Map<Integer, Sectie> sectii = new HashMap<>();
        List<Pacient> pacienti = new ArrayList<>();

        sectii = citireJSON("date/sectii.json");
        sectii.entrySet().stream()
                .forEach(entry -> System.out.println(entry.getValue().toString()));

        pacienti = citireText("date/pacienti.txt");
        pacienti.stream()
                .forEach(pacient -> System.out.println(pacient.toString()));

        System.out.println("Cerinta 1 ----------------------------------------");
        sectii.entrySet().stream()
                .filter(entry -> entry.getValue().getLocuri() > 5)
                .forEach(entry -> System.out.println(entry.getValue().toString()));

        System.out.println("Cerinta 2 ----------------------------------------");
        // creez o clasa auxiliara pentru a asocia un cod de sectie cu varsta medie a pacientilor de pe sectie
        class PeSectie {
            private int cod;
            private double varstaMedie;

            public PeSectie(int cod, double varstaMedie) {
                this.cod = cod;
                this.varstaMedie = varstaMedie;
            }

            public int getCod() {
                return cod;
            }

            public double getVarstaMedie() {
                return varstaMedie;
            }

            @Override
            public String toString() {
//                return "PeSectie{" +
//                        "cod=" + cod +
//                        ", varstaMedie=" + varstaMedie +
//                        '}';
                return String.format("%20.1f", this.varstaMedie);
            }
        }

        System.out.printf("%20s %30s %20s %20s\n", "Cod sectie", "Denumire sectie", "Număr locuri", "Varsta medie");

        Map<Integer, List<Pacient>> pacientiPeSectie = pacienti.stream()
                .collect(Collectors.groupingBy(Pacient::getCodSectie));

        Map<Integer, PeSectie> peSectii = new HashMap<>();
        for (var entry : pacientiPeSectie.entrySet()) {
            double medie = 0.0;
            for (var pacient : entry.getValue()) {
                medie += pacient.getVarsta();
            }
            medie /= entry.getValue().size();
            peSectii.put(entry.getKey(), new PeSectie(entry.getKey(), medie));
        }

        Map<Integer, Sectie> finalSectii = sectii;
        peSectii.entrySet().stream()
                .map(entry -> entry.getValue())
                .sorted((s1, s2) -> Double.compare(s2.getVarstaMedie(), s1.getVarstaMedie()))
                .forEach(s -> {
                    System.out.printf("%20d", s.getCod());
                    finalSectii.entrySet().stream()
                            .filter(sectie -> sectie.getKey() == s.getCod())
                            .forEach(sectie -> {
                                System.out.printf("%30s", sectie.getValue().getDenumire());
                                System.out.printf("%20d", sectie.getValue().getLocuri());
                            });
                    System.out.println(s.toString());
                });

        System.out.println("Cerinta 3 ----------------------------------------");
        System.out.printf("%20s %30s %20s\n", "Cod sectie", "Denumire sectie", "Număr pacienti");
        sectii.entrySet().stream()
                .map(entry -> entry.getValue())
                .forEach(sectie -> {
                    System.out.printf("%20d", sectie.getCod());
                    System.out.printf("%30s", sectie.getDenumire());
                    pacientiPeSectie.entrySet().stream()
                            .filter(entry -> entry.getKey() == sectie.getCod())
                            .forEach(s -> System.out.printf("%20d\n", s.getValue().size()));
                });
        System.out.println();

        System.out.println("Cerinta 4 ----------------------------------------");
//        final int PORT_NUMBER = 8383;
//
//        try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
//            System.out.println("Asteptam conexiune client ...");
//
//            try (Socket client = server.accept();
//                 PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
//
//                System.out.println("Conexiune client realizata!");
//                String mesajClient = in.readLine();
//                int codSectie = Integer.parseInt(mesajClient);
//
//                System.out.println("Codul sectiei primit de la client este " + codSectie);
//
//                sleep(1000);  // server-ul lucreaza la elaborarea raspunsului pentru client ...
//
//                int nrLocuri = sectii.get(codSectie).getLocuri();
//                int nrPacienti = 0;
//                if (pacientiPeSectie.containsKey(codSectie))
//                    nrPacienti = pacientiPeSectie.get(codSectie).size();
//
//                out.println(nrLocuri - nrPacienti);  // returnam catre client numarul de locuri disponibile pentru sectia solicitata
//                System.out.println("Am trimis numarul de locuri disponibile " + (nrLocuri - nrPacienti));
//
//                System.out.print("Am termina tprocesarea cererii clientului - inchidem conexiunea");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("Cerinta 5 ----------------------------------------");
        Map<Integer, Sectie> finalSectii1 = sectii;
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int PORT_NUMBER = 8383;

                try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
                    System.out.println("Asteptam conexiune client ...");

                    try (Socket client = server.accept();
                         PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

                        System.out.println("Conexiune client realizata!");
                        String mesajClient = in.readLine();
                        int codSectie = Integer.parseInt(mesajClient);

                        System.out.println("Codul sectiei primit de la client este " + codSectie);

                        sleep(1000);  // server-ul lucreaza la elaborarea raspunsului pentru client ...

                        int nrLocuri = finalSectii1.get(codSectie).getLocuri();
                        int nrPacienti = 0;
                        if (pacientiPeSectie.containsKey(codSectie))
                            nrPacienti = pacientiPeSectie.get(codSectie).size();

                        int locurDisponibile = nrLocuri - nrPacienti;
                        out.println(locurDisponibile);  // returnam catre client numarul de locuri disponibile pentru sectia solicitata
                        System.out.println("Am trimis numarul de locuri disponibile " + locurDisponibile);

                        System.out.println("Am termina tprocesarea cererii clientului - inchidem conexiunea");

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // pornim serverul pe un thread distinct
        serverThread.start();

        sleep(500);

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int PORT_NUMBER = 8383;
                try (Socket client = new Socket("127.0.0.1", PORT_NUMBER);
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
                    System.out.println("Am deschis conexiunea cu server-ul!");

                    int codSectie = 4;
                    out.println(codSectie);
                    System.out.println("Am trimis codul de sectie " + codSectie + " catre server ...");

//                    System.out.println(in.readLine());
                    System.out.println("Numarul de locuri libere pentru sectia " + codSectie + " este " + in.readLine());

                    System.out.println("Am inchis conexiunea cu serverul!");

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
//            e.printStackTrace();
                    System.out.print("Conexiunea cu serverul nu a putut fi realizata. Verificati functionalitatea server-ului!");
                }
            }
        });
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}
