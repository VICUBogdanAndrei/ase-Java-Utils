import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class mainClient {
    public static void main(String[] args) {
        final int PORT_NUMBER = 8383;
        try (Socket client = new Socket("127.0.0.1", PORT_NUMBER);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
            System.out.println("Am deschis conexiunea cu server-ul!");

            int codSectie = 4;
            out.println(codSectie);
            System.out.println("Am trimis codul de sectie " + codSectie + " catre server ...");

            System.out.print("Numarul de locuri libere pentru sectia " + codSectie + " este ");
            System.out.println(in.readLine());

            System.out.print("Am inchis conexiunea cu serverul!");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.print("Conexiunea cu serverul nu a putut fi realizata. Verificati functionalitatea server-ului!");
        }

    }

}
