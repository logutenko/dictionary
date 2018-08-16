import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;

public class CommandLineHandler {
    public static final String INPUT_DFLT = "input.txt";
    public static final String OUTPUT_DFLT = "output.txt";
    private String input;
    private String output;

    public CommandLineHandler() {
        input = INPUT_DFLT;
        output = OUTPUT_DFLT;
    }

    public void parseArgs(String[] args) throws ParseException, FileNotFoundException {
        Option optInput = new Option("i", "input", true, "To provide input file (\"input.txt\" by default)");
        optInput.setArgs(1);
        Option optOutput = new Option("o", "output", true, "To provide output file (\"output.txt\" by default)");
        optOutput.setArgs(1);
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
            input = arguments[0];
            File file = new File(input);
            if (!file.exists()) {
                throw new FileNotFoundException("The file " + input + " doesn't exist. Try again.");
            }
        }
        if (commandLine.hasOption("o")) {
            String[] arguments = commandLine.getOptionValues("o");
            output = arguments[0];
        }
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}
