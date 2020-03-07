package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DreamInit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Storage.get().initWithSilent();
    }
}
