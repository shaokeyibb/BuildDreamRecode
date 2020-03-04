package cn.lingyuncraft.builddreamrecode.data;

import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.features.FakeDream;
import cn.lingyuncraft.builddreamrecode.features.PublicShop;
import cn.lingyuncraft.builddreamrecode.utils.Economy;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopInventoryHolder implements InventoryExecutor {

    protected Inventory inventory;

    public ShopInventoryHolder() {
        this.inventory = construct();
    }

    @Override
    public void execute(InventoryClickEvent event) {
        ClickType clickType = event.getClick();
        ItemStack item = event.getCurrentItem();
        HumanEntity user = event.getWhoClicked();

        if (item != null && item.getType() != Material.AIR) {
            List<String> lore = item.getLore();
            if (lore != null && !lore.isEmpty()) {
                try {
                    String[] dreamAuthorRaw = lore.get(2).split("[:]");
                    if (user.getUniqueId().toString().equals(Bukkit.getPlayer(dreamAuthorRaw[1]).getUniqueId().toString())) {
                        user.sendMessage("你不能购买自己的梦境");
                    } else {
                        String[] costRaw = lore.get(4).split("[:]");
                        double cost = Double.parseDouble(costRaw[1]);
                        String[] publicBuyFeeRaw = lore.get(5).split("[:]");
                        double publicBuyFee = Double.parseDouble(publicBuyFeeRaw[1]);
                        if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
                            if (Economy.hasBalance(user.getUniqueId(), cost + publicBuyFee)) {
                                user.sendMessage("筑梦系统>> 释放梦境失败，您没有足够的金币支付筑梦和授权费用");
                            } else {
                                Economy.addBalance(user.getUniqueId(), -publicBuyFee);
                                user.sendMessage("筑梦系统>> 已扣除筑梦授权费" + publicBuyFee);
                                Economy.addBalance(Bukkit.getPlayer(dreamAuthorRaw[1]).getUniqueId(), publicBuyFee * 0.7);
                                Bukkit.getPlayer(dreamAuthorRaw[1]).sendMessage("筑梦系统>> 你收到了来自" + user.getName() + "的筑梦授权费" + publicBuyFee * 0.7 + "金币");
                                DreamControl.releaseDream(PublicShop.getShopPublicID(item), user.getUniqueId());
                            }
                        } else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                            FakeDream.sendFakeDream((Player) user, PublicShop.getShopPublicID(item));
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Inventory construct() {
        try {
            Storage.loadToDreamList();
            Inventory inv = Bukkit.createInventory(this, 6 * 9, PublicShop.SHOP_TITLE);
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
                double costAll = cost + publicBuyFee;

                if (isPublic) {
                    amount++;
                    if (amount > 54) {
                        // player.sendMessage("由于界面限制和插件作者不会分页，部分梦境未能显示");
                        break;
                    }
                    ItemStack itemStack = new ItemStack(Material.STONE, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    itemMeta.setDisplayName(id);

                    List<String> lore = new ArrayList<>();
                    lore.add(0, "公众ID: " + id);
                    lore.add(1, "描述:" + description);
                    lore.add(2, "梦境作者: " + Bukkit.getPlayer(UUID.fromString(author)).getDisplayName());
                    lore.add(3, "----------");
                    lore.add(4, "筑梦费用: " + cost);
                    lore.add(5, "梦境授权费用: " + publicBuyFee);
                    lore.add(6, "总计单次费用: " + costAll);
                    lore.add(7, "----------");
                    lore.add(8, "筑梦时间：" + savedTime);
                    lore.add(9, "----------");
                    lore.add(10, "左键此处以购买并释放梦境");
                    lore.add(11, "右键此处以释放梦境预览");
                    lore.add(12, "----------");

                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    inv.addItem(itemStack);
                }
            }
            return inv;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Bukkit.createInventory(this, 0, "菜单初始化失败, 请联系管理员");
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
