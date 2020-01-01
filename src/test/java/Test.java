import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.utils.FileUtil;
import org.bukkit.Sound;

import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Sound sound = Sound.AMBIENT_CAVE;

        System.out.println(Sound.class.getSuperclass() == Enum.class);

        for (Sound sound1 : Sound.class.getEnumConstants()) {
            System.out.println(sound1.name());
        }

    }
}
