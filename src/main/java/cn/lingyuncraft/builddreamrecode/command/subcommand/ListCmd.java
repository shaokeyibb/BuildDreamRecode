package cn.lingyuncraft.builddreamrecode.command.subcommand;

import cn.lingyuncraft.builddreamrecode.data.Dream;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.PCommand;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

public class ListCmd implements PCommand {
    @Override
    public String getPermission() {
        return "BuildDream.use";
    }

    @Override
    public boolean execute(PPlugin plugin, CommandSender sender, String[] args) {
        switch (args.length) {
            case 1:
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.ERROR, "在控制台中不能查询自己的梦境."));
                } else {
                    Player user = (Player) sender;
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "&a&l您&7的已筑梦境列表"));
                    for (Dream dream : Storage.get().getDataMap().values()) {
                        if (user.getUniqueId().toString().equals(dream.author.toString())) {
                            for (String msg : dream.info()) {
                                plugin.lang.send(user, msg);
                            }
                        }
                    }
                }
                break;
            case 2:
                if (!sender.hasPermission("Builddream.admin.check")) {
                    sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "您没有权限这么做."));
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "目标玩家离线或不存在."));
                    } else {
                        sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "&a&l" + target.getName() + " &7的已筑梦境列表"));
                        for (Dream dream : Storage.get().getDataMap().values()) {
                            if (target.getUniqueId().toString().equals(dream.author.toString())) {
                                for (String msg : dream.info()) {
                                    sender.sendMessage(msg);
                                }
                            }
                        }
                    }
                }
                break;
            default:
                sender.sendMessage(plugin.lang.build(plugin.localeKey, LocaleUtil.Type.ERROR, "参数有误, 请检查参数是否正确."));
                break;
        }
        return true;
    }
}
