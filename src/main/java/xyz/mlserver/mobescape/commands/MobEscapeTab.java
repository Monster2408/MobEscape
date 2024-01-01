package xyz.mlserver.mobescape.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MobEscapeTab implements TabCompleter {

    private static final List<String> FIRST_CMD_ARGS = Arrays.asList("gui", "join", "leave");
    private static final List<String> FIRST_CMD_ARGS_OP = Stream.of(Arrays.asList("create", "set", "add", "delete"), FIRST_CMD_ARGS).flatMap(Collection::stream).collect(Collectors.toList());

    private static final List<String> SET_CMD_ARGS = Arrays.asList("lobby", "arena", "max", "min", "under", "undery", "icon", "pos", "pos1", "pos2", "goal");
    private static final List<String> ADD_CMD_ARGS = Arrays.asList("spawn", "path");
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> cmdList = new ArrayList<>();
        if (!sender.isOp()) {
            if (args.length == 1) {
                for (String arg : FIRST_CMD_ARGS) if (arg.contains(args[0])) cmdList.add(arg);
                return cmdList;
            }
            return null;
        }
        if(args.length >= 1){
            if (args.length == 1) {
                for (String arg : FIRST_CMD_ARGS_OP) if (arg.contains(args[0])) cmdList.add(arg);
            } else {
                if ("set".contains(args[0])) {
                    for (String arg : SET_CMD_ARGS) if (arg.contains(args[1])) cmdList.add(arg);
                }
                if ("add".contains(args[0])) {
                    for (String arg : ADD_CMD_ARGS) if (arg.contains(args[1])) cmdList.add(arg);
                }
                if ("delete".contains(args[0])) {
                    for (String arg : ADD_CMD_ARGS) if (arg.contains(args[1])) cmdList.add(arg);
                }
            }
            return cmdList;
        }
        return null;
    }

}
