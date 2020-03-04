package cn.lingyuncraft.builddreamrecode.listener;

import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.features.FakeDream;
import cn.lingyuncraft.builddreamrecode.features.PublicShop;
import cn.lingyuncraft.builddreamrecode.utils.Economy;
import cn.lingyuncraft.builddreamrecode.utils.PublicID;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class InventoryGetter implements Listener {

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e) throws IOException, InvalidConfigurationException {

        ClickType clickType = e.getClick();
        ItemStack item = e.getCurrentItem();
        HumanEntity author = e.getWhoClicked();

        if (e.getView().getTitle().equals(PublicShop.SHOP_TITLE)) {
            if (!(e.getCurrentItem() == null)) {
                String[] dreamAuthorRaw = e.getCurrentItem().getLore().get(2).split("[:]");
                if (author.getUniqueId().toString().equals(Bukkit.getPlayer(dreamAuthorRaw[1]).getUniqueId().toString())) {
                    author.sendMessage("你不能购买自己的梦境");
                } else {
                    String[] costRaw = e.getCurrentItem().getLore().get(4).split("[:]");
                    double cost = Double.parseDouble(costRaw[1]);
                    String[] publicBuyFeeRaw = e.getCurrentItem().getLore().get(5).split("[:]");
                    double publicBuyFee = Double.parseDouble(publicBuyFeeRaw[1]);
                    if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
                        if (Economy.hasBalance(author.getUniqueId(), cost + publicBuyFee)) {
                            author.sendMessage("筑梦系统>> 释放梦境失败，您没有足够的金币支付筑梦和授权费用");
                        } else {
                            Economy.addBalance(author.getUniqueId(), -publicBuyFee);
                            author.sendMessage("筑梦系统>> 已扣除筑梦授权费" + publicBuyFee);
                            Economy.addBalance(Bukkit.getPlayer(dreamAuthorRaw[1]).getUniqueId(), publicBuyFee * 0.7);
                            Bukkit.getPlayer(dreamAuthorRaw[1]).sendMessage("筑梦系统>> 你收到了来自" + author.getName() + "的筑梦授权费" + publicBuyFee * 0.7 + "金币");
                            DreamControl.releaseDream(PublicShop.getShopPublicID(item), author.getUniqueId());
                        }
                    } else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                        FakeDream.sendFakeDream((Player) author, PublicShop.getShopPublicID(item));
                    }
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveItem(InventoryMoveItemEvent e) {
    }
}
