package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.TempStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

import java.util.UUID;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class LocationSetter implements Listener {
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

                    if (e.getAction() == LEFT_CLICK_BLOCK) {
                        TempStorage.getTempPos1().put(uuid, loc);
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.INFO, "已选择点1(" + BasicUtil.formatLocation(loc) + "&7)"));
                    } else if (e.getAction() == RIGHT_CLICK_BLOCK) {
                        TempStorage.getTempPos2().put(uuid, loc);
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.INFO, "已选择点2(" + BasicUtil.formatLocation(loc) + "&7)"));
                    }
                }
            }
        }
    }
}
