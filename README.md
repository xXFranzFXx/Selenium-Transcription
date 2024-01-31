
<h1>Selenium Automation Testing</h1><br>


<h3>Test Subject</h3>
https://empathtrauma.com<br>
https://easyyesleads.com<br>

<h3>Programming Language</h3>
Java <br>

<h3>Libraries/Tools Used</h3> 
* Selenium - Web automation<br>
* Selenium devtools - capturing network requests<br>
* TestNG - Test execution<br>
* REST Assured - Validation of REST web services<br>
* log4j - Capturing logs<br>
* Extent Reports - reporting<br>
* Gradle - Build and package management<br>
* assemblyAI - transcribing audio files<br>
<h3>Overview</h3>
This project demonstrates automation testing with iframe elements to locate audio data that can be used to create transcripts. Selenium devtools is used to capture network requests, associated with click events in order to get audio file links from the request.  The links are then passed to the assemblyAI API to produce transcriptions of the audio files.  If the target provides text or transcription data already, then that data is simply located and written to a text file.
A third method that is used is by sending a request with restassured and then writing that response to a text file.  I also wanted to practice testing a page that contains elements that lazy-load.  The lazy loaded elements are also contained within the iframe, making it a bit more challenging (for me) to test. 
<h3>Getting Started</h3>
Follow these steps to get started with the Koel Test Automation Framework:<br><br>
1.) Clone this repository to your local machine. <br>
2.) cd selenium-automation-testing <br>
3.) Rename "sample.env" to ".env" in src/test/resources <br>
4.) Fill in all the appropriate property values in the .env file<br>
5.) gradle clean test<br>
