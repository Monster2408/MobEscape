package xyz.mlserver.mobescape.utils.api;

import java.util.ArrayList;
import java.util.List;

public class MainAPI {

    public static String getMinuteTime(int i) {
        i = i / 10;
        int min = i / 60;
        int sec = i % 60;
        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }
    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        MainAPI.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

}
