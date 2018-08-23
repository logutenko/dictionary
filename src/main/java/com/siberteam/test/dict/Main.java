package com.siberteam.test.dict;

import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        CommandLineHandler handler = new CommandLineHandler();
        DictionaryCollector dictionaryCollector = new DictionaryCollector();
        AnagramsCollector anagramsCollector = new AnagramsCollector();
        try {
            handler.parseArgs(args);
            String input = handler.getInputFile();
            String dictionary = handler.getDictionaryFile();
            String anagrams = handler.getAnagramsFile();
            dictionaryCollector.collect(input, dictionary);
            anagramsCollector.collect(dictionary, anagrams);
        } catch (ParseException | FileNotFoundException x) {
            System.err.println("Invalid arguments: " + x.getMessage());
        }
    }
}