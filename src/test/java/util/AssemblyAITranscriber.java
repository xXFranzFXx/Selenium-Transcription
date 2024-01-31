package util;
import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.*;


public class AssemblyAITranscriber {
    String url1 = "https://podcasts.helloaudio.fm/download/wb75n5fX1v/af5558b4-2257-4cd8-a196-fdd3b81d45f5.mp3";
    public static Transcript transcribeAudio(String url) {
        AssemblyAI client = AssemblyAI.builder()
                .apiKey(System.getProperty("assemblyAIApiKey"))
                .build();
        Transcript transcript = client.transcripts().transcribe(url);
        if (transcript.getStatus().equals("error")) {
            System.err.println(transcript.getError());
        }
       return transcript;
    }
}
