import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TestConfig {
    private List<String> stringList = new ArrayList<>();

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName("TestConfig");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.get(new TestConfig()) instanceof List) {
                List<System> newField = (List<System>) field.get(new TestConfig());

                System.out.println(newField.get(0));
                System.out.println(field.getType().getName());
            }
        }
    }
}
