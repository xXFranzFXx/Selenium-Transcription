package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import util.AssemblyAITranscriber;
import util.TranscriptUtil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
public class RestAPITests {
    public Response getTranscript() {
        return given()
                .params("token", System.getProperty("empathToken"))
                .baseUri(System.getProperty("empathUrl"))
                .get()
                .then()
                .statusCode(200)
                .extract().response();
    }
    @Test
    public void writeFileTextOnly() throws IOException {
        List<String> transcriptText = TranscriptUtil.extractTranscriptText(getTranscript());
        TranscriptUtil.convertTranscriptToFile(transcriptText, "transcript");
    }
    @Test
    public void writeFileTextWitTimeStamps() throws IOException {
        TranscriptUtil.convertTranscriptToFileWithTimestamps(getTranscript());
    }
    @Test
    public void transcribeFromLink(String link) throws IOException {
      TranscriptUtil.convertTranscriptToFileFromLink(link);
    }
}
