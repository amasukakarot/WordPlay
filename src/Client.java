import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket listen = new Socket("127.0.0.1", 9095);
        System.out.println("Connected to server on port 9095");

        PrintWriter out = new PrintWriter(listen.getOutputStream(), true);
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        ServerConnection serverConnection = new ServerConnection(listen);

        new Thread(serverConnection).start();

        while (true) {
            String clientCommand = keyboard.readLine();
            out.println(clientCommand);

        }
    }

}
