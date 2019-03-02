import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;

/**
 * Ls utility
 *
 * A utility that prints the contents of directories to standard console output.
 *
 * @author Andrey Matveets
 */
public class LsLauncher {
    @Option(
            name = "-l",
            aliases = "--long",
            metaVar = "LongFlag",
            usage = "Additional file information"
    )
    private boolean lFlag;

    @Option(
            name = "-h",
            aliases = "--human-readable",
            metaVar = "HumanFlag",
            usage = "Translate in human-readable format"
    )
    private boolean hFlag;

    @Option(
            name = "-r",
            aliases = "--reverse",
            metaVar = "ReverseFlag",
            usage = "Reverses output order"
    )
    private boolean rFlag;

    @Option(
            name = "-o",
            aliases = "--output",
            metaVar = "OutputFlag",
            usage = "Indicates the name of file containing result"
    )
    private String outputFile;

    @Argument(
            required = true,
            metaVar = "FileOrDir",
            usage = "File or directory name"
    )
    private String fileOrDir;

    public static void main(String[] args) {
        new LsLauncher().launch(args);
    }

    //Launches a console application
    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar Ls.jar [-l] [-h] [-r] [-o output.file] directory_or_file");
            parser.printUsage(System.err);
        }

        try {
            Ls ls = new Ls(fileOrDir, lFlag, hFlag, rFlag, outputFile);
            ArrayList<String> fileList = ls.getConvertedFileList();

            if (outputFile == null) {
                for (String aFileList : fileList) {
                    System.out.println(aFileList);
                }
            } else {
                ls.writeInformationToFile(fileList);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
