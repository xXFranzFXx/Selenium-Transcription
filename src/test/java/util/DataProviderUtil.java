package util;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviderUtil {

    @DataProvider(name="YoutubeData")
    public Object[][] getYoutubeData(Method method){
        if(method.getName().equalsIgnoreCase("writeClosedCaptionsToFileAsync")) {
        return new Object[][]{
                {"https://www.youtube.com/watch?v=cc26zFE8X1k", "youtubeCC"}
        };
        } else {
            return new Object[][] {
                    {"https://www.youtube.com/watch?v=cc26zFE8X1k"}
            };
        }
    }
}
