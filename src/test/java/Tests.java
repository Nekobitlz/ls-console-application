import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void printFileAllFlags() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                true, true, true, null).getConvertedFileList();
        assertEquals("[2KB 01.03.2019 15:51:20 rwx o.txt]", dirList.toString());
    }

    @Test
    public void printDirectoryAllFlags() {
        ArrayList<String> dirList = new Ls("src/test/resources/testDirectory",
                true, true, true, null).getConvertedFileList();
        assertEquals("[2MB 01.03.2019 18:13:32 rx RANDOMFILE.txt, " +
                              "0B 01.03.2019 18:09:19 rwx oneMoreFolder, " +
                              "24B 01.03.2019 14:45:48 rwx !!#I#U@#I@U#'.rar]", dirList.toString());
    }

    @Test
    public void printFileLFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                true, false, false, null).getConvertedFileList();
        assertEquals("[o.txt 111 01.03.2019 15:51:20 2180]", dirList.toString());
    }

    @Test
    public void printFileHFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                false, true, false, null).getConvertedFileList();
        assertEquals("[o.txt rwx  2KB]", dirList.toString());
    }

    @Test
    public void printFileRFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/testDirectory",
                false, false, true, null).getConvertedFileList();
        assertEquals("[   RANDOMFILE.txt,    oneMoreFolder,    !!#I#U@#I@U#'.rar]", dirList.toString());
    }

    @Test
    public void printFileLAndHFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                true, true, false, null).getConvertedFileList();
        assertEquals("[o.txt rwx 01.03.2019 15:51:20 2KB]", dirList.toString());
    }

    @Test
    public void printFileHandRFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                false, true, true, null).getConvertedFileList();
        assertEquals("[2KB  rwx o.txt]", dirList.toString());
    }

    @Test
    public void printFileLandRFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                true, false, true, null).getConvertedFileList();
        assertEquals("[2180 01.03.2019 15:51:20 111 o.txt]", dirList.toString());
    }

    @Test
    public void printFileOFlag() throws IOException {
        Ls ls = new Ls("src/test/resources/o.txt",
                false, false, false, "TestFile.txt");
        File expectedFile = new File("TestFile.txt");

        ArrayList<String> dirList = ls.getConvertedFileList();
        ls.writeInformationToFile(dirList);

        String content = new String(Files.readAllBytes(Paths.get("TestFile.txt")));

        assertTrue(expectedFile.exists());  //check if file exists
        assertEquals("o.txt   ", content); //check for equality of contents

        expectedFile.delete();
    }

    @Test
    public void printFileNoFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/o.txt",
                false, false, false, null).getConvertedFileList();
        assertEquals("[o.txt   ]", dirList.toString());
    }

    @Test
    public void printDirectoryNoFlag() {
        ArrayList<String> dirList = new Ls("src/test/resources/testDirectory",
                false, false, false, null).getConvertedFileList();
        assertEquals("[!!#I#U@#I@U#'.rar   , oneMoreFolder   , RANDOMFILE.txt   ]", dirList.toString());
    }
}
