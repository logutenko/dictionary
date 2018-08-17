import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Reader implements Supplier<List<String>> {
    private String path;
    private String charset;

    public Reader(String path) {
        this.path = path;
        charset = Charset.defaultCharset().name();
    }

    @Override
    public List<String> get() {
        Predicate<String> wordFilter = Pattern.compile("[а-я]{3,}").asPredicate();
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            Pattern p = Pattern.compile("charset=(.*)");
            Matcher m = p.matcher(connection.getContentType());
            if (m.find()) {
                charset = m.group(1);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset))) {
                return reader.lines().flatMap(Pattern.compile("\\s+")::splitAsStream)
                        .map(word -> word.toLowerCase().replaceAll("[^а-я]", ""))
                        .distinct()
                        .filter(wordFilter)
                        .collect(Collectors.toList());
            }
        } catch (MalformedURLException e) {
            System.err.println("Incorrect URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception while reading URL: " + e.getMessage());
        }
        return null;
    }
}
