import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        FileLogger fileLogger = new FileLogger.Builder().autoFlush(true).fileName("%d{yyyy-MM-dd}").loggerFolder(new File(".")).build();

        fileLogger.d("test");
    }
}
