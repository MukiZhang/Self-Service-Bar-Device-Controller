package Devices;

import Message_.State.Refresh;

public class Init {
    public Init() throws Exception {
        Light l = new Light();
        new Refresh();
        l.init();
    }
}
