package formativetask1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordGame {

    private static final int MAX_TOTAL = 200;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> wordList = loadWordList();

        if (wordList == null) {
            System.out.println("Error loading word list. Exiting.");
            return;
        }

        List<String> playedWords = new ArrayList<>();
        int runningTotal = 0;
        int currentPlayer = 1;

        while (runningTotal <= MAX_TOTAL) {
            System.out.println("Player " + currentPlayer + " to choose a word...");
            String inputWord = getInputWord(scanner, playedWords);

            if (inputWord.equals("*")) {
                System.out.println("Player " + currentPlayer + " has given up. Game Over!");
                break;
            }

            int wordValue = calculateWordValue(inputWord);
            if (isValidWord(wordList, inputWord) && (runningTotal + wordValue <= MAX_TOTAL)) {
                playedWords.add(inputWord);
                runningTotal += wordValue;
                displayGameStatus(playedWords, runningTotal);
                currentPlayer = 3 - currentPlayer; // Switch player (1 to 2, 2 to 1)
            } else {
                System.out.println("Invalid word. Try again.");
            }
        }
    }

    private static List<String> loadWordList() {
        try {
            Path path = Paths.get("datafile.txt");
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInputWord(Scanner scanner, List<String> playedWords) {
        String inputWord;
        do {
            System.out.print("Enter a 3-letter word (lower case) or enter * to give up > ");
            inputWord = scanner.nextLine();
        } while (!isValidInput(inputWord, playedWords));
        return inputWord;
    }

    private static boolean isValidInput(String inputWord, List<String> playedWords) {
        return inputWord.matches("[a-z]{3}") || inputWord.equals("*") && !playedWords.isEmpty();
    }

    private static boolean isValidWord(List<String> wordList, String word) {
        return wordList.contains(word);
    }

    private static int calculateWordValue(String word) {
        int value = 0;
        for (char c : word.toCharArray()) {
            value += c - 'a' + 1;
        }
        return value;
    }

    private static void displayGameStatus(List<String> playedWords, int runningTotal) {
        System.out.println("------------------------------------------------------------");
        System.out.printf("| %-5s| %-12s| %-14s|\n", "word", "word total", "running total");
        System.out.println("------------------------------------------------------------");

        for (String playedWord : playedWords) {
            int wordValue = calculateWordValue(playedWord);
            System.out.printf("| %-5s| %-12d| %-14d|\n", playedWord, wordValue, runningTotal);
        }

        System.out.println("------------------------------------------------------------");
    }
}
