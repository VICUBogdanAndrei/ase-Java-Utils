import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
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
}
