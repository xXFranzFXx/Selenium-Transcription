package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EmpathTraumaPage;

import java.io.IOException;
import java.util.List;

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
