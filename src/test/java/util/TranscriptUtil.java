package util;

import com.assemblyai.api.resources.transcripts.types.Transcript;
import io.restassured.response.Response;
import org.testng.Reporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
    public static void convertTranscriptToFile(List<String> transcript, String name) throws IOException {
        Path filePath = Paths.get("src/test/resources/"+name+".txt");
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        for (String str : transcript) {
            Files.writeString(filePath, str + System.lineSeparator(),
                    StandardOpenOption.APPEND);
        }
    }
    public static void convertTranscriptToFileWithTimestamps(Response response) throws IOException {
        Path filePath = Paths.get("src/test/resources/transcriptAndTimestamps.txt");
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        Files.writeString(filePath, response.asString(), StandardOpenOption.APPEND);
    }
    public static void convertTranscriptToFileFromLink(String link, String fileName) throws IOException {
        Path filePath = Paths.get("src/test/resources/"+fileName+".txt");
        Transcript transcript = AssemblyAITranscriber.transcribeAudio(link);
        String transcriptString = String.valueOf(transcript.getText());
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);
        Files.writeString(filePath, transcriptString, StandardOpenOption.APPEND);
        Reporter.log("Successfully transcribed link: " + link, true);
        Reporter.log("All links for the current test will be transcribed to "+fileName+".txt", true);
    }
    public static List<String> readFileToList(String fileName) throws IOException {
        Path filePath = Paths.get("src/test/resources/"+fileName+".txt");
        List<String> result;
        try (Stream<String> lines = Files.lines(filePath)) {
            result = lines.toList();
        }
        return result;
    }
}
