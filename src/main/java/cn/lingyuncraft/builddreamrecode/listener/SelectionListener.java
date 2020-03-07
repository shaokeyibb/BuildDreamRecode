package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.TempStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.I18n;

import java.util.UUID;

public class SelectionListener implements Listener {
    @EventHandler
    public void onBlockBreak(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().getType().equals(Material.WOODEN_SWORD)) {
                Block block = e.getClickedBlock();
                if (block != null) {
                    e.setCancelled(true);

                    PPlugin plugin = BuildDreamRecode.getInstance();
                    Location loc = block.getLocation();
                    Player user = e.getPlayer();
                    UUID uuid = user.getUniqueId();

                    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        TempStorage.getTempPos1().put(uuid, loc);
                        I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.INFO, "已选择点1(" + BasicUtil.formatLocation(loc) + "&7)"));
                    } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        TempStorage.getTempPos2().put(uuid, loc);
                        I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.INFO, "已选择点2(" + BasicUtil.formatLocation(loc) + "&7)"));
                    }
                }
            }
        }
    }
}
