import com.github.julyss2019.mcsp.julylibrary.config.JulyConfig;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import com.github.julyss2019.mcsp.julylibrary.text.PlaceholderContainer;

import java.util.List;

public class Test {
    private List<String> list;

    public static void main(String[] args) throws NoSuchFieldException {

        System.out.println(JulyText.setPlaceholders("3331$${yh}3131", new PlaceholderContainer().add("yh", ":")));
    }
}
