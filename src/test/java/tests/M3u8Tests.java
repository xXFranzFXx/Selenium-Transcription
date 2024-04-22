package tests;

import base.BaseTest;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.M3u8Page;
import util.AssemblyAITranscriber;
import util.FfmpegUtil;
import util.FileUtil;
import util.TranscriptUtil;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class M3u8Tests extends BaseTest {
    @BeforeClass
    public void clearFiles() throws IOException {
        FileUtil.deleteAudioFiles();
    }
    @Test(description = "extract audio output from video stream and create audio transcription")
    public void convertM3u8() {
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
                    convertAudio.completeExceptionally(e);
                }
            }
                if(convertAudio.isDone()) {
                    CompletableFuture<List<File>> audioFileList = CompletableFuture.supplyAsync(() -> Arrays.asList(FileUtil.getAudioFiles()));
                    CompletableFuture<Transcript> futureTranscript = new CompletableFuture<>();
                    CompletableFuture<Void>futureFile = new CompletableFuture<>();
                    try {
                        for (File file : audioFileList.get()) {
                            System.out.println(file.getName());
                            Transcript transcript = null;
                            try {
                              futureTranscript.complete(AssemblyAITranscriber.transcribeAudioFile(file.getName()));
                             } catch (IOException e) {
                                futureTranscript.completeExceptionally(e);
                             }
                             String transcriptString = futureTranscript.get().toString();
                             System.out.println("transcription: " + transcriptString);
                             try {
                                String fileName = file.getName();
                                String name = fileName.substring(0, fileName.indexOf("m")) + "txt";
                                Path filePath = Path.of("src/test/resources/m3u8/" + name);
                                futureFile.complete(TranscriptUtil.convertTranscriptToFile(transcriptString, filePath));
                             } catch (IOException e) {
                                 futureFile.completeExceptionally(e);
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        audioFileList.completeExceptionally(e);
                    }
                }
            Assert.assertTrue(FileUtil.checkAudioFiles());
        });
        M3u8Page m3u8Page = new M3u8Page(getDriver());
        m3u8Page.pause(2);
        devTools.clearListeners();
        devTools.close();
    }
}
