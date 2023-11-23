package xyz.mlserver.mobescape.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlserver.java.Log;
import xyz.mlserver.mobescape.utils.api.MsgAPI;

public class MobEscapeCmd implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Log.error(MsgAPI.getNotUseConsoleMsg());
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
            player.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape <join|leave|start|stop|setspawn|setarena|setlobby");
            return true;
        }
        return false;
    }
}
