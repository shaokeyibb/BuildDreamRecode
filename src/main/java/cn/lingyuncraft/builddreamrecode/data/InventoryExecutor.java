package cn.lingyuncraft.builddreamrecode.data;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface InventoryExecutor extends InventoryHolder {
    void execute(InventoryClickEvent event);

    Inventory construct();
}
