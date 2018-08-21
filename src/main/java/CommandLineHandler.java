import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;

public class CommandLineHandler {
    public static final String INPUT_DFLT = "input.txt";
    public static final String DICTIONARY_DFLT = "dictionary.txt";
    public static final String ANAGRAMS_DFLT = "anagrams.txt";
    private String inputFile;
    private String dictionaryFile;
    private String anagramsFile;

    public CommandLineHandler() {
        inputFile = INPUT_DFLT;
        dictionaryFile = DICTIONARY_DFLT;
        anagramsFile = ANAGRAMS_DFLT;
    }

    public void parseArgs(String[] args) throws ParseException, FileNotFoundException {
        Option optInput = new Option("i", "input", true, "To provide input file (\"input.txt\" by default)");
        optInput.setArgs(1);
        Option optOutput = new Option("o", "output", true, "To provide output files (\"dictionary.txt, anagrams.txt\" by default)");
        optOutput.setArgs(2);
        optOutput.setValueSeparator(',');
        Option optHelp = new Option("h", "help", false, "Help information");
        Options optionList = new Options();
        optionList.addOption(optInput);
        optionList.addOption(optOutput);
        optionList.addOption(optHelp);
        CommandLineParser cmdLinePosixParser = new DefaultParser();
        CommandLine commandLine = cmdLinePosixParser.parse(optionList, args);
        if (commandLine.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("app collects Russian words from files to output dictionary", optionList);
        }
        if (commandLine.hasOption("i")) {
            String[] arguments = commandLine.getOptionValues("i");
            inputFile = arguments[0];
            File file = new File(inputFile);
            if (!file.exists()) {
                throw new FileNotFoundException("The file " + inputFile + " doesn't exist. Try again.");
            }
        }
        if (commandLine.hasOption("o")) {
            String[] arguments = commandLine.getOptionValues("o");
            if (arguments.length < 2){
                throw new ParseException("You should specify two files for option -o like \"-o=1.txt,2.txt\"");
            }
            dictionaryFile = arguments[0];
            anagramsFile = arguments[1];
        }
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getDictionaryFile() {
        return dictionaryFile;
    }

    public String getAnagramsFile() {
        return anagramsFile;
    }
}
