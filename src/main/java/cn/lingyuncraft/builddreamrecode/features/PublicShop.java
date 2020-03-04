package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PublicShop {

    public static String SHOP_TITLE = "§6§l筑梦市场";

    public static void sendGUI(Player player) throws IOException, InvalidConfigurationException {
        Storage.loadToDreamList();
        Inventory inv = Bukkit.createInventory(null,54,SHOP_TITLE);
        int amount = 0;
        for (String id : Storage.getDreamList()) {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(Storage.getStorageYAMLFile(id));
            String author = yaml.getString("author");
            double cost = yaml.getDouble("Cost");
            String description = yaml.getString("Description");
            boolean isPublic = yaml.getBoolean("IsPublic");
            double publicBuyFee = yaml.getDouble("PublicBuyFee");
            String savedTime = yaml.getString("SavedTime");
            double costAll = cost+publicBuyFee;
            if (isPublic){
                amount++;
                if (amount==54){
                    player.sendMessage("由于界面限制和插件作者不会分页，部分梦境未能显示");
                    break;
                }
                ItemStack itemStack = new ItemStack(Material.STONE, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = new LinkedList<>();
                lore.add(0, "公众ID: "+ id);
                lore.add(1,"描述:"+description);
                lore.add(2,"梦境作者: "+Bukkit.getPlayer(UUID.fromString(author)).getDisplayName());
                lore.add(3,"----------");
                lore.add(4,"筑梦费用: "+cost);
                lore.add(5,"梦境授权费用: "+publicBuyFee);
                lore.add(6,"总计单次费用: "+costAll);
                lore.add(7,"----------");
                lore.add(8,"筑梦时间："+savedTime);
                lore.add(9,"----------");
                lore.add(10,"左键此处以购买并释放梦境");
                lore.add(11,"右键此处以释放梦境预览");
                lore.add(12,"----------");
                itemMeta.setDisplayName(id);
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inv.addItem(itemStack);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                player.openInventory(inv);
            }
        }.runTask(BuildDreamRecode.getInstance());
    }

    public static String getShopPublicID(ItemStack item) {
        return item.getItemMeta().getDisplayName();
    }

}
