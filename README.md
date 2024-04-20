
<h1>Selenium-Transcription</h1><br>


<h3>Test Subjects</h3>
https://empathtrauma.com<br>
https://easyyesleads.com<br>
https://www.youtube.com<br>
https://xhoni-mimillari.mykajabi.com/linkedin<br>

<img src="assets/empath.png" width="225" height="175"/>
<img src="assets/easyYesLeads.png" width="225" height="175"/>

<h3>Programming Language</h3>
Java <br>

<h3>Libraries/Tools Used</h3> 
* Selenium - Web automation<br>
* Selenium devtools - capturing network requests<br>
* CompletableFutures to handle async queries<br>
* assemblyAI - transcribing audio files<br>
* TestNG - Test execution<br>
* REST Assured - Validation of REST web services<br>
* log4j - Capturing logs<br>
* Extent Reports - reporting<br>
* Gradle - Build and package management<br>
* FFmpeg - For extracting audio data from streaming video files<br>

<h3>Overview</h3>
This project demonstrates test automation with iframes containing lazy-loaded elements. The aim is to locate audio data that can be used to create transcripts. Streaming video files (m3u8) are transcribed by using FFmpeg as a subprocess to convert them into an audio file first, then transcribing the converted file. Selenium devtools is used to capture network requests, associated with click events in order to get audio file links from the request.  The links are then passed to the assemblyAI API to produce transcriptions of the audio files. The transcription is then written to a .txt file.  If the target provides text or transcription data already, then that data is simply located and written to a .txt file. All files produced by the testcases are for testing purposes only.<br>
<h3>Getting Started</h3>
Follow these steps to get started<br>
1.) Clone this repository to your local machine. <br>
2.) cd selenium-transcription <br>
3.) Rename "sample.env" to ".env" in src/test/resources, you will also need an assemblyAI API key<br>
4.) Fill in all the appropriate property values in the .env file<br>
5.) Add the url for any youtube video as a TestNG parameter in the TextNG.xml file<br>
6.) Be sure you have FFmpeg installed<br> 
7.) gradle clean test<br>
