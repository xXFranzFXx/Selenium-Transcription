package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FfmpegUtil {
        public static Void convertToMp3(String m3u8Link, String name) throws IOException {
            String ffmpeg = System.getProperty("ffmpegDir");
            String destinationFile = System.getProperty("ffmpegDestFile")+"/"+name+".mp3";
            String folderPath = System.getProperty("user.dir") + destinationFile;
            ProcessBuilder processBuilder = new ProcessBuilder(ffmpeg, "-i", m3u8Link, folderPath);
            try {
                Process process = processBuilder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    public static File convertToMp3File(String m3u8Link, String name) throws IOException {
        String ffmpeg = System.getProperty("ffmpegDir");
        String destinationFile = System.getProperty("ffmpegDestFile")+"/"+name+".mp3";
        String folderPath = System.getProperty("user.dir") + destinationFile;
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpeg, "-i", m3u8Link, folderPath);
        try {
            Process process = processBuilder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(destinationFile);
    }

}
