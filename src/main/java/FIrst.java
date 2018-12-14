import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FIrst {

    String ip_id_t_file = "/home/jijie/IdeaProjects/first/src/FIles/ip_id_type.txt";
    String respouncse_file = "/home/jijie/IdeaProjects/first/src/FIles/respounce.txt";
    Map<String, String> id_ip = new HashMap();

    public FIrst() {

        id_ip_Map();
        String ip = this.id_ip.get("20001");
        System.out.println(ip);
    }

    public void id_ip_Map() {

        ArrayList<String> lines = readFiles(ip_id_t_file);

        for (String line : lines) {
            String[] arr = line.split("\\s+");
            this.id_ip.put(arr[0], arr[1]);
        }

    }

    public ArrayList<String> readFiles(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File(fileName);

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            while ((tempString = reader.readLine()) != null) {
                lines.add(tempString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return lines;
    }

    public static void main(String[] args) {

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        String jsonString = "{" +
                "\"Command\":\"Dispatch\"," +
                "\"FrameNumber\":\"00\"," +
                "\"Type\":\"Delete\"," +
                "\"Data\":[" +
                "{" +
                "\"DeviceId\":\"123456787654321\"//设备 ID" +
                "}" +
                "]" +
                "}";
        map = gson.fromJson(jsonString, map.getClass());
//        String goodsid=(String) map.get("goods_id");
        System.out.println("map的值为:" + map.toString());

    }
}
