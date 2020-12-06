import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordPlayServer {

    static ArrayList<PlayerHandler> playerList = new ArrayList<>();
    static int clientId = 0;
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9095);
        System.out.println("[SERVER] SERVER STARTED ON PORT 9095. WAITING FOR CLIENT...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            GameEngine gameEngine = new GameEngine();
            PlayerHandler playerThread = new PlayerHandler(++clientId, clientSocket, gameEngine, playerList);
            System.out.println("[SERVER] Client " + clientId + " connected");

            playerList.add(playerThread);
            pool.execute(playerThread);
        }


    }
}
