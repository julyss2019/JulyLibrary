import com.github.julyss2019.mcsp.julylibrary.utils.MathUtil;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(MathUtil.getRandomInteger(1, 5));
        }
    }

    public static int sum(int index, int[] array) {
        if (index != array.length - 1) {
            return array[index] + sum(index + 1, array);
        }

        return array[index];
    }
}
