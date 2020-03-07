package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.data.ShopInventoryHolder;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PublicShop {

    public static String SHOP_TITLE = "§6§l筑梦市场";

    public static void sendGUI(Player player) {
        Storage.get().initWithSilent();
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                player.openInventory(new ShopInventoryHolder().getInventory());
            }
        }.runTask(BuildDreamRecode.getInstance());
    }

    public static String getShopPublicID(ItemStack item) {
        return item.getItemMeta().getDisplayName();
    }

}
