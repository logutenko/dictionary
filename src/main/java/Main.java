import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        ConcurrentSkipListSet<String> dictionary = new ConcurrentSkipListSet<>();
         try {
            handler.parseArgs(args);
            Path input = Paths.get(handler.getInput());
            Path output = Paths.get(handler.getOutput());
            ExecutorService executorService = Executors.newCachedThreadPool();
            try (Stream<String> s = Files.lines(input)) {
                s.forEach(file -> executorService.submit(new Reader(file, dictionary)));
            }
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
            Files.write(output, dictionary);
        } catch (ParseException | FileNotFoundException x) {
            System.err.println("Invalid arguments: " + x.getMessage());
        } catch (IOException x) {
            System.err.println("General I/O exception: " + x.getMessage() + "\nException belongs to " + x.getClass());
        }
    }
}