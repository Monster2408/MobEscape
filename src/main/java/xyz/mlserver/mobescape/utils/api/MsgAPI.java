package xyz.mlserver.mobescape.utils.api;

import xyz.mlserver.mobescape.MobEscape;

public class MsgAPI {
    private static String NOT_USE_CONSOLE_MSG = "コンソールからは実行できません。";
    private static String WRONG_COMMAND_MSG = "コマンドの使い方が間違っています。";


    public static String getNotUseConsoleMsg() {
        return NOT_USE_CONSOLE_MSG;
    }

    public static void setNotUseConsoleMsg(String msg) {
        NOT_USE_CONSOLE_MSG = msg;
    }

    public static String getWrongCommandMsg() {
        return WRONG_COMMAND_MSG;
    }

    public static void setWrongCommandMsg(String msg) {
        WRONG_COMMAND_MSG = msg;
    }



    public static void loadMsgFile() {
        MobEscape.messageYml.reloadConfig();
        setNotUseConsoleMsg(MobEscape.messageYml.getConfig().getString("NOT_USE_CONSOLE", "&cコンソールからこのコマンドを使用することはできません！"));
        setWrongCommandMsg(MobEscape.messageYml.getConfig().getString("WRONG_COMMAND", "&cコマンドの使い方が間違っています。"));
    }
}
