import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Reader implements Runnable {
    private String file;
    private ConcurrentSkipListSet<String> dictionary;

    public Reader(String file, ConcurrentSkipListSet<String> dictionary) {
        this.file = file;
        this.dictionary = dictionary;
    }

    @Override
    public void run() {
        Path input = Paths.get(file);
        Predicate<String> wordFilter = Pattern.compile("[а-я]{3,}").asPredicate();
        try (Stream<String> s = Files.lines(input)) {
            s.flatMap(Pattern.compile("\\s+")::splitAsStream)
                    .map(word -> word.toLowerCase().replaceAll("[^а-я]",""))
                    .filter(wordFilter)
                    .forEach(word -> dictionary.add(word));
        } catch (IOException x) {
            System.err.println("Exception  while reading: " + x.getMessage() + "\nException belongs to " + x.getClass());
        }
    }
}
