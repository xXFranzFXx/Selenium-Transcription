package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File[] getAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/");
        return rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
    }
    public static boolean checkAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/");
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3") && file.getName().endsWith(".mp3.txt")
        );
        return files != null;
    }
    public static void deleteAudioFiles() throws IOException {
        File directory = new File(System.getProperty("ffmpegDestFile"));
        FileUtils.cleanDirectory(directory);
    }
}
