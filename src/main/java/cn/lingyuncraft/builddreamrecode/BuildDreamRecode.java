package cn.lingyuncraft.builddreamrecode;

import cn.lingyuncraft.builddreamrecode.listener.InventoryGetter;
import cn.lingyuncraft.builddreamrecode.listener.LocationSetter;
import cn.lingyuncraft.builddreamrecode.manage.CommandHandler;
import cn.lingyuncraft.builddreamrecode.utils.Configuration;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class BuildDreamRecode extends JavaPlugin {

    private static BuildDreamRecode plugin;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("正在加载BuildDreamRecode，版本" + getDescription().getVersion());
        try {
            Configuration.checkFirstRun();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Configuration.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Configuration.check();
        try {
            Storage.loadToDreamList();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new LocationSetter(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryGetter(), this);
        Bukkit.getPluginCommand("builddream").setExecutor(new CommandHandler());
        getLogger().info("加载完成，作者贺兰星辰，本插件仅用于繁星工坊服务器内部使用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载");
    }


    public static BuildDreamRecode getInstance() {
        return plugin;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((net.coreprotect.CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
    }
}