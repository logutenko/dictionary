import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        WordsCollector collector = new WordsCollector();
        try {
            handler.parseArgs(args);
            String input = handler.getInputFile();
            String dictionary = handler.getDictionaryFile();
            String anagrams = handler.getAnagramsFile();
            collector.collectDictionary(input, dictionary);
            collector.collectAnagrams(dictionary, anagrams);
        } catch (ParseException | FileNotFoundException x) {
            System.err.println("Invalid arguments: " + x.getMessage());
        }
    }
}