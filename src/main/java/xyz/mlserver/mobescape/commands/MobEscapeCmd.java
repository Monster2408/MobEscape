package xyz.mlserver.mobescape.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.mlserver.java.Log;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.api.MsgAPI;
import xyz.mlserver.mobescape.utils.bukkit.LocationParser;
import xyz.mlserver.mobescape.utils.game.MobEscapeGUI;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

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

        if (args[0].equalsIgnoreCase("gui")) {
            MobEscapeGUI.open(player);
            return true;
        }

        if (!sender.isOp()) return true;

        Location playerLoc = player.getLocation().clone();
        MobEscapeMap map = MobEscapeMap.getEditMap(player);
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length > 1) {
                String mapName = args[1];
                MobEscapeMap.createMap(player, mapName);
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップ「" + mapName + "」を作成しました。");
                return true;
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップ名を入力してください。");
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("lobby")) {
                    MobEscapeMap.setLobby(playerLoc.clone());
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lロビーを設定しました。");
                } else if (args[1].equalsIgnoreCase("name")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else if (args.length > 2) {
                        map.setName(args[2]);
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップ名を「" + args[2] + "」に設定しました。");
                    } else {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップ名を入力してください。");
                    }
                } else if (args[1].equalsIgnoreCase("arena")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        map.setArenaLobby(playerLoc.clone());
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lロビーを設定しました。");
                    }
                } else if (args[1].equalsIgnoreCase("max")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else if (args.length > 2) {
                        try {
                            int max = Integer.parseInt(args[2]);
                            map.setMaxPlayer(max);
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l最大プレイヤー数を「" + max + "」に設定しました。");
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l数字を入力してください。");
                        }
                    } else {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l最大プレイヤー数を入力してください。");
                    }
                } else if (args[1].equalsIgnoreCase("min")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                        return true;
                    } else if (args.length > 2) {
                        try {
                            int min = Integer.parseInt(args[2]);
                            map.setMinPlayer(min);
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l最小プレイヤー数を「" + min + "」に設定しました。");
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l数字を入力してください。");
                        }
                    } else {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l最小プレイヤー数を入力してください。");
                    }
                } else if (args[1].equalsIgnoreCase("icon")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                        return true;
                    } else {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item == null) sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lアイテムを持ってください。");
                        else {
                            map.setMaterial(item.getType());
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lアイコンを「" + item.getType().name() + "」に設定しました。");
                        }
                    }
                } else if (args[1].equalsIgnoreCase("pos")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                        if (worldEdit == null) {
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lWorldEditがインストールされていません。");
                        } else {
                            LocalSession session = worldEdit.getSession(player);
                            try {
                                Region selection = session.getSelection(session.getSelectionWorld());
                                if (selection != null && selection.getWorld() != null && selection.getMinimumPoint() != null && selection.getMaximumPoint() != null) {
                                    Location pos1 = new Location(
                                            Bukkit.getWorld(selection.getWorld().getName()),
                                            selection.getMinimumPoint().getX(),
                                            selection.getMinimumPoint().getY(),
                                            selection.getMinimumPoint().getZ()
                                    );
                                    Location pos2 = new Location(
                                            Bukkit.getWorld(selection.getWorld().getName()),
                                            selection.getMaximumPoint().getX(),
                                            selection.getMaximumPoint().getY(),
                                            selection.getMaximumPoint().getZ()
                                    );
                                    map.setPos1(pos1);
                                    map.setPos2(pos2);
                                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l範囲を設定しました。");
                                } else {
                                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l範囲を選択してください。");
                                }
                            } catch (IncompleteRegionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else if (args[1].equalsIgnoreCase("pos1")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        map.setPos1(playerLoc.clone());
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l範囲1を設定しました。");
                    }
                } else if (args[1].equalsIgnoreCase("pos2")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        map.setPos2(playerLoc.clone());
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l範囲2を設定しました。");
                    }
                } else if (args[1].equalsIgnoreCase("goal")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
                        if (worldEdit == null) {
                            sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lWorldEditがインストールされていません。");
                        } else {
                            LocalSession session = worldEdit.getSession(player);
                            try {
                                Region selection = session.getSelection(session.getSelectionWorld());
                                if (selection != null && selection.getWorld() != null && selection.getMinimumPoint() != null && selection.getMaximumPoint() != null) {
                                    Location pos1 = new Location(
                                            Bukkit.getWorld(selection.getWorld().getName()),
                                            selection.getMinimumPoint().getX(),
                                            selection.getMinimumPoint().getY(),
                                            selection.getMinimumPoint().getZ()
                                    );
                                    Location pos2 = new Location(
                                            Bukkit.getWorld(selection.getWorld().getName()),
                                            selection.getMaximumPoint().getX(),
                                            selection.getMaximumPoint().getY(),
                                            selection.getMaximumPoint().getZ()
                                    );
                                    map.setGoalPos1(pos1);
                                    map.setGoalPos2(pos2);
                                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lゴール範囲を設定しました。");
                                } else {
                                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l範囲を選択してください。");
                                }
                            } catch (IncompleteRegionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape set <lobby|name|max|min>");
                }
                return true;
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape set <lobby|name|max|min>");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (map == null) {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
            } else if (args.length  > 1) {
                if (args[1].equalsIgnoreCase("spawn")) {
                    map.addSpawn(playerLoc.clone());
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lスポーン地点を追加しました。");
                } else if (args[1].equalsIgnoreCase("path")) {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l中継地点を追加しました。" + ChatColor.LIGHT_PURPLE + "#" + map.getPath().size());
                    map.addPath(playerLoc.clone());
                } else {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape add spawn");
                }
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape add spawn");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (map == null) {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
            } else if (args.length > 1) {
                if (args[1].equalsIgnoreCase("spawn")) {
                    map.deleteSpawn();
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lスポーン地点を削除しました。");
                } else if (args[1].equalsIgnoreCase("path")) {
                    map.deletePath();
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l中継地点を削除しました。");
                } else {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape delete spawn");
                }
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape delete spawn");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("map")) {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップ一覧");
                    for (int id : MobEscapeMap.getMapHashMap().keySet()) {
                        MobEscapeMap map1 = MobEscapeMap.getMapHashMap().get(id);
                        if (map1 == null) {
                            continue;
                        }
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l" + id + ": " + map1.getName());
                    }
                } else if (args[1].equalsIgnoreCase("spawn")) {
                    if (map == null) {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§l編集中のマップがありません。");
                    } else {
                        sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lスポーン地点");
                    }
                } else {
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape list <map|spawn>");
                }
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方が間違っています。");
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lコマンドの使い方: /mobescape list <map|spawn>");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("save")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("confirm")) {
                    MobEscapeMap.saveMap();
                    sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップを保存しました。");
                }
            } else {
                sender.sendMessage("§c§l[§4§lMobEscape§c§l] §f§lマップを保存するには、/mobescape save confirm を実行してください。");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("edit")) {
            MobEscapeGUI.openEdit(map, player);
        }
        return false;
    }
}
