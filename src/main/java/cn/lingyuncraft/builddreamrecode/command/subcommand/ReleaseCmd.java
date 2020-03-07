package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.data.Dream;
import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.utils.PublicID;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

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
                plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "请填写公众ID."));
                return false;
            } else {
                if (!PublicID.hasPublicID(args[1])) {
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "目标公众ID无效."));
                } else {
                    DreamControl.releaseDream(args[1], user.getUniqueId());
                    return true;
                }
            }
        } else {
            sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.ERROR, "您不能在控制台运行该指令."));
        }
        return true;
    }
}
