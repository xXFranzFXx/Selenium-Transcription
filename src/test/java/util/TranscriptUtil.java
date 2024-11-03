package util;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import io.restassured.response.Response;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.Stream;

public class TranscriptUtil {
    public static List<String> extractTranscriptText(Response response) {
        String regex = "[a-zA-Z].+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response.asString());
        List<String> allMatches = new ArrayList<>();
        while (matcher.find()) {
            allMatches.add(matcher.group(0));
        }
        allMatches.remove(0);
        String finalString = String.join("", allMatches);
        return allMatches;
    }
    public static void convertTranscriptToFile(List<String> transcript, String file) throws IOException {
        Path filePath = Path.of("src/test/resources/"+file);
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        for (String str : transcript) {
            Files.writeString(filePath, str + System.lineSeparator(),
                    StandardOpenOption.APPEND);
        }
    }
    public static Void convertTranscriptToFile(String transcript, String file) throws IOException {
        Path filePath = Path.of("src/test/resources/"+file);
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        Files.writeString(filePath, transcript,
                    StandardOpenOption.APPEND);
        return null;
    }
    public static void convertTranscriptToFileWithTimestamps(Response response) throws IOException {
        Path filePath = Path.of("src/test/resources/empathTrauma/transcriptAndTimestamps.txt");
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        Files.writeString(filePath, response.asString(), StandardOpenOption.APPEND);
    }
    public static Void convertTranscriptToFileFromLink(String link, String fileName) throws IOException {
        Path filePath = Path.of("src/test/resources/"+fileName+".txt");
        Transcript transcript = AssemblyAITranscriber.transcribeAudio(link);
        String transcriptString = String.valueOf(transcript.getText());
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        Files.writeString(filePath, transcriptString, StandardOpenOption.APPEND);
        Reporter.log("Successfully transcribed link: " + link, true);
        Reporter.log("All links for the current test will be transcribed to "+fileName+".txt", true);
        return null;
    }

    /*
    use this helper method to construct a list of audio file links from audioLinks.txt file. the links in that list can be transcribed
     */
    public static List<String> readFileToList(String fileName) throws IOException {
        Path filePath = Path.of("src/test/resources/"+fileName+".txt");
        List<String> result;
        try (Stream<String> lines = Files.lines(filePath)) {
            result = lines.toList();
        }
        return result;
    }
    public static CompletableFuture<List<String>> audioLinks = CompletableFuture.supplyAsync(() -> {
            try {
                return readFileToList("audioLinks");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    public static CompletableFuture<Void> asyncTranscribeLinks() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> futureTranscript = new CompletableFuture<>();
            try {
                for (String s : audioLinks.get())
                    futureTranscript.complete(convertTranscriptToFileFromLink(s, "asyncTranscribeFromLinks"));
            } catch (IOException | ExecutionException | InterruptedException e) {
                futureTranscript.completeExceptionally(e);
            }
        System.out.println(futureTranscript.get());
        return futureTranscript;
    }
    CompletableFuture<Void> convert = audioLinks.thenAccept(list -> {
        for (String l: list) {
            try {
                convertTranscriptToFileFromLink(l, "asyncTranscribeFromLinks");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    });
    public static Void transcribeM3u8() throws IOException {
        File[] audioFiles = FileUtil.getAudioFiles("m3u8Dir");
        for (File file : audioFiles) {
            System.out.println(file.getName());
            try {
                Transcript transcript = AssemblyAITranscriber.transcribeAudioFile(file.getName());
                String transcriptString = transcript.toString();
                System.out.println("transcription: " + transcriptString);
                String fileName = file.getName();
                String name = fileName.substring(0, fileName.indexOf("m")) + "txt";
                String fileDest = "m3u8/"+name;
                TranscriptUtil.convertTranscriptToFile(transcriptString, fileDest);
            } catch (IOException e) {
             e.printStackTrace();
            }
        }
        return null;
    }
    public static Void transcribeM3u8Lemur(String prompt) throws IOException {
        File[] audioFiles = FileUtil.getAudioFiles("m3u8Dir");
        for (File file : audioFiles) {
            System.out.println(file.getName());
            try {
                String fileName = file.getName();
                String lemur = AssemblyAITranscriber.transcribeAudioFileWithLemur(fileName, prompt);
                System.out.println("lemur: " + lemur);
                String name = fileName.substring(0, fileName.indexOf("m")) +"lemur.txt";
                String fileDest = "m3u8/"+name;
                TranscriptUtil.convertTranscriptToFile(lemur, fileDest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
