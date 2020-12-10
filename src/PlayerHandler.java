import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    }

    private int getClientId() {
        return clientId;
    }

    //setup game.
    private void setup() throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("Hello player " + clientId + ".The game has started. Please enter your first word. ");
        outToAll("Number of players connected:" + playerList.size());

    }

    @Override
    public void run() {
        try {
            setup();
            out.println("Player " + gameEngine.getClientMove() + " turn first.");
            while (true) {
                String clientCommand = in.readLine();
                if (clientCommand.startsWith("move")) {
                    String enteredWord = clientCommand.substring(5);
                    move(enteredWord, clientId);
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


    //method to send messages to all players.
    private void outToAll(String s) {
        for (PlayerHandler client : playerList) {
            client.out.println(s);
        }
    }

    /*
        First check move to see if it's the players turn.
        If list isn't empty, get last character of last word, make sure entered word starts with this last character
        If it does, validate the word, so check if it's a valid word + whether or not it's already been entered.
    */
    //method for the players move
    private void move(String enteredWord, int clientId) throws IOException {
        if (gameEngine.checkMove(clientId)) {
            if (!gameEngine.getWordList().isEmpty()) {
                lastCharacterOfLastWordCheck(enteredWord,clientId);
            } else {
                validateEnteredWord(enteredWord,clientId);
            }
        } else {
            out.println("It's not your move!");
        }
    }

    /*
        lastWordEntered = get the last word which was entered into the list
        lastCharacterOfLastWord = get the last character of the last word which was entered into the list
     */
    //method to return the last character of the last word entered.
    private void lastCharacterOfLastWordCheck(String enteredWord, int clientId) {
        String lastWordEntered = gameEngine.getWordList().get(gameEngine.getWordList().size() -1);
        String lastCharacterOfLastWord = lastWordEntered.substring((lastWordEntered.length() - 1));

        if (enteredWord.startsWith(lastCharacterOfLastWord)) {
            validateEnteredWord(enteredWord,clientId);
        } else {
            //player should lose at this point
            out.println("You entered the word '" + enteredWord + "' which does not being with the last character of the previously entered word! You lose.");
        }
    }

    /*
        First check if the word exists or not  in the english dictionary
        Check if word is in the list or not, if yes , add to list, if not player turn ends and they lose.
     */
    //method to check whether a word is a valid english word and whether or not it's already been entered.
    private void validateEnteredWord(String enteredWord, int clientId) {
        if (gameEngine.doesItExist(enteredWord)) {
            if (!(gameEngine.getWordList().contains(enteredWord))) {
                gameEngine.addWord(enteredWord);
                outToAll("Player " + clientId + " entered the word:" + enteredWord);
            } else {
                //player should lose at this point
                outToAll("Player " + clientId + " entered the word '" + enteredWord +  "'which has already been entered. They lose!");
            }
        } else {
            //player should lose at this point
            outToAll("Player " + clientId + " entered the word '" + enteredWord +"' which does not exist. They lose!");
        }
    }
}
