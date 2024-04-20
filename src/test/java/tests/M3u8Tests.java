package tests;

import base.BaseTest;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.M3u8Page;
import util.AssemblyAITranscriber;
import util.FfmpegUtil;
import util.TranscriptUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class M3u8Tests extends BaseTest {
    public File[] getAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/");
        return rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3")
        );
    }
    public boolean checkAudioFiles() {
        File rootFolder = new File(System.getProperty("user.dir") + "/src/test/resources/");
        File[] files = rootFolder.listFiles(file ->
                file.getName().endsWith(".mp3") && file.getName().endsWith(".mp3.txt")
        );
        return files != null;
    }
    @Test(description = "extract audio output from video stream")
    public void convertM3u8() throws IOException{
        final CompletableFuture<Void> convertAudio = new CompletableFuture<>();
        getDriver().get(System.getProperty("m3u8Player"));
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request req = requestConsumer.getRequest();
            if (!req.getUrl().startsWith("https://embed-cloudfront.wistia.com/deliveries/") && req.getUrl().endsWith(".m3u8")) {
                String id = requestConsumer.getRequestId().toString();
                System.out.println("Video stream file: " + req.getUrl());
                try {
                    convertAudio.complete(FfmpegUtil.convertToMp3(req.getUrl(), id));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                convertAudio.get();
            }  catch (ExecutionException | InterruptedException e) {
                convertAudio.completeExceptionally(e);
            }
            if(convertAudio.isDone()) {
                List<File> audioFiles = Arrays.stream(getAudioFiles()).toList();
                for(File file: audioFiles) {
                    Transcript transcript = null;
                    try {
                        transcript = AssemblyAITranscriber.transcribeAudioFile(file.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String transcriptString = transcript.toString();
                    System.out.println("transcription: " + transcriptString);
                    try {
                        TranscriptUtil.convertTranscriptToFile(transcriptString, file.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Assert.assertTrue(checkAudioFiles());
            }
        });
        M3u8Page m3u8Page = new M3u8Page(getDriver());
        m3u8Page.pause(2);
        devTools.clearListeners();
        devTools.close();
    }
}

