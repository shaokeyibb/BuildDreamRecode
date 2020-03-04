package cn.lingyuncraft.builddreamrecode.manage;

import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.features.FakeDream;
import cn.lingyuncraft.builddreamrecode.features.PublicShop;
import cn.lingyuncraft.builddreamrecode.utils.*;
import com.sk89q.worldedit.WorldEditException;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandHandler implements CommandExecutor {

    @SneakyThrows
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("builddream")) {

            if (args.length == 0) {
                return false;
            }

            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(
                        "--------筑梦系统使用帮助--------\n" +
                                "以下指令中，[]为必填参数，<>为选填参数\n" +
                                "/builddream help --- 显示使用帮助\n" +
                                "/builddream create [公众ID] <梦境描述> <筑梦市场授权费用> --- 筑梦，当填写“筑梦市场授权费用”时即视为公开梦境到梦境商店\n" +
                                "/builddream release [公众ID] --- 释放梦境\n" +
                                "/builddream snapshot [公众ID] --- 释放梦境预览\n" +
                                "/builddream list <玩家名称> --- 检索自己或他人的所有梦境\n" +
                                "/builddream shop --- 打开筑梦市场\n" +
                                "------------------------------"
                );
                return true;
            } else if (args[0].equalsIgnoreCase("release")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("筑梦系统>> 你不能在控制台运行该指令");
                    return true;
                } else {
                    if (!(sender.hasPermission("BuildDream.use"))) {
                        sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                        return true;
                    } else {
                        Player player = (Player) sender;
                        if (args.length == 1) {
                            sender.sendMessage("筑梦系统>> 请填写公众ID");
                            return false;
                        } else {
                            if (!PublicID.hasPublicID(args[1])) {
                                player.sendMessage("筑梦系统>> 指定的公众ID不存在");
                            } else {
                                try {
                                    DreamControl.releaseDream(args[1], player.getUniqueId());
                                } catch (IOException | InvalidConfigurationException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("筑梦系统>> 你不能在控制台运行该指令");
                    return true;
                } else {
                    if (!(sender.hasPermission("BuildDream.use"))) {
                        sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                        return true;
                    } else {
                        Player player = (Player) sender;
                        if (TempStorage.getTempPos1().get(((Player) sender).getUniqueId()) == null || TempStorage.getTempPos1().get(((Player) sender).getUniqueId()) == null) {
                            sender.sendMessage("筑梦系统>> 请先用木剑完成圈点后再筑梦");
                        } else {
                            switch (args.length) {
                                case 1:
                                    sender.sendMessage("筑梦系统>> 请填写公众ID");
                                    return false;
                                case 2:
                                    if (PublicID.checkPublicID(args[1]) == 1) {
                                        player.sendMessage("筑梦系统>> 公众ID已被占用，请更换一个再试");
                                    } else if (PublicID.checkPublicID(args[1]) == 2) {
                                        player.sendMessage("筑梦系统>> 公众ID含有非法字符，请检查后重新输入!");
                                    } else {
                                        try {
                                            DreamControl.buildDream(args[1], player.getWorld(), TempStorage.getTempPos1().get(player.getUniqueId()), TempStorage.getTempPos2().get(player.getUniqueId()), player.getUniqueId(), "这个人很懒，什么都没有描述", false, 0);
                                        } catch (WorldEditException | IOException | InvalidConfigurationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                case 3:
                                    if (PublicID.checkPublicID(args[1]) == 1) {
                                        player.sendMessage("筑梦系统>> 公众ID已被占用，请更换一个再试");
                                    } else if (PublicID.checkPublicID(args[1]) == 2) {
                                        player.sendMessage("筑梦系统>> 公众ID含有非法字符，请检查后重新输入!");
                                    } else {
                                        try {
                                            DreamControl.buildDream(args[1], player.getWorld(), TempStorage.getTempPos1().get(player.getUniqueId()), TempStorage.getTempPos2().get(player.getUniqueId()), player.getUniqueId(), args[2], false, 0);
                                        } catch (WorldEditException | IOException | InvalidConfigurationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                default:
                                    if (PublicID.checkPublicID(args[1]) == 1) {
                                        player.sendMessage("筑梦系统>> 公众ID已被占用，请更换一个再试");
                                    } else if (PublicID.checkPublicID(args[1]) == 2) {
                                        player.sendMessage("筑梦系统>> 公众ID含有非法字符，请检查后重新输入!");
                                    } else {
                                        try {
                                            DreamControl.buildDream(args[1], player.getWorld(), TempStorage.getTempPos1().get(player.getUniqueId()), TempStorage.getTempPos2().get(player.getUniqueId()), player.getUniqueId(), args[2], true, Double.parseDouble(args[3]));
                                        } catch (WorldEditException | IOException | InvalidConfigurationException e) {
                                            e.printStackTrace();
                                        } catch (NumberFormatException e) {
                                            player.sendMessage("筑梦系统>> 参数有误，请检查参数是否正确");
                                            return false;
                                        }
                                    }
                            }
                        }
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("shop")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage("筑梦系统>> 你不能在控制台运行该指令");
                    return true;
                } else {
                    if (!(sender.hasPermission("BuildDream.use"))) {
                        sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                        return true;
                    } else {
                        if (!Configuration.isEnablePublicShop()) {
                            sender.sendMessage("筑梦系统>> 筑梦商店未开放");
                            return true;
                        } else {
                            PublicShop.sendGUI((Player) sender);
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (!(sender.hasPermission("BuildDream.use"))) {
                    sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                } else {
                    switch (args.length) {
                        case 1:
                            if (!(sender instanceof Player)) {
                                sender.sendMessage("筑梦系统>> 在控制台中不能查询自己的梦境");
                            } else {
                                sender.sendMessage(((Player) sender).getDisplayName() + "筑梦系统>> 已筑梦的梦境列表");
                                for (String ID : Storage.getDreamList()) {
                                    YamlConfiguration file = new YamlConfiguration();
                                    try {
                                        file.load(Storage.getStorageYAMLFile(ID));
                                    } catch (IOException | InvalidConfigurationException e) {
                                        e.printStackTrace();
                                    }
                                    if (file.getString("author").equals(((Player) sender).getUniqueId().toString())) {
                                        sender.sendMessage(ID + "\n释放梦境费用:" + file.getString("Cost") + "\n描述:" + file.getString("Description") + "\n----------");
                                    }
                                }
                            }
                            break;
                        case 2:
                            if (!sender.hasPermission("Builddream.admin.check")) {
                                sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                            } else {
                                if (Bukkit.getPlayer(args[1]) == null) {
                                    sender.sendMessage("筑梦系统>> 指定玩家不存在");
                                } else {
                                    sender.sendMessage("筑梦系统>> " + Bukkit.getPlayer(args[1]).getDisplayName() + "已筑梦的梦境列表");
                                    for (String ID : Storage.getDreamList()) {
                                        YamlConfiguration file = new YamlConfiguration();
                                        try {
                                            file.load(Storage.getStorageYAMLFile(ID));
                                        } catch (IOException | InvalidConfigurationException e) {
                                            e.printStackTrace();
                                        }
                                        if (file.getString("author").equals(Bukkit.getPlayer(args[1]).getUniqueId().toString())) {
                                            sender.sendMessage("publicID:" + ID + "\n释放梦境费用:" + file.getString("Cost") + "\n描述:" + file.getString("Description") + "\n----------");
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            sender.sendMessage("筑梦系统>> 参数有误，请检查参数是否正确");
                            break;
                    }
                }
                return true;

            } else if (args[0].equalsIgnoreCase("snapshot")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("筑梦系统>> 你不能在控制台运行该指令");
                    return true;
                } else {
                    if (!(sender.hasPermission("BuildDream.use"))) {
                        sender.sendMessage("筑梦系统>> 你没有权限执行此命令");
                        return true;
                    } else {
                        Player player = (Player) sender;
                        if (args.length == 1) {
                            sender.sendMessage("筑梦系统>> 请填写公众ID");
                            return false;
                        } else {
                            if (!PublicID.hasPublicID(args[1])) {
                                player.sendMessage("筑梦系统>> 指定的公众ID不存在");
                            } else {
                                FakeDream.sendFakeDream(player,args[1]);
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage("筑梦系统>> 未知指令");
                return false;
            }
        }
        return true;
    }
}
