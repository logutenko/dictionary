import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordsCollector {

    public void collectDictionary(String inputFile, String outputFile) {
        ConcurrentSkipListSet<String> dictionary = new ConcurrentSkipListSet<>();
        Path input = Paths.get(inputFile);
        Path output = Paths.get(outputFile);
        try (Stream<String> s = Files.lines(input)) {
            s.map(url -> CompletableFuture.supplyAsync(new Reader(url)).thenAccept(dictionary::addAll))
                    .forEach(CompletableFuture::join);
            Files.write(output, dictionary);
        } catch (IOException e) {
            System.err.println("General I/O exception: " + e.getMessage() + "\nException belongs to " + e.getClass());
        }
    }

    public void collectAnagrams(String inputFile, String outputFile) {
        Path input = Paths.get(inputFile);
        Path output = Paths.get(outputFile);
        CopyOnWriteArrayList<String> anagrams = new CopyOnWriteArrayList<>();
        try (Stream<String> s = Files.lines(input)) {
            List<String> dictionary = s.collect(Collectors.toList());
            dictionary.stream()
                    .map(word -> CompletableFuture.supplyAsync(new Analyzer(dictionary
                            .stream()
                            .filter(x -> x.length() == word.length() && !x.equals(word)), word))
                            .thenAccept(anagram -> {
                                if (!anagram.isEmpty())
                                    anagrams.add(word + ": " + anagram);
                            }))
                    .forEach(CompletableFuture::join);
            Files.write(output, anagrams);
        } catch (IOException e) {
            System.err.println("General I/O exception: " + e.getMessage() + "\nException belongs to " + e.getClass());
        }
    }
}

