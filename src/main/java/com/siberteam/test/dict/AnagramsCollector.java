package com.siberteam.test.dict;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;


public class AnagramsCollector {

    private static Set<String> splitAnagrams(Set<String> list) {
        return list.stream()
                .map(base -> list.stream()
                        .filter(word -> !word.equals(base))
                        .collect(joining(", ", base + ": ", "")))
                .collect(toSet());
    }

    private String transformToKey(String word) {
        char[] baseline = word.toCharArray();
        Arrays.sort(baseline);
        return new String(baseline);
    }

    public void collect(String inputFile, String outputFile) {
        Path input = Paths.get(inputFile);
        Path output = Paths.get(outputFile);
        try (Stream<String> s = Files.lines(input)) {
            Map<String, Set<String>> draftAnagrams = s.collect(groupingBy(this::transformToKey, toSet()));
            Set<String> anagrams = draftAnagrams.values().stream()
                    .filter(list -> list.size() > 1)
                    .map(AnagramsCollector::splitAnagrams)
                    .flatMap(Collection::stream)
                    .collect(toSet());
            Files.write(output, anagrams);
        } catch (IOException e) {
            System.err.println("General I/O exception: " + e.getMessage() + "\nException belongs to " + e.getClass());
        }
    }
}
