package Message_;

public class Respounce {
    int deviceNo;
    String type;
    int result;
    String respounceInfo;
    int reType;
    String date;

    public Respounce() {
    }

    public Respounce(int deviceNo, String type, int result, String respounceInfo, int reType, String date) {
        this.deviceNo = deviceNo;
        this.type = type;
        this.result = result;
        this.respounceInfo = respounceInfo;
        this.reType = reType;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRespounceInfo() {
        return respounceInfo;
    }

    public void setRespounceInfo(String respounceInfo) {
        this.respounceInfo = respounceInfo;
    }

    public int getReType() {
        return reType;
    }

    public void setReType(int reType) {
        this.reType = reType;
    }

    @Override
    public String toString() {
        return deviceNo + " " + type + " " + result + " " + respounceInfo + " " + reType;
    }
}
