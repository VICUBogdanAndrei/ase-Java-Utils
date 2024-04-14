import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client_TCP {

    public static void main(String[] args){

        final int PORT = 8080;
        final String serverName = "127.0.0.1";

        try(Socket socket = new Socket(serverName, PORT)){

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("M-am conectat la server!");

            Scanner scanner = new Scanner(System.in);

            while(true){

                System.out.print("Introdu denumirea specializarii: ");
                String specializare = scanner.nextLine();

                out.println(specializare);

                System.out.println("[RASPUNS SERVER -> numar locuri ramase]: " + in.readLine());

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
