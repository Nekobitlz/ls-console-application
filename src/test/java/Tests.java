import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.File;
import java.util.Date;

import static org.junit.Assert.*;

public class Tests {
    private String testFileDirectory = "src/test/resources/test.txt";
    private String testRarDirectory = "src/test/resources/test.rar";
    private String emptyDirectoryAddress = "src/test/resources/emptyDirectory";
    private String testDirectoryAddress = "src/test/resources/";

    private File testFile = new File(testFileDirectory);
    private File testRar = new File(testRarDirectory);
    private File emptyDirectory = new File (emptyDirectoryAddress);

    private String testFileTime;
    private String testRarTime;
    private String emptyDirectoryTime;

    @Before
    public void createTestFiles() throws IOException {
        testFile.createNewFile();
        testFileTime = getLastModify(testFile);

        testRar.createNewFile();
        testRarTime = getLastModify(testRar);

        emptyDirectory.createNewFile();
        emptyDirectoryTime = getLastModify(emptyDirectory);
    }

    @After
    public void deleteTestFiles() {
        testFile.delete();
        testRar.delete();
        emptyDirectory.delete();
    }

    @Test
    public void printFileAllFlags() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                true, true, true, null).getConvertedFileList();
        assertEquals("[0B " + testFileTime + " rwx test.txt]", dirList.toString());
    }

    @Test
    public void printDirectoryAllFlags() throws Exception {
        ArrayList<String> dirList = new Ls(testDirectoryAddress,
                true, true, true, null).getConvertedFileList();
        assertEquals("[0B " + testFileTime + " rwx test.txt, " +
                              "0B " + testRarTime + " rwx test.rar, " +
                              "0B "+ emptyDirectoryTime + " rwx emptyDirectory]", dirList.toString());
    }

    @Test
    public void printFileLFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                true, false, false, null).getConvertedFileList();
        assertEquals("[test.txt 111 " + testFileTime + " 0]", dirList.toString());
    }

    @Test
    public void printFileHFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                false, true, false, null).getConvertedFileList();
        assertEquals("[test.txt rwx  0B]", dirList.toString());
    }

    @Test
    public void printFileRFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testDirectoryAddress,
                false, false, true, null).getConvertedFileList();
        assertEquals("[   test.txt,    test.rar,    emptyDirectory]", dirList.toString());
    }

    @Test
    public void printFileLAndHFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                true, true, false, null).getConvertedFileList();
        assertEquals("[test.txt rwx " + testFileTime +" 0B]", dirList.toString());
    }

    @Test
    public void printFileHandRFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                false, true, true, null).getConvertedFileList();
        assertEquals("[0B  rwx test.txt]", dirList.toString());
    }

    @Test
    public void printFileLandRFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                true, false, true, null).getConvertedFileList();
        assertEquals("[0 " + testFileTime + " 111 test.txt]", dirList.toString());
    }

    @Test
    public void printFileOFlag() throws Exception {
        Ls ls = new Ls(testFileDirectory,
                false, false, false, "TestFile.txt");
        File expectedFile = new File("TestFile.txt");

        ArrayList<String> dirList = ls.getConvertedFileList();
        ls.writeInformationToFile(dirList);

        String content = new String(Files.readAllBytes(Paths.get("TestFile.txt")));

        assertTrue(expectedFile.exists());  //check if file exists
        assertEquals("test.txt   ", content); //check for equality of contents

        expectedFile.delete();
    }

    @Test
    public void printFileNoFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testFileDirectory,
                false, false, false, null).getConvertedFileList();
        assertEquals("[test.txt   ]", dirList.toString());
    }

    @Test
    public void printDirectoryNoFlag() throws Exception {
        ArrayList<String> dirList = new Ls(testDirectoryAddress,
                false, false, false, null).getConvertedFileList();
        assertEquals("[emptyDirectory   , test.rar   , test.txt   ]", dirList.toString());
    }

    @Test
    public void printEmptyDirectory() throws Exception {
        ArrayList<String> dirList = new Ls(emptyDirectoryAddress,
                false, false, false, null).getConvertedFileList();
        assertEquals("[emptyDirectory   ]", dirList.toString());
    }

    @Test (expected = FileNotFoundException.class)
    public void testNonexistentFile() throws Exception {
        ArrayList<String> dirList = new Ls(emptyDirectoryAddress + "/wtf.txt",
                true, true, true, null).getConvertedFileList();
    }

    private static String getLastModify(File file) {
        Date fileDate = new Date(file.lastModified());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        return dateFormat.format(fileDate);
    }

}
