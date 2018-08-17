import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Reader implements Runnable {
    private URL url;
    private ConcurrentSkipListSet<String> dictionary;
    private String charset;

    public Reader(String url, ConcurrentSkipListSet<String> dictionary) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            System.err.println("Incorrect URL: " + e.getMessage());
        }
        this.dictionary = dictionary;
        charset = Charset.defaultCharset().name();
    }

    @Override
    public void run() {
        Predicate<String> wordFilter = Pattern.compile("[а-я]{3,}").asPredicate();
        try {
            URLConnection connection = url.openConnection();
            Pattern p = Pattern.compile("charset=(.*)");
            Matcher m = p.matcher(connection.getContentType());
            if (m.find()) {
                charset = m.group(1);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset))) {
                reader.lines().flatMap(Pattern.compile("\\s+")::splitAsStream)
                        .map(word -> word.toLowerCase().replaceAll("[^а-я]", ""))
                        .filter(wordFilter)
                        .forEach(word -> dictionary.add(word));
            }
        } catch (IOException e) {
            System.err.println("Exception while reading URL: " + e.getMessage());
        }
    }
}
