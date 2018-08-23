package com.siberteam.test.dict;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class DictionaryCollector {

    public void collect(String inputFile, String outputFile) {
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
}
