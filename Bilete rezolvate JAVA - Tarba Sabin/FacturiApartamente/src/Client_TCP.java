import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Scanner;

public class Client_TCP {

    public static void main(String[] args){

        final int PORT = 8080;
        final String serverName = "127.0.0.1";

        try(Socket socket = new Socket(serverName, PORT)){

            System.out.println("Client conectat la server!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Introduceti numele apartamentului (se va duce catre server cererea): ");
            String nrApartament = new Scanner(System.in).nextLine();
            out.println(nrApartament);


            String json = in.readLine();
            JSONObject obj = new JSONObject(json);

            System.out.println("[RASPUNS SERVER]:");
            System.out.println("suprafata: " + obj.get("persoane"));
            System.out.println("persoane: " + obj.get("suprafata"));


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
