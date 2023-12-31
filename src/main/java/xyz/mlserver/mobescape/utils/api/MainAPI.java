package xyz.mlserver.mobescape.utils.api;

public class MainAPI {

    static String getMinuteTime(int i) {
        int min = i / 60;
        int sec = i % 60;
        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }

    static String getMinuteTime(double d) {
        int i = (int) d;
        int min = i / 60;
        int sec = i % 60;
        int msec = (int) ((d - i) * 10);
        return String.format("%02d", min) + ":" + String.format("%02d", sec) + "." + msec;
    }

    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        MainAPI.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

}
