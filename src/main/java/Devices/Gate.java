package Devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gate extends Device {
    public ArrayList<Map<String, String>> searchD(ArrayList<String> infos) {
        ArrayList<Map<String, String>> nweIDs = new ArrayList<>();

        return nweIDs;
    }

    public boolean AddNewD() {
        ArrayList<String> infos = new ArrayList<>();
        ArrayList<Map<String, String>> ids = searchD(infos);

        return true;
    }


    public boolean delateD(String ids) {

        return true;
    }

    public boolean openD(String ids) {

        return true;
    }

    public boolean closeD(String ids) {
        return true;
    }

    public Map delateDs(ArrayList<String> ids) {
        Map<String, Boolean> result = new HashMap<>();
        return result;
    }

    public Map openDs(ArrayList<String> ids) {
        Map<String, Boolean> result = new HashMap<>();
        return result;
    }

    public Map closeDs(ArrayList<String> ids) {
        Map<String, Boolean> result = new HashMap<>();
        return result;
    }

    public int generateResult(String message) {
        int result = 0;
        return result;
    }
}