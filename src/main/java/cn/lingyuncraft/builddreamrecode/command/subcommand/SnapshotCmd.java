package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.features.FakeDream;
import cn.lingyuncraft.builddreamrecode.utils.PublicID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.I18n;

public class SnapshotCmd implements PCommand {
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
            } else {
                if (!PublicID.hasPublicID(args[1])) {
                    I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.ERROR, "目标公众ID无效."));
                } else {
                    FakeDream.sendFakeDream(user, args[1]);
                }
            }
        } else {
            sender.sendMessage(plugin.lang.build(plugin.localeKey, I18n.Type.ERROR, "您不能在控制台运行该指令."));
        }
        return true;
    }
}
