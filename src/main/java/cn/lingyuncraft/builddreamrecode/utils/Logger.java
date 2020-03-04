package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Logger {

    static YamlConfiguration storage1 = new YamlConfiguration();
    static YamlConfiguration storage2 = new YamlConfiguration();

    public static void logBuild(String publicID, UUID playerUUID) throws IOException, InvalidConfigurationException {
        storage1.load(Storage.getPluginFolderPath() + File.separator + "logger.yml");
        String playerName = Bukkit.getPlayer(playerUUID).getDisplayName();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        storage1.set(publicID, null);
        storage1.set(publicID + ".Build." + playerName, currentTime);
        storage1.save(Storage.getPluginFolderPath() + File.separator + "logger.yml");
        BuildDreamRecode.getInstance().getLogger().info(playerName + "创建梦境\"" + publicID + "\"成功");
    }

    public static void logRelease(String publicID, UUID playerUUID) throws IOException, InvalidConfigurationException {
        storage1.load(Storage.getPluginFolderPath() + File.separator + "logger.yml");
        String playerName = Bukkit.getPlayer(playerUUID).getDisplayName();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        storage2.set(publicID, null);
        storage2.set(publicID + ".Release." + playerName, currentTime);
        storage2.save(Storage.getPluginFolderPath() + File.separator + "logger.yml");
        BuildDreamRecode.getInstance().getLogger().info(playerName + "释放梦境\"" + publicID + "\"成功");
    }
}
