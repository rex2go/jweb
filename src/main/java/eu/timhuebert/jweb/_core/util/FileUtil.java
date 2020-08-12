package eu.timhuebert.jweb._core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    public static byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

}