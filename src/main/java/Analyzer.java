import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analyzer implements Supplier<String> {
    private Stream<String> dictionary;
    private char[] baseline;

    public Analyzer(Stream<String> dictionary, String base) {
        this.dictionary = dictionary;
        baseline = base.toLowerCase().toCharArray();
        Arrays.sort(baseline);
    }

    private boolean isAnagram(String word) {
        char[] wordToArray = word.toLowerCase().toCharArray();
        Arrays.sort(wordToArray);
        return Arrays.equals(baseline, wordToArray);
    }

    @Override
    public String get() {
        return dictionary
                .filter(word -> isAnagram(word))
                .collect(Collectors.joining(", "));
    }
}
