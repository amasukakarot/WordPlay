import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerHandler implements Runnable {

    Socket socket;
    int clientId;
    BufferedReader in;
    PrintWriter out;
    GameEngine gameEngine;
    private ArrayList<PlayerHandler> playerList = new ArrayList<>();


    public PlayerHandler(int clientId, Socket socket, GameEngine gameEngine, ArrayList<PlayerHandler> playerList) throws IOException {
        this.clientId = clientId;
        this.socket = socket;
        this.gameEngine = gameEngine;
        this.playerList = playerList;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    private int getClientId(){
        return clientId;
    }

    private void setup() throws IOException {
        gameEngine.getWordsFromFile();
    }

    @Override
    public void run() {
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //welcome message for client
        out.println("Hello client " + clientId + ".The game has started. Please enter your first word. ");
        outToAll("Number of clients connected:" + playerList.size());
        try {
            while (true) {
                String clientCommand = in.readLine();
                System.out.println("Client " + clientId + " command:" + clientCommand);
                if (clientCommand.startsWith("move")) {
                    String enteredWord = clientCommand.substring(5);
                    checkWord(enteredWord);

                } else if (clientCommand.equalsIgnoreCase("words")) {
                    out.println(gameEngine.getWordList());
                } else {
                    out.println("Incorrect command entered. Please use 'move XXX' or 'words'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outToAll(String s) {
        for (PlayerHandler client : playerList) {
            client.out.println(s);
        }
    }

    private void outToClient(String s, int clientId){
        for (PlayerHandler client: playerList){
            if (client.getClientId() == clientId){
                client.out.println(s);
            }
        }
    }

    private void checkWord(String enteredWord) throws IOException {
        if (gameEngine.doesItExist(enteredWord)) {
            if (!(gameEngine.getWordList().contains(enteredWord))) {
                gameEngine.addWord(enteredWord);
                outToAll("Client " + clientId + " entered the word:" + enteredWord);
            } else {
                outToAll("Client " + clientId + " entered a word which has already been entered. They lose!");
            }
        } else {
            outToAll("Client " + clientId + " entered a word which does not exist. They lose!");
            //tell client 2 that they won, disconnect client one.
        }
    }

}
