package tests;

import base.BaseTest;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.openqa.selenium.devtools.v121.network.model.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EasyYesLeadsPage;
import pages.EmpathTraumaPage;
import util.TranscriptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

public class EmpathTraumaTests extends BaseTest {
    EmpathTraumaPage empathTraumaPage;
    private final int maxExcerpts = 869;
    @BeforeMethod
    @Parameters({"empathTraumaURL"})
    public void setupBrowser(String empathTraumaURL) {
        getDriver().get(empathTraumaURL);
    }
    @Test(description = "Get transcript text from every audio excerpt and store it in a List")
    public void clickTranscriptButton() throws InterruptedException, IOException {
        empathTraumaPage = new EmpathTraumaPage(getDriver());
        List<String> transcript = empathTraumaPage.visitIframeSource().clickTranscript().findTranscripts();
        Assert.assertEquals(transcript.size(), maxExcerpts);
    }

}
