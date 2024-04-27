package util;
import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.lemur.requests.LemurTaskParams;
import com.assemblyai.api.resources.transcripts.types.*;
import util.logs.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AssemblyAITranscriber {
    //    String url1 = "https://podcasts.helloaudio.fm/download/wb75n5fX1v/af5558b4-2257-4cd8-a196-fdd3b81d45f5.mp3";
    public static Transcript transcribeAudio(String url) {
        AssemblyAI client = AssemblyAI.builder()
                .apiKey(System.getProperty("assemblyAIApiKey"))
                .build();
        Transcript transcript = client.transcripts().transcribe(url);
        if (transcript.getStatus().equals("error")) {
            System.err.println(transcript.getError());
            Log.error("Error transcribing audio url: " + transcript.getAudioUrl());
            Log.error("Error message: " + transcript.getError());
        }
        Log.info("Audio url: " + transcript.getAudioUrl());
        Log.info("Audio transcription id: " + transcript.getId());
        Log.info("Audio duration: " + transcript.getAudioDuration());
        Log.info("Audio transcription summary: " + transcript.getSummary());
        return transcript;
    }

    public static Transcript transcribeAudioFile(String fileName) throws IOException {
        String filePath = System.getProperty("m3u8Dir") + "/" +fileName;
        AssemblyAI client = AssemblyAI.builder()
                .apiKey(System.getProperty("assemblyAIApiKey"))
                .build();
        Transcript transcript = client.transcripts().transcribe(new File(filePath));
        if (transcript.getStatus().equals("error")) {
            System.err.println(transcript.getError());
            Log.error("Error transcribing audio url: " + transcript.getAudioUrl());
            Log.error("Error message: " + transcript.getError());
        }
        Log.info("Audio url: " + transcript.getAudioUrl());
        Log.info("Audio transcription id: " + transcript.getId());
        Log.info("Audio duration: " + transcript.getAudioDuration());
        Log.info("Audio transcription summary: " + transcript.getSummary());
        return transcript;
    }
    public static String transcribeAudioFileWithLemur(String fileName, String prompt) throws IOException {
        String filePath = System.getProperty("m3u8Dir") + "/" + fileName;
        AssemblyAI client = AssemblyAI.builder()
                .apiKey(System.getProperty("assemblyAIApiKey"))
                .build();
        Transcript transcript = client.transcripts().transcribe(new File(filePath));

        // Step 3: Apply LeMUR.
        var params = LemurTaskParams.builder()
                .prompt(prompt)
                .transcriptIds(List.of(transcript.getId()))
                .build();

        var response = client.lemur().task(params);

        return response.getResponse();
    }
}