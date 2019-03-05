import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contains methods for running the console ls utility
 *
 * @author Andrey Matveets
 */
public class Ls {
    private boolean lFlag, hFlag, rFlag;
    private String fileOrDir, outputFile;

    /**
     * Calls application constructor
     *
     * @param fileOrDir Target file or directory name
     * @param lFlag Long flag
     * @param hFlag Human-readable flag
     * @param rFlag Reverse flag
     * @param outputFile Output file name
     */
    public Ls(String fileOrDir, boolean lFlag, boolean hFlag, boolean rFlag, String outputFile) {
        this.fileOrDir = fileOrDir;
        this.lFlag = lFlag;
        this.hFlag = hFlag;
        this.rFlag = rFlag;
        this.outputFile = outputFile;
    }

    /**
     * Gets a converted list with all flags
     *
     * @return File list with all flags
     */
    public ArrayList<String> getConvertedFileList() throws Exception {
        File files = new File(fileOrDir);

        if (!files.exists())
            throw new FileNotFoundException("File not found");

        ArrayList<File> fileList = makeFileList(files);

        return printDirectory(fileList);
    }

    /**
     * Writes all the found information to a file (output flag)
     *
     * @param fileList Converted list with all flags
     * @throws IOException If an I/O error occurs
     */
    public void writeInformationToFile(ArrayList<String> fileList) throws IOException {
        File newOutputFile = new File(outputFile);
        FileWriter fileWriter = new FileWriter(newOutputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter writer = new PrintWriter(bufferedWriter);

        for (String aFileList : fileList) {
            writer.write(aFileList);
        }

        System.out.println("Result is recorded in " + outputFile);
        writer.close();
    }

    /**
     * Writes all directory files to one list.
     *
     * @param file Target file or directories
     *
     * @return List containing all files of the directory
     */
    private ArrayList<File> makeFileList(File file) {
        ArrayList<File> fileList = new ArrayList<>();

        if (file.isDirectory())
            Collections.addAll(fileList, Objects.requireNonNull(file.listFiles()));
        else
            fileList.add(file);

        //sorted by name
        fileList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return fileList;
    }

    /**
     * Converts ALL files from the list with all flags
     *
     * @param fileList List containing all files of the directory
     *
     * @return List of all converted files information
     */
    private ArrayList<String> printDirectory(ArrayList<File> fileList) {
        ArrayList<String> formattedList = new ArrayList<>();

        if (rFlag) {
            //inverts list, i.e. changes the order of output to opposite
            Collections.reverse(fileList);
        }

        for (File aFileList : fileList) {
            formattedList.add(printFile(aFileList));
        }

        return formattedList;
    }

    /**
     * Converts ONE file with all flags
     *
     * @param file Target file
     *
     * @return String containing file information
     */
    private String printFile(File file) {
        String fileName = file.getName();
        String rwxPermissions = getPermissions(file);

        String lastModify = "";
        String fileStringLength = "";

        if (lFlag) {
            lastModify = getLastModify(file);
            fileStringLength = Long.toString(file.length()); //default byte length
        }

        if (hFlag) {
            fileStringLength = getHumanReadableSize(file);
        }

        //if rFlag is active - reverts the output line
        String convertedFile = rFlag
                ? String.format("%s %s %s %s", fileStringLength,  lastModify, rwxPermissions, fileName)
                : String.format("%s %s %s %s", fileName, rwxPermissions, lastModify, fileStringLength);

        return convertedFile.replaceAll("[\\s]{2,}", " ").trim();
    }

    /**
     * Gets the date the file was last modified
     *
     * @param file Target file
     *
     * @return Date and time of the last file change
     */
    public static String getLastModify(File file) {
        Date fileDate = new Date(file.lastModified());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        return dateFormat.format(fileDate);
    }

    /**
     * Get rwx format
     *
     * @param file Target file
     *
     * @return Rwx file permissions
     */
    private String getPermissions(File file) {
        String permission = "";

        if (lFlag) {
            //bitmask format
            permission += file.canRead() ? 1 : 0;
            permission += file.canWrite() ? 1 : 0;
            permission += file.canExecute() ? 1 : 0;
        }

        if (hFlag) {
            //rwx format
            permission = "";
            permission += file.canRead() ? "r" : "";
            permission += file.canWrite() ? "w" : "";
            permission += file.canExecute() ? "x" : "";
        }

        return permission;
    }

    /**
     * Gets file size in human-readable format
     *
     * @param file Target file
     *
     * @return Human-readable file size
     */
    public static String getHumanReadableSize(File file) {
        long fileSize = file.length();
        String hrSize = "";

        if (fileSize < 1024)
            hrSize += fileSize + "B";
        else if (fileSize < 1024 * 1024)
            hrSize += fileSize / 1024 + "KB";
        else if (fileSize < 1024 * 1024 * 1024)
            hrSize += fileSize / (1024 * 1024) + "MB";
        else
            hrSize += fileSize / (1024 * 1024 * 1024) + "GB";

        return hrSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Ls ls = (Ls) o;

        return lFlag == ls.lFlag &&
                hFlag == ls.hFlag &&
                rFlag == ls.rFlag &&
                Objects.equals(outputFile, ls.outputFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lFlag, hFlag, rFlag, outputFile);
    }
}
