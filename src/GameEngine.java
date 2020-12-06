import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GameEngine {

    private static List<String> wordList = new ArrayList<>();
    private Set<String> wordsSet;
    private String[] words;
    private static int clientMove;

    public GameEngine() {
        clientMove = 1;
    }

    public int getClientMove() {
        return clientMove;
    }

    public void addWord(String word) {
        wordList.add(word);
    }

    public List<String> getWordList() {
        return wordList;
    }

    public boolean checkMove(int clientId){
        if (clientId == 1 && clientMove == 1){
            clientMove = 2;
            return true;
        } else if (clientId == 2 && clientMove == 2){
            clientMove = 1;
            return true;
        } else {
            return false;
        }

    }


    public boolean doesItExist(String word) {
        return wordsSet.contains(word);
    }

    public void getWordsFromFile() throws IOException {
        Path path = Paths.get("words.txt");
        byte[] readBytes = Files.readAllBytes(path);
        String wordListContents = new String(readBytes, "UTF-8");
        words = wordListContents.split("\n");
        wordsSet = new HashSet<>();
        Collections.addAll(wordsSet, words);
        wordsSet = wordsSet.stream().map(p -> p.toLowerCase()).collect(Collectors.toSet());
    }
}
