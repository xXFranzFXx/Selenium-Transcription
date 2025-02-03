package tests;

import base.BaseTest;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.openqa.selenium.devtools.v121.network.model.RequestId;
import org.testng.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.YoutubePage;
import util.DataProviderUtil;
import util.FileUtil;
import util.TranscriptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;
    @BeforeClass
    public void clearFiles() throws IOException {
        FileUtil.clearDirectory("youtubeDir");
    }
    @Test(description = "Get transcript text from every audio excerpt and write to a file", dataProvider="YoutubeData", dataProviderClass = DataProviderUtil.class)
    public void clickTranscriptButton(String youtubeURL) throws IOException {
        getDriver().get(youtubeURL);
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickDismiss();
        List<String> transcriptList =  youtubePage.createTranscriptList();
        TranscriptUtil.convertTranscriptToFile(transcriptList, "youtube/youtubeTranscript.txt");
        Assert.assertEquals(youtubePage.createMap().keySet().size(), youtubePage.getTimeStamps());
    }
    @Test(description = "Get the closed captions from network response and write to file", dataProvider="YoutubeData", dataProviderClass = DataProviderUtil.class)
    public void writeClosedCaptionsToFile(String youtubeURL){
        getDriver().get(youtubeURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            if (responseReceived.getResponse().getUrl().contains("timedtext")) {
                System.out.println("responseReceived: " + responseReceived.getRequestId().toString());
                try {
                        List<String> ccContent = new ArrayList<>();
                        String resBody = devTools.send(Network.getResponseBody(responseReceived.getRequestId())).getBody();
                        ccContent.add("Title: " + youtubePage.videoTitle());
                        ccContent.add("Video url: " + youtubeURL);
                        ccContent.add(resBody);
                        TranscriptUtil.convertTranscriptToFile(ccContent, "youtube/youtubeClosedCaptions.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickCCBtnNoAds();
        devTools.clearListeners();
        devTools.close();
    }
    @Test(description = "Get the closed captions from network response and write to file", dataProvider="YoutubeData", dataProviderClass = DataProviderUtil.class)
    public void writeClosedCaptionsToFileAsync(String youtubeURL, String name) {
        final CompletableFuture<RequestId> requestId = new CompletableFuture<>();
        final CompletableFuture<String> resBody = new CompletableFuture<>();
        getDriver().get(youtubeURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
                Request req = requestConsumer.getRequest();

                if (req.getUrl().contains("timedtext")) {
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
                    String fileDest = "youtube/"+name+".txt";
                    TranscriptUtil.convertTranscriptToFile(ccContent, fileDest);
                    Assert.assertEquals(requestId.get(), responseReceived.getRequestId());
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
