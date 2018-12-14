package Devices;

import Message_.Respounce;
import Message_.gateWay;
import Message_.ip_id_type;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicFunction {
    public String gateWay_file = "/home/jijie/IdeaProjects/first/src/FIles/gateWay.txt";
    public String ip_id_t_file = "/home/jijie/IdeaProjects/first/src/FIles/ip_id_type.txt";
    public String respouncse_file = "/home/jijie/IdeaProjects/first/src/FIles/respounce.txt";

    public BasicFunction() {
    }

    public void WriteFiles_append(String line, String fileName) {

        FileWriter fw = null; //
        try {
            File file = new File(fileName);
            fw = new FileWriter(file, true);//
            fw.write(line + "\r\n");
            fw.flush();//

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //
            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> id_ip(String id, String type) {
        Map<String, String> map = new HashMap<>();
        ArrayList<String> lines = readFiles(ip_id_t_file);
        ArrayList<ip_id_type> id_ip = StrToip_id(lines);
        for (ip_id_type dp : id_ip) {
            if (dp.getDeviceNo() == Integer.valueOf(id) && dp.getType().contains(type)) {
                map.put("Ip", dp.getIP());
                map.put("DeviceId", dp.getDeviceID());
                map.put("port", String.valueOf(dp.getPort()));

            }
        }
        return map;
    }

    public int generateResult(String message, int type) {
        int result = 0;
        return result;
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

    public void WriteFiles(ArrayList<String> lines, String fileName) {

        FileWriter fw = null; //
        try {
            File file = new File(fileName);
            fw = new FileWriter(file);//

            for (int i = 0; i < lines.size(); i++) {
                fw.write(lines.get(i) + "\r\n");
                fw.flush();//
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Delete(String ID, int type1, String type_d) {
        int i = 0;
        ArrayList<String> lines = new ArrayList<>();
        String fileName = "";
        switch (type1) {
            case 1: {
                fileName = ip_id_t_file;
                lines = readFiles(fileName);
                for (String line : lines) {
                    if (line.contains(ID))
                        lines.remove(i);
                    i++;
                }
                break;
            }
            case 2: {
                fileName = respouncse_file;
                lines = readFiles(fileName);

                String Did = id_ip(ID, type_d).get("DeviceId");

                if (Did != "") {
                    for (String line : lines) {
                        if (line.contains(Did))
                            lines.remove(i);
                        i++;
                    }
                }

                break;
            }
        }

        WriteFiles(lines, fileName);

    }

    public ArrayList<ip_id_type> StrToip_id(ArrayList<String> strs) {
        ArrayList<ip_id_type> arrayList = new ArrayList<>();

        for (String str : strs) {
            String arr[] = str.split(" ");
            String info[] = new String[5];
            int i = 0;
            for (String a : arr) {
                if (a.length() != 0) {
                    info[i] = a;
                    i++;
                }
            }
            ip_id_type pdt = new ip_id_type(Integer.valueOf(info[0]), info[1], Integer.valueOf(info[2])
                    , info[3], info[4]);
            arrayList.add(pdt);
        }
        return arrayList;
    }

    public ArrayList<gateWay> StrToGateWay(ArrayList<String> strs) {
        ArrayList<gateWay> arrayList = new ArrayList<>();

        for (String str : strs) {
            String arr[] = str.split(" ");
            String info[] = new String[5];
            int i = 0;
            for (String a : arr) {
                if (a.length() != 0) {
                    info[i] = a;
                    i++;
                }
            }
            gateWay pdt = new gateWay(info[0], info[1], Integer.valueOf(info[2])
                    , info[3]);
            arrayList.add(pdt);
        }
        return arrayList;
    }

    public ArrayList<Respounce> StrToResp(ArrayList<String> strs) {
        ArrayList<Respounce> arrayList = new ArrayList<>();

        for (String str : strs) {
            String arr[] = str.split(" ");
            String info[] = new String[6];
            int i = 0;
            for (String a : arr) {
                if (a.length() != 0) {
                    info[i] = a;
                    i++;
                }
            }
            Respounce pdt = new Respounce(Integer.valueOf(info[0]), info[1],
                    Integer.valueOf(info[2])
                    , info[3], Integer.valueOf(info[4]), info[5]);
            arrayList.add(pdt);
        }
        return arrayList;
    }

}
