package tests;

import base.BaseTest;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.openqa.selenium.devtools.v121.network.model.RequestId;
import org.testng.Assert;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.YoutubePage;
import util.TranscriptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;

    @Test(description = "Get transcript text from every audio excerpt and write to a file")
    @Parameters({"youtubeURL"})

    public void clickTranscriptButton(String youtubeURL) throws IOException {
        getDriver().get(youtubeURL);
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickMore().clickTranscript();
        List<String> transcriptList =  youtubePage.createTranscriptList();
        TranscriptUtil.convertTranscriptToFile(transcriptList, "youtubeTranscript");
        Assert.assertEquals(youtubePage.createMap().keySet().size(), youtubePage.getTimeStamps());
    }

    @Test(description = "Get the closed captions from network response and write to file")
    @Parameters({"youtubeURL"})
    public void writeClosedCaptionsToFile(String youtubeURL){
        final CompletableFuture<RequestId> requestId = new CompletableFuture<>();
        final CompletableFuture<String> resBody = new CompletableFuture<>();
        getDriver().get(youtubeURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request req = requestConsumer.getRequest();
            if(req.getUrl().contains("timedtext")) {
                requestId.complete(requestConsumer.getRequestId());
                }
            });

        devTools.addListener(Network.responseReceived(), responseReceived -> {
                    try {
                        List<String> ccContent = new ArrayList<>();
                        resBody.complete(devTools.send(Network.getResponseBody(requestId.get())).getBody());
                        ccContent.add("Title: " + youtubePage.videoTitle());
                        ccContent.add("Video url: " + youtubeURL);
                        ccContent.add(resBody.get());
                        TranscriptUtil.convertTranscriptToFile(ccContent, "youtubeClosedCaptions");
                    } catch (InterruptedException | ExecutionException | IOException e) {
                        resBody.completeExceptionally(e);
                    }
                });
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickCCBtn();
        devTools.clearListeners();
        devTools.close();
    }
}
