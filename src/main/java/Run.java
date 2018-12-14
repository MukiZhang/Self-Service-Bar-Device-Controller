import Devices.Light;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Run {

    public static void main(String[] args) throws IOException {
        Light l = new Light();
        Map<String, String> light_Per = new HashMap<>();
        light_Per.put("id", "10001");
        light_Per.put("temp", "2700");//min 100 递增
        light_Per.put("lumi", "50");// min 10 递增

        l.init();
        l.AddNewD();
//        System.out.println(l.closeD("10001"));
//         System.out.println(l.openD("10001"));
//        System.out.println(l.controlBrAndCoL(light_Per));
        l.getRecentState(10001, "light");
    }
}
