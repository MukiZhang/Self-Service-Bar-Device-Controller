package Message_;

import java.util.Map;

public class L_respounce {
    String ID;
    String type;
    int state;
    Map<String, Object> data;

    public L_respounce() {
    }

    public L_respounce(String ID, String type, int state, Map<String, Object> data) {
        this.ID = ID;
        this.type = type;
        this.state = state;
        this.data = data;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}


