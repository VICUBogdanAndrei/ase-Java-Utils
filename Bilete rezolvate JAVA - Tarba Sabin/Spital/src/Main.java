import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/*În fișierul date\sectii.json se găsesc informații privind secțiile unui spital de urgență.
        Fisierul este structurat astfel:
        [
        {
        "cod_sectie": 1,
        "denumire": "ORL",
        "numar_locuri":10
        },
        ...
        ]
        În fișierul date\pacienti.txt se află informații privind pacienții internați, câte o linie pentru fiecare pacient, astfel:
        cnp_pacient, nume_pacient,varsta,cod_secție
        ...
        Cnp-ul este de tip long, numele este șir de caractere iar codul secției si varsta sunt intregi.

        Să se scrie o aplicație Java care să îndeplinească următoarele cerințe:

        1) Să afișeze la consolă lista secțiilor spitalului cu un număr de locuri strict mai mare decât 10
        Punctaj: 1 punct.
        Criteriu de acordare a punctajului: afișarea corectă la consolă

        2) Să afișeze la consolă lista secțiilor spitalului sortată descrescător după varsta medie a pacientilor internați pe secție.
        Pentru fiecare secție se va afișa codul, denumirea, numărul de locuri și vârsta medie a pacienților.
        Punctaj: 1 punct
        Criteriu de acordare a punctajului: afișarea corectă la consolă

        3) Să se scrie în fișierul text jurnal.txt un raport al internărilor pe secții, de forma:
        cod_secție_1,nume secție_1,numar_pacienti_1
        ...
        Punctaj: 1 punct
        Criteriu de acordare a punctajului: urmărirea fișierului raport.txt

        4) Să implementeze funcționalitățile de server și client TCP/IP și să se execute următorul scenariu:
        componenta client trimite serverului codul unei secții iar componenta server va întoarce clientului numărul de locuri libere.
        Serverul se va opri după servirea unei cereri.

        Punctaj:
        1 punct - afișarea la consolă de către server a codului primit de la client
        1 punct - afișarea la consolă de către client a numărului de locuri libere
        Criteriu de acordare a punctajului: afișarea corectă la consolă*/
public class Main {

    public static List<Sectie> getSectii(){
        List<Sectie> sectii = new ArrayList<>();

        try(var fisier = new FileReader("Date/sectii.json")){

            JSONTokener tokener = new JSONTokener(fisier);
            JSONArray array = new JSONArray(tokener);

            sectii = StreamSupport.stream(array.spliterator(), false).map(item -> (JSONObject)item)
                    .map(item -> new Sectie(
                            item.getInt("cod_sectie"),
                            item.getString("denumire"),
                            item.getInt("numar_locuri"))).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sectii;
    }

    public static List<Pacient> getPacienti() {
        List<Pacient> pacienti = new ArrayList<>();

        try (var fisier = new BufferedReader(new FileReader("Date/pacienti.txt"))) {

            pacienti = fisier.lines().map(Pacient::new).collect(Collectors.toList());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

       return pacienti;
    }




    public static void main(String[] args){

        List<Sectie> sectii = getSectii();
        List<Pacient> pacienti = getPacienti();

        System.out.println("{{ -- Exercitiul 1) -- }}");
        sectii.stream().filter(x -> x.getNrLocuri() > 10).forEach(System.out::println);



        System.out.println("\n{{ -- Exercitiul 2) -- }}");

        Map<Double, Sectie> sectiiVarstaMedie = new HashMap<>(); // media si sectia respectiva

        sectii.forEach(x -> {

            double medie = pacienti.stream().filter(y -> y.getCodSectie() == x.getCodSectie())
                    .collect(Collectors.averagingInt(Pacient::getVarsta));

            sectiiVarstaMedie.put(medie, x);

        });
        sectiiVarstaMedie.keySet().stream().sorted(Comparator.reverseOrder()).forEach(x ->
                        System.out.println(sectiiVarstaMedie.get(x) + " " + x)
                );



        System.out.println("\n{{ -- Exercitiul 3) -- }}");
        Map<Integer, Integer> sectieNrPacienti = new HashMap<>();

        try(var fisier = new FileWriter("raport.txt")){

            sectii.forEach(x -> {

                int nrPacienti = pacienti.stream().filter(y -> y.getCodSectie() == x.getCodSectie()).collect(Collectors.toList()).size();

                sectieNrPacienti.put(x.getCodSectie(), nrPacienti);

                try {
                    fisier.write(String.valueOf(x.getCodSectie()));
                    fisier.write(",");
                    fisier.write(x.getDenumireSectie());
                    fisier.write(",");
                    fisier.write(String.valueOf(nrPacienti));
                    fisier.write("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fisier.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Fisier \"raport.txt\" creat cu succes!\n");


        System.out.println("\n{{ -- Exercitiul 4) -- }}");

        final int PORT = 8080;


        try(ServerSocket server = new ServerSocket(PORT)){
            System.out.println("Asteptam conexiune ...");

            Socket socket = server.accept();

            System.out.println("Client conectat la server!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int codSectie = Integer.parseInt(in.readLine());

            out.println(String.valueOf(sectieNrPacienti.get(codSectie)));

            System.out.println("Server-ul a trimis informatia catre client!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
