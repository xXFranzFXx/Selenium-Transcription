package util;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviderUtil {

    @DataProvider(name="YoutubeData")
    public Object[][] getYoutubeData(Method method){
        if(method.getName().equalsIgnoreCase("writeClosedCaptionsToFileAsync")) {
        return new Object[][]{
                //put youtube videos that contain ads here
                {"https://www.youtube.com/watch?v=TdwwPP9oW4c", "youtubeCC"}
        };
        } else {
            return new Object[][] {
                    //put youtube videos that don't contain ads here
//                    {"https://www.youtube.com/watch?v=cc26zFE8X1k"},
                    {"https://www.youtube.com/watch?v=TdwwPP9oW4c"}

            };
        }
    }
}
