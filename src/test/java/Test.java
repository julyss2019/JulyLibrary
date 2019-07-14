import com.github.julyss2019.mcsp.julylibrary.skull.CacheSkullManager;
import com.github.julyss2019.mcsp.julylibrary.utils.FileUtil;

import java.io.File;

public class Test {
    private static CacheSkullManager cacheSkullManager = new CacheSkullManager();

    public static void main(String[] args) {
        System.out.println(FileUtil.getFileName(new File("plugin.yml")));
    }
}
