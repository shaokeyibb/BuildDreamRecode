package cn.lingyuncraft.builddreamrecode.data;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.features.DreamControl;
import cn.lingyuncraft.builddreamrecode.features.FakeDream;
import cn.lingyuncraft.builddreamrecode.features.PublicShop;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

import java.util.*;

import static org.bukkit.Material.CHEST;

public class ShopInventoryHolder implements InventoryExecutor {

    private PPlugin plugin;

    protected Inventory inventory;

    private Map<Integer, Dream> dreamMap = new HashMap<>();

    public ShopInventoryHolder() {
        this.plugin = BuildDreamRecode.getInstance();
        this.inventory = construct();
    }

    @Override
    public void execute(InventoryClickEvent event) {
        ClickType clickType = event.getClick();
        ItemStack item = event.getCurrentItem();
        Player user = (Player) event.getWhoClicked();

        if (item != null && item.getType() != Material.AIR) {
            Dream dream = dreamMap.getOrDefault(event.getSlot(), null);
            if (dream == null) {
                plugin.lang.logError(LocaleUtil.GET, "GUI-Dream(筑梦商店中的梦境)", "Dream 对象为 null.");
                return;
            }
            try {
                if (user.getUniqueId().toString().equals(dream.author.toString())) {
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "您不能购买自己的梦境."));
                } else {
                    double cost = dream.cost + dream.fee;
                    double money = ((BuildDreamRecode) plugin).getVaultUtil().getBalances(user);
                    if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
                        if (money < cost) {
                            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "释放梦境失败, 您没有足够的金币支付筑梦和授权费用."));
                        } else {
                            ((BuildDreamRecode) plugin).getVaultUtil().take(user, dream.fee);
                            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "已扣除筑梦授权费 &c" + dream.fee + " &7金币."));

                            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                            if (rsp != null) {
                                Economy economy = rsp.getProvider();
                                double permission = dream.fee * 0.7;
                                economy.depositPlayer(Bukkit.getOfflinePlayer(dream.author), permission);

                                Player author = Bukkit.getPlayer(dream.author);
                                if (author != null) {
                                    plugin.lang.send(author, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "您收到了来自 &a" + user.getName() + " &7的筑梦授权费 &c" + permission + " &7金币."));
                                }
                            }

                            DreamControl.releaseDream(dream.publicID, user.getUniqueId());
                        }
                    } else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
                        FakeDream.sendFakeDream(user, dream.publicID);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Inventory construct() {
        try {
            Inventory inv = Bukkit.createInventory(this, 6 * 9, PublicShop.SHOP_TITLE);
            int amount = 0;
            for (Dream dream : Storage.get().getDataMap().values()) {
                String id = dream.publicID;
                double costAll = dream.cost + dream.fee;

                if (dream.publicMode) {
                    if (amount > 54) {
                        // player.sendMessage("由于界面限制和插件作者不会分页，部分梦境未能显示");
                        break;
                    }

                    Material icon = CHEST;
                    try {
                        List<DyeColor> colors = new ArrayList<>(Arrays.asList(DyeColor.values()));
                        String color = colors.get(new Random().nextInt(colors.size())).name();
                        icon = Material.valueOf(color + "_BED");
                    } catch (Throwable e) {
                        plugin.lang.logError(LocaleUtil.LOAD, "GUI(筑梦市场)", e.toString());
                    }
                    ItemStack itemStack = new ItemStack(icon, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    itemMeta.setDisplayName(id);

                    String[] loreFormat = {
                            "",
                            "&7梦境信息 ▶",
                            "  &7公众ID: &f" + id,
                            "  &7描述: &f" + dream.description,
                            "  &7作者: &f" + Bukkit.getOfflinePlayer(dream.author).getName(),
                            "",
                            "&7价格 ▶",
                            "  &7筑梦费用: &f" + dream.cost,
                            "  &7授权费用: &f" + dream.fee,
                            "  &7共 &f" + costAll + " &7金币",
                            "",
                            "&7筑梦于 ▶",
                            "  &f" + dream.getTime(),
                            "",
                            "  &a▶ &f左键以购买并释放梦境",
                            "  &b▶ &f右键以释放梦境预览",
                            ""
                    };

                    List<String> lore = new ArrayList<>(Arrays.asList(loreFormat));
                    lore.replaceAll(s -> plugin.lang.color(s));

                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    inv.addItem(itemStack);
                    dreamMap.put(amount, dream);
                    amount++;
                }
            }
            return inv;
        } catch (Throwable e) {
            plugin.lang.logError(LocaleUtil.LOAD, "GUI(筑梦商店)", e.toString());
            e.printStackTrace();
        }
        return Bukkit.createInventory(this, 0, "菜单初始化失败, 请联系管理员");
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
