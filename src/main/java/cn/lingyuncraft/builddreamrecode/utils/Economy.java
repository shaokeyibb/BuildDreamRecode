package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.NonNull;
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

    public static boolean hasBalance(@NonNull UUID playerUUID, @NonNull double balance) {
        return getBalance(playerUUID) >= balance;
    }

    public static void addBalance(@NonNull UUID playerUUID, @NonNull double balance) {
        if (balance > 0) {
            BuildDreamRecode.getEconomy().depositPlayer(Bukkit.getPlayer(playerUUID), balance);
        } else if (balance < 0) {
            BuildDreamRecode.getEconomy().withdrawPlayer(Bukkit.getPlayer(playerUUID), Math.abs(balance));
        }
    }

    public static double getBalance(@NonNull UUID playerUUID) {
        return BuildDreamRecode.getEconomy().getBalance(Bukkit.getPlayer(playerUUID));
    }

    public double getBuyPrice(@NonNull ItemStack item) throws IOException, InvalidConfigurationException {
        Material m = item.getType();
        return storage.getDouble("worth." + m, -1);
    }
}
