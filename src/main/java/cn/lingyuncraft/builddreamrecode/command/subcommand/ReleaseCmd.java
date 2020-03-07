package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.data.Dream;
import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.utils.PublicID;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.I18n;

public class ReleaseCmd implements PCommand {
    @Override
    public String getPermission() {
        return "BuildDream.use";
    }

    @Override
    public boolean execute(PPlugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player user = (Player) sender;
            if (args.length == 1) {
                I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "请填写公众ID."));
                return false;
            } else {
                if (!PublicID.hasPublicID(args[1])) {
                    I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "目标公众ID无效."));
                } else {
                    Dream dream = Storage.get().get(args[1]);
                    if (dream != null) {
                        if (user.getUniqueId().toString().equals(dream.author.toString())) {
                            DreamControl.releaseDream(args[1], user.getUniqueId());
                        }
                    } else {
                        I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "未找到梦境对象."));
                    }
                }
            }
        } else {
            sender.sendMessage(plugin.lang.build(plugin.localeKey, I18n.Type.ERROR, "您不能在控制台运行该指令."));
        }
        return true;
    }
}
