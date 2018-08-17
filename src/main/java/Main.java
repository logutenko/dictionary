import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        try {
            handler.parseArgs(args);
            Path input = Paths.get(handler.getInput());
            Path output = Paths.get(handler.getOutput());
            List<String> result;
            try (Stream<String> s = Files.lines(input)) {
                result = s.map(url -> CompletableFuture.supplyAsync(new Reader(url)))
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }
            Files.write(output, result);
        } catch (ParseException | FileNotFoundException x) {
            System.err.println("Invalid arguments: " + x.getMessage());
        } catch (IOException x) {
            System.err.println("General I/O exception: " + x.getMessage() + "\nException belongs to " + x.getClass());
        }
    }
}