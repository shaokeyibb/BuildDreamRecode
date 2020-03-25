package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.utils.PublicID;
import cn.lingyuncraft.builddreamrecode.utils.TempStorage;
import com.sk89q.worldedit.WorldEditException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.I18n;

import java.io.IOException;
import java.util.UUID;

public class CreateCmd implements PCommand {
    @Override
    public String getPermission() {
        return "BuildDream.use";
    }

    @Override
    public boolean execute(PPlugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player user = (Player) sender;
            UUID uuid = user.getUniqueId();
            if (TempStorage.getTempPos1().get(uuid) == null || TempStorage.getTempPos1().get(uuid) == null) {
                I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "请先用木剑完成选区后再筑梦."));
            } else {
                switch (args.length) {
                    case 1:
                        I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "请填写公众ID."));
                        break;
                    case 2:
                        buildDream(plugin, user, args[1], null, 0.0D);
                        break;
                    case 3:
                        buildDream(plugin, user, args[1], args[2], 0.0D);
                        break;
                    default:
                        try {
                            double fee = Double.parseDouble(args[3]);
                            buildDream(plugin, user, args[1], args[2], fee);
                        } catch (Throwable e) {
                            plugin.lang.logError("筑梦", args[1], e.toString());
                        }
                        break;
                }
            }
        } else {
            sender.sendMessage(plugin.lang.build(plugin.localeKey, I18n.Type.ERROR, "您不能在控制台运行该指令."));
        }
        return true;
    }

    private void buildDream(PPlugin plugin, Player user, String publicID, String desc, double fee) {
        UUID uuid = user.getUniqueId();
        String description = "这个人很懒, 什么都没有描述.";
        if (desc != null && !desc.equalsIgnoreCase("")) {
            description = desc;
        }

        int checkResult = PublicID.checkPublicID(publicID);
        switch (checkResult) {
            case 1:
                I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "公众ID已被占用, 换一个试试."));
                break;
            case 2:
                I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "公众ID含有非法字符, 请检查后重新输入!"));
                break;
            case 0:
                try {
                    if(fee!=0.0D){
                        DreamControl.buildDream(publicID, user.getWorld(), TempStorage.getTempPos1().get(uuid), TempStorage.getTempPos2().get(uuid), uuid, description, false, 0);
                    }else{
                        DreamControl.buildDream(publicID, user.getWorld(), TempStorage.getTempPos1().get(uuid), TempStorage.getTempPos2().get(uuid), uuid, description, true, fee);
                    }
                } catch (WorldEditException | IOException e) {
                    plugin.lang.logError("筑梦", publicID, e.toString());
                    e.printStackTrace();
                }
                break;
            default:
                plugin.lang.logError("筑梦", publicID, "未知公众ID检查值: " + checkResult);
                break;
        }
    }
}
