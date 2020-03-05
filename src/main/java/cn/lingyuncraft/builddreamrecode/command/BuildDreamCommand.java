package cn.lingyuncraft.builddreamrecode.command;

import cn.lingyuncraft.builddreamrecode.command.subcommand.*;
import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.CommandHandler;
import org.serverct.parrot.parrotx.command.subcommands.ReloadCommand;

public class BuildDreamCommand extends CommandHandler {

    public BuildDreamCommand(@NonNull PPlugin plugin) {
        super(plugin);
        registerSubCommand("create", new CreateCmd());
        registerSubCommand("list", new ListCmd());
        registerSubCommand("release", new ReleaseCmd());
        registerSubCommand("shop", new ShopCmd());
        registerSubCommand("snapshot", new SnapshotCmd());
        registerSubCommand("reload", new ReloadCommand("BuildDream.reload"));
    }

}
