package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Worth.WorthItem;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class Economy {
    YamlConfiguration storage = new YamlConfiguration();

    public Economy() throws IOException, InvalidConfigurationException {

        storage.load(Configuration.getHookCMICMIWorthSystemWorthFile());
    }

    public static boolean hasBalance(@NotNull UUID playerUUID, @NotNull double balance) {
        return getBalance(playerUUID) >= balance;
    }

    public static void addBalance(@NotNull UUID playerUUID, @NotNull double balance) {
        if (balance > 0) {
            BuildDreamRecode.getEconomy().depositPlayer(Bukkit.getPlayer(playerUUID), balance);
        } else if (balance < 0) {
            BuildDreamRecode.getEconomy().withdrawPlayer(Bukkit.getPlayer(playerUUID), Math.abs(balance));
        }
    }

    public static double getBalance(@NotNull UUID playerUUID) {
        return BuildDreamRecode.getEconomy().getBalance(Bukkit.getPlayer(playerUUID));
    }

    public double getBuyPrice(@NotNull ItemStack item) throws IOException, InvalidConfigurationException {
        if (Configuration.isHookCMICMIWorthSystemEnable()) {
            WorthItem worth = CMI.getInstance().getWorthManager().getWorth(item);
            if (item.getType() == Material.AIR || item.getType() == Material.VOID_AIR || item.getType() == Material.CAVE_AIR) {
                return 0;
            } else if (worth == null) {
                return -1;
            } else {
                return worth.getBuyPrice();
            }
        } else {
            Material m = item.getType();
            return storage.getDouble("worth." + m, -1);
        }
    }
}
