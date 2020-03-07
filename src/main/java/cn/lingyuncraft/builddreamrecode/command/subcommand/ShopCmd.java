package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.features.PublicShop;
import cn.lingyuncraft.builddreamrecode.utils.Configuration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.I18n;

public class ShopCmd implements PCommand {
    @Override
    public String getPermission() {
        return "BuildDream.use";
    }

    @Override
    public boolean execute(PPlugin plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player user = (Player) sender;
            if (!Configuration.GLOBAL_SHOP) {
                I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.WARN, "筑梦商店未开放."));
                return true;
            } else {
                PublicShop.sendGUI(user);
            }
        } else {
            sender.sendMessage(plugin.lang.build(plugin.localeKey, I18n.Type.ERROR, "您不能在控制台运行该指令."));
        }
        return true;
    }
}
