package com.lohika;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrey Zhelezny
 *         Date: 11/23/15
 */
public class FileUtils {
    public static List<File> foundFilesByPattern(String targetDir, final String pattern) {
        return new ArrayList<File>(Arrays.asList(new File(targetDir).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().contains(pattern.toLowerCase());
            }
        })));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeStringsToFile(String str, String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if (file.exists())
            file.delete();
        PrintWriter fw = null;
        try {
            fw = new PrintWriter(new BufferedWriter(new FileWriter(file, file.exists())));
            fw.println(str);
        } finally {
            assert fw != null;
            fw.close();
        }
    }
}
