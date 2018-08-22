import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordsCollector {

    private static List<String> splitAnagrams(List<String> list) {
        List<String> finalList = new ArrayList<>();
        for (String base : list) {
            String wordAnagrams = list.stream()
                    .filter(word -> !word.equals(base))
                    .collect(Collectors.joining(", "));
            finalList.add(base + ": " + wordAnagrams);
        }
        return finalList;
    }

    private String transformToKey(String word) {
        char[] baseline = word.toCharArray();
        Arrays.sort(baseline);
        return new String(baseline);
    }

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
        try (Stream<String> s = Files.lines(input)) {
            Map<String, List<String>> draftAnagrams = new HashMap<>();
            s.forEach(word -> draftAnagrams.computeIfAbsent(transformToKey(word), k -> new ArrayList<>()).add(word));
            List<String> anagrams = draftAnagrams.values().stream()
                    .filter(list -> list.size() > 1)
                    .map(WordsCollector::splitAnagrams)
                    .flatMap(Collection::stream)
                    .sorted()
                    .collect(Collectors.toList());
            Files.write(output, anagrams);
        } catch (IOException e) {
            System.err.println("General I/O exception: " + e.getMessage() + "\nException belongs to " + e.getClass());
        }
    }
}

