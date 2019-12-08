import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.utils.FileUtil;

import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println(FileUtil.getFileName(new File("plugin.yml")));
    }
}
