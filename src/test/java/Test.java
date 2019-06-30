import com.github.julyss2019.mcsp.julylibrary.skull.CacheSkullManager;

public class Test {
    private static CacheSkullManager cacheSkullManager = new CacheSkullManager();

    public static void main(String[] args) {
        for (int i = 1000; i < 2000; i++) {
            cacheSkullManager.loadTexture("" + i);
        }
        System.out.println("t");
    }
}
