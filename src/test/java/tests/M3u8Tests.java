package tests;

import base.BaseTest;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.M3u8Page;
import util.FfmpegUtil;
import util.FileUtil;
import util.TranscriptUtil;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class M3u8Tests extends BaseTest {
    @BeforeClass
    public void clearFiles() throws IOException {
        FileUtil.clearDirectory("m3u8Dir");
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
            String exclude = System.getProperty("exclude");
            if (!req.getUrl().startsWith(exclude) && req.getUrl().endsWith(".m3u8")) {
                String id = requestConsumer.getRequestId().toString();
                System.out.println("Video stream file: " + req.getUrl());
                try {
                    convertAudio.complete(FfmpegUtil.convertToMp3(req.getUrl(), id));
                    Assert.assertTrue(FileUtil.checkAudioFiles("m3u8Dir"));
                    TranscriptUtil.transcribeM3u8();
                    Assert.assertTrue(FileUtil.checkTxtFiles("m3u8Dir"));
                } catch (IOException e) {
                    convertAudio.completeExceptionally(e);
                }
            }
        });
        M3u8Page m3u8Page = new M3u8Page(getDriver());
        m3u8Page.pause(2);
        devTools.clearListeners();
        devTools.close();
    }
}

