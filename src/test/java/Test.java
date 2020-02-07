import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.Tab;

public class Test {
    public static void main(String[] args) {
        Tab tab = new Tab("test");

        tab.addSubTab(new Tab("t1").addSubTab(new Tab("t")).addSubTab(new Tab("tt")));
        tab.addSubTab(new Tab("t2"));

        System.out.println(tab.toString());
    }
}
