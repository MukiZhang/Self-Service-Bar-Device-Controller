package Message_;

import Devices.BasicFunction;

import java.util.*;

/**
 * this class is used to refresh the recent info in gas table and electric table
 *
 * @author Ji Jie
 * @version 1.0
 */
public class Refresh extends BasicFunction {

    public Refresh() throws Exception {
//        getLightState();
        getStation();
    }

    /**
     * this method is used to refresh electric table info in static time
     *
     * @throws Exception
     */

    public void getStation() throws Exception {
        // TODO Auto-generated method stub
        int wait = 60 * 5;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getLightState();
            }
        }, wait * 1000, wait * 1000);

    }

    void getLightState() {
        //get Devices INFO
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        ArrayList<String> lines = readFiles(ip_id_t_file);
        ArrayList<ip_id_type> id_ip = StrToip_id(lines);
        for (ip_id_type dp : id_ip) {
            Map<String, String> map = new HashMap<>();
            map.put("Ip", dp.getIP());
            map.put("DeviceId", dp.getDeviceID());
            map.put("port", String.valueOf(dp.getPort()));
            map.put("Id", String.valueOf(dp.getDeviceNo()));
            maps.add(map);
        }
        //遍历
        for (Map<String, String> m : maps) {
            int sta = 0;
            String st = "";
            Map<String, String> reM = new HashMap<>();
            String sMes = new light_getM().getStateByGate(m.get("DeviceId"));
            String resp = new Clinet().unicast(m.get("Ip"), Integer.valueOf(m.get("port")), sMes);

            Map<String, Object> mess = new light_getM().StringToMap(resp);

            ArrayList<Map<String, String>> arrayList = (ArrayList<Map<String, String>>) mess.get("Data");
            reM.put("DeviceId", arrayList.get(0).get("DeviceId"));
            for (Map<String, String> ass : arrayList) {
                reM.put(ass.get("Key"), ass.get("Value"));
            }
            if (Integer.valueOf(reM.get("ColorTemperature")) <= 6500 && Integer.valueOf(reM.get("ColorTemperature")) >= 2700) {
                sta = 1;
            }
            if (Integer.valueOf(reM.get("Luminance")) <= 100 && Integer.valueOf(reM.get("Luminance")) >= 0) {
                sta = 1 * 10 + sta;
            }
            if (Integer.valueOf(reM.get("Switch")) == 1 || Integer.valueOf(reM.get("Switch")) == 0) {
                sta = 1 * 100 + sta;
            }

            switch (sta) {
                case 0: {//S T L x
                    st = "0";
                    break;
                }
                case 1: {//S L x
                    st = "1";
                    break;
                }
                case 10: {//S x
                    st = "2";
                    break;
                }
                case 11: {//S x
                    st = "4";
                    break;
                }
                case 100: {//T L x
                    st = "3";
                    break;
                }
                case 111: {
                    st = "7";
                    break;
                }
                case 101: {//L x
                    st = "5";
                    break;
                }
                case 110: {//L x
                    st = "6";
                    break;
                }
            }

            String respounce = m.get("Id") + " " + "light" + " " + st + " " + reM.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(respounce, respouncse_file);
        }
    }

}