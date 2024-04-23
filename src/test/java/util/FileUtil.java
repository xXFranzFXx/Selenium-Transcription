package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static File[] getAudioFiles(String dir) {
        String filesLocation = System.getProperty(dir);
        File rootFolder = new File(System.getProperty("user.dir") + "/" + filesLocation);
        return rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
    }
    public static boolean checkAudioFiles(String dir) {
        String filesLocation = System.getProperty(dir);
        File rootFolder = new File(System.getProperty("user.dir") + "/" + filesLocation);
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
        return files != null;
    }
    public static boolean checkTxtFiles(String dir) {
        String filesLocation = System.getProperty(dir);
        File rootFolder = new File(System.getProperty("user.dir") + "/" + filesLocation);
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3.txt")
        );
        return files != null;
    }
    public static void clearDirectory(String dir) throws IOException {
        String filesLocation = System.getProperty(dir);
        File directory = new File(filesLocation);
        FileUtils.cleanDirectory(directory);
    }
}
