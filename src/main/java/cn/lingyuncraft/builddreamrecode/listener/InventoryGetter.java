package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.data.InventoryExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryGetter implements Listener {

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder != null) {
            if (holder instanceof InventoryExecutor) {
                e.setCancelled(true);
                ((InventoryExecutor) holder).execute(e);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveItem(InventoryMoveItemEvent e) {
        InventoryHolder holder = e.getDestination().getHolder();
        if (holder != null) {
            if (holder instanceof InventoryExecutor) {
                e.setCancelled(true);
            }
        }
    }
}
