import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Scanner;

public class Client_TCP {

    public static void main(String[] args){

        final int PORT = 8080;
        final String serverName = "127.0.0.1";

        try(Socket socket = new Socket(serverName, PORT)){

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Client conectat cu succes!");

            System.out.print("Cod sectie (se va trimite catre server): ");
            String codSectie = new Scanner(System.in).nextLine();

            out.println(codSectie);

            System.out.println("[RASPUNS SERVER]: " + in.readLine());


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
