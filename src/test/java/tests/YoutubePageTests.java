package tests;

import base.BaseTest;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.openqa.selenium.devtools.v121.network.model.RequestId;
import org.testng.Assert;

import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.YoutubePage;
import util.TranscriptUtil;
import util.listeners.TestListener;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;
    public void writeFile(String transcript) throws IOException {
        try {
            TranscriptUtil.convertTranscriptToFile(transcript, "youtubeTranscript");
        } catch (IOException e) {
           e.printStackTrace();
        }
        }
    @Test(description = "Get transcript text from every audio excerpt and write to a file")
    public void clickTranscriptButton() throws IOException {
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickMore().clickTranscript();
        List<String> transcriptList =  youtubePage.createTranscriptList();
        TranscriptUtil.convertTranscriptToFile(transcriptList, "youtubeTranscript");
        Assert.assertEquals(youtubePage.createMap().keySet().size(), youtubePage.getTimeStamps());
    }

    @Test(description = "Get the closed captions from network response and write to file")
    @Parameters({"youtubeURL"})
    public void writeClosedCaptionsToFile(String youtubeURL) throws ExecutionException, InterruptedException {
        getDriver().get(youtubeURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        final RequestId[] rID = new RequestId[1];
        CompletableFuture<RequestId> reqID = CompletableFuture.supplyAsync(() -> {
            devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
                Request req = requestConsumer.getRequest();
                if (req.getUrl().contains("timedtext")) {
                 rID[0] = requestConsumer.getRequestId();
                }
            });
            return rID[0];
        });

        CompletableFuture<Void> resBody = reqID.thenApplyAsync( id -> {
                devTools.addListener(Network.responseReceived(), responseReceived -> {
                        String body = devTools.send(Network.getResponseBody(id)).getBody();
                    try {
                        writeFile(body);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            });
        resBody.get();

        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickCCBtn();
        devTools.close();
    }
}
