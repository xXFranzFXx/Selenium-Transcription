package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File[] getAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/m3u8");
        return rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
    }
    public static boolean checkAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/m3u8");
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
        return files != null;
    }
    public static boolean checkTxtFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/m3u8");
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3.txt")
        );
        return files != null;
    }
    public static void deleteAudioFiles() throws IOException {
        File directory = new File(System.getProperty("folder"));
        FileUtils.cleanDirectory(directory);
    }
}
