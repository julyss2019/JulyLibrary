import org.apache.commons.lang3.Validate;
import org.bukkit.Material;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        Validate.inclusiveBetween(Integer.MIN_VALUE, 100, 1000, "T");
    }

    public static int sum(int index, int[] array) {
        if (index != array.length - 1) {
            return array[index] + sum(index + 1, array);
        }

        return array[index];
    }
}
