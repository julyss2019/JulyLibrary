import com.github.julyss2019.mcsp.julylibrary.map.MapBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        @SuppressWarnings("unchecked") Map<String, Integer> map = new MapBuilder<String, Integer>().put("1", 1).put("2", 23).build();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "，" + entry.getValue());
        }

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime()));
    }
}
