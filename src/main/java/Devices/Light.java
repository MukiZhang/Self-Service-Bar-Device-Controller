package Devices;

import Message_.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Light extends Device {
    static private String TYPE = "light";

    public int init() throws IOException {
        int resu = 1;
        String host = "255.255.255.255";// 广播地址
        int port = 8000;// 广播的目的端口

        String sMessage = "{\"Command\":\"RequestTcp\"}";
        Clinet c = new Clinet();
        String resMes = c.BroadCast(host, port, sMessage).replace("\n", "").replace("\t", "");
        if (resMes == null) {
            resu = 0;
        } else {
            Map<String, Object> result = new light_getM().StringToMap(resMes);
            String gateWay = "G001" + " " + result.get("Ip") + " " + result.get("Port") + " " + "Light" + " " + result.toString().replace(" ", "-");
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(gateWay);

            WriteFiles(arrayList, gateWay_file);
        }
        return resu;
    }

    public boolean AddNewD() {
        String type = "light";
        int deNo = 10001;
        ArrayList<String> infos = readFiles(ip_id_t_file);//现有的deviceId
        ArrayList<Map<String, String>> ids = searchD(infos);
        ArrayList<ip_id_type> pdts = StrToip_id(infos);

        if (ids.isEmpty()) {
            System.out.println("No new devices added");
            return false;
        } else {
            if (!infos.isEmpty()) {
                int max = deNo;
                for (ip_id_type p : pdts) {
                    if (p.getType().contains(type) && p.getDeviceNo() > max) {
                        max = p.getDeviceNo();
                    }
                }
                deNo = max;
            }
            for (Map<String, String> id : ids) {
                ip_id_type ipd = new ip_id_type(deNo, id.get("Ip"), Integer.valueOf(id.get("port")), id.get("DeviceId"), type);
                WriteFiles_append(ipd.toString(), ip_id_t_file);
                deNo++;

            }
            return true;
        }


    }

    @Override
    public ArrayList<Map<String, String>> searchD(ArrayList<String> infos) {

        ArrayList<Map<String, String>> newIDs = new ArrayList<>();


        ArrayList<Map<String, String>> IDs = new ArrayList<>();

        String sendM = new light_getM().deviceList();
        ArrayList<String> gateWay_infos = readFiles(gateWay_file);
        ArrayList<gateWay> gateWays = StrToGateWay(gateWay_infos);
        ArrayList<Map<String, String>> IPs_ports = new ArrayList<>();//现有的port

        for (gateWay g : gateWays) {
            Map<String, String> i_p = new HashMap<>();
            i_p.put("Ip", g.getIp());
            i_p.put("port", String.valueOf(g.getPort()));
            IPs_ports.add(i_p);
        }
        // search devices in the gateway
        for (Map<String, String> m : IPs_ports) {
            String message = new Clinet().unicast(m.get("Ip"), Integer.valueOf(m.get("port")), sendM);
//            System.out.println(message);
            Map<String, Object> deviceMessage = new light_getM().StringToMap(message);
            int count = Integer.valueOf(deviceMessage.get("TotalNumber").toString());
            ArrayList<Map<String, String>> Data = (ArrayList<Map<String, String>>) deviceMessage.get("Data");

            for (int i = 0; i < count; i++) {
                Map<String, String> id = new HashMap<>();
                id.put("Ip", m.get("Ip"));
                id.put("port", m.get("port"));
                id.put("DeviceId", Data.get(i).get("DeviceId"));
                IDs.add(id);
            }
        }
//        System.out.println("IDS"+IDs.toString());
        if (infos == null) {
            newIDs = IDs;
        } else {
            ArrayList<ip_id_type> pdts = StrToip_id(infos);
            for (Map<String, String> id : IDs) {
                int count = 0;
                for (ip_id_type p : pdts) {

                    if (id.get("DeviceId").contains(p.getDeviceID()) && id.get("Ip").contains(p.getIP())) {
                        count++;
                    }
                    if (count != 0)
                        break;
                }
                if (count == 0)
                    newIDs.add(id);
            }
        }
        return newIDs;
    }

    public boolean delateD(String ids) {
        Delete(ids, 1, "light");
        return true;
    }

    public boolean openD(String ids) {
        int type = 2;
        int value = 1;
        Map<String, String> map = id_ip(ids, TYPE);
        String deviceID = map.get("DeviceId");
        String ip = map.get("Ip");
        int port = Integer.valueOf(map.get("port"));

        String sMes = new light_getM().ColTemp_Swi_Lum(deviceID, value, type);

        String res = new Clinet().unicast(ip, port, sMes);
        Map res_m = new light_getM().StringToMap(res);
        ArrayList<Map<String, String>> arrayList = (ArrayList<Map<String, String>>) res_m.get("Data");
        Map<String, String> data = arrayList.get(0);
        Map<String, String> recentS = getRecentState(Integer.valueOf(ids), TYPE);
        if (Integer.valueOf(data.get("Value")) == value) {
            recentS.put("Switch", "1");
            String write = ids + " " + TYPE + " " + "1" + " " + recentS.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            write = ids + " " + TYPE + " " + "1" + " " + res_m.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            return true;
        } else {
            String write = ids + " " + TYPE + " " + "4" + " " + recentS.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            String write1 = ids + " " + TYPE + " " + "0" + " " + res_m.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write1, respouncse_file);
            return false;
        }

    }

    public boolean closeD(String ids) {
        int type = 2;
        int value = 0;
        Map<String, String> map = id_ip(ids, TYPE);
        String deviceID = map.get("DeviceId");
        String ip = map.get("Ip");
        int port = Integer.valueOf(map.get("port"));

        String sMes = new light_getM().ColTemp_Swi_Lum(deviceID, value, type);

        String res = new Clinet().unicast(ip, port, sMes);
        Map res_m = new light_getM().StringToMap(res);

        ArrayList<Map<String, String>> arrayList = (ArrayList<Map<String, String>>) res_m.get("Data");
        Map<String, String> data = arrayList.get(0);
        Map<String, String> recentS = getRecentState(Integer.valueOf(ids), TYPE);
        if (Integer.valueOf(data.get("Value")) == value) {
            recentS.put("Switch", "0");
            String write = ids + " " + TYPE + " " + "1" + " " + recentS.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            String write1 = ids + " " + TYPE + " " + "1" + " " + res_m.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write1, respouncse_file);
            return true;
        } else {
            String write = ids + " " + TYPE + " " + "4" + " " + recentS.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            write = ids + " " + TYPE + " " + "0" + " " + res_m.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();
            WriteFiles_append(write, respouncse_file);
            return false;
        }

    }

    /**
     * @param light_Per Map<String,String> id(10001),temp,lumi
     * @return res 0 both fail 3 both successful 2 t-s l-f  1 t-f l-s
     */
    public int controlBrAndCoL(Map<String, String> light_Per) {
        int res = 0;
        int type_t = 1;
        int value_t = Integer.valueOf(light_Per.get("temp"));
        int type_l = 3;
        int value_l = Integer.valueOf(light_Per.get("lumi"));
        Map<String, String> map = id_ip(light_Per.get("id"), TYPE);
        String deviceID = map.get("DeviceId");
        String ip = map.get("Ip");
        int port = Integer.valueOf(map.get("port"));
        String sMes_l = new light_getM().ColTemp_Swi_Lum(deviceID, value_l, type_l);
        String sMes_t = new light_getM().ColTemp_Swi_Lum(deviceID, value_t, type_t);
        String res_l = new Clinet().unicast(ip, port, sMes_l);

        Map<String, String> recentS = getRecentState(Integer.valueOf(light_Per.get("id")), TYPE);
        recentS.put("Switch", "0");

        Map res_m_l = new light_getM().StringToMap(res_l);
//
        /**
         *
         */
        ArrayList<Map<String, String>> ass = (ArrayList<Map<String, String>>) res_m_l.get("Data");
        Map<String, String> data = ass.get(0);
        String write = "";
        if (Integer.valueOf(data.get("Value")) == value_l) {
            recentS.put("Luminance", String.valueOf(value_l));
            write = map.get("DeviceId") + " " + TYPE + " " + "1" + " " + res_m_l.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();
            res++;
        } else
            write = map.get("DeviceId") + " " + TYPE + " " + "0" + " " + res_m_l.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();

        WriteFiles_append(write, respouncse_file);
        String res_t = new Clinet().unicast(ip, port, sMes_t);
        Map res_m_t = new light_getM().StringToMap(res_t);
        ass = (ArrayList<Map<String, String>>) res_m_t.get("Data");
        Map<String, String> data_t = ass.get(0);

        if (Integer.valueOf(data_t.get("Value")) == value_t) {
            recentS.put("ColorTemperature", String.valueOf(value_t));
            write = map.get("DeviceId") + " " + TYPE + " " + "1" + " " + res_m_t.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();

            res += 2;
        } else
            write = map.get("DeviceId") + " " + TYPE + " " + "1" + " " + res_m_t.toString().replace(" ", "") + " " + "2" + " " + new DateTransform().getCurrenTime();

        WriteFiles_append(write, respouncse_file);

        write = map.get("DeviceId") + " " + TYPE + " " + "8" + " " + recentS.toString().replace(" ", "") + " " + "1" + " " + new DateTransform().getCurrenTime();
        WriteFiles_append(write, respouncse_file);
        /**
         *
         */
        return res;
    }

    public Map delateDs(ArrayList<String> ids) {
        Map<String, Boolean> results = new HashMap<>();
        for (String id : ids) {
            Boolean result = delateD(id);
            results.put(id, result);
        }
        return results;
    }

    public Map openDs(ArrayList<String> ids) {
        Map<String, Boolean> results = new HashMap<>();
        for (String id : ids) {
            Boolean result = openD(id);
            results.put(id, result);
        }
        return results;
    }

    public Map closeDs(ArrayList<String> ids) {
        Map<String, Boolean> results = new HashMap<>();
        for (String id : ids) {
            Boolean result = closeD(id);
            results.put(id, result);
        }
        return results;
    }

    /**
     * @param light_Pers: id(10001),temp,lumi
     * @return
     */
    public Map<String, Integer> controlBrAndCoLs(ArrayList<Map<String, String>> light_Pers) {

        Map<String, Integer> results = new HashMap<>();
        for (Map<String, String> light_P : light_Pers) {
            int result = controlBrAndCoL(light_P);
            results.put(light_P.get("id").toString(), result);
        }
        return results;
    }

    public Map<String, String> getRecentState(int id, String type) {
        Map<String, String> State = new HashMap<>();
        ArrayList<Respounce> respounces = StrToResp(readFiles(respouncse_file));
        String infos = "";
        for (Respounce res : respounces) {
            if (res.getDeviceNo() == id && res.getType().contains(type) && res.getReType() == 1) {
                infos = res.getRespounceInfo();
            }
        }
        Pattern p1 = Pattern.compile("\\w+=\\w+");
        Matcher m1 = p1.matcher(infos);
        while (m1.find()) {

            if (m1.group(0) != null) {
                String str[] = new String[2];
                int i = 0;
                String arr[] = m1.group(0).split("=");
                for (String a : arr) {
                    if (!a.isEmpty()) {
                        str[i] = a;
                        i++;
                    }
                }
                State.put(str[0], str[1]);
            }
        }
        System.out.println(State.toString());
        return State;
    }

}
