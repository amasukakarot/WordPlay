import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GameEngine {

    static List<String> wordList = new ArrayList<>();
    private Set<String> wordsSet;
    private String[] words;

    public GameEngine() {

    }

    public void addWord(String word) {
        wordList.add(word);
    }

    public List<String> getWordList() {
        return wordList;
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
