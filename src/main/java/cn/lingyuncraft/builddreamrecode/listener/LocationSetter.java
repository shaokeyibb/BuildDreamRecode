package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.utils.TempStorage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class LocationSetter implements Listener {
    @EventHandler
    public void onBlockBreak(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().getType().equals(Material.WOODEN_SWORD)) {
                e.setCancelled(true);
                if (e.getAction() == LEFT_CLICK_BLOCK) {
                    TempStorage.getTempPos1().put(e.getPlayer().getUniqueId(), Objects.requireNonNull(e.getClickedBlock()).getLocation());
                    e.getPlayer().sendMessage("筑梦系统>> 已选择点1(" + e.getClickedBlock().getLocation().getX() + "," + e.getClickedBlock().getLocation().getY() + "," + e.getClickedBlock().getLocation().getZ() + ")");
                } else if (e.getAction() == RIGHT_CLICK_BLOCK) {
                    TempStorage.getTempPos2().put(e.getPlayer().getUniqueId(), Objects.requireNonNull(e.getClickedBlock()).getLocation());
                    e.getPlayer().sendMessage("筑梦系统>> 已选择点2(" + e.getClickedBlock().getLocation().getX() + "," + e.getClickedBlock().getLocation().getY() + "," + e.getClickedBlock().getLocation().getZ() + ")");
                }
            }
        }
    }
}
