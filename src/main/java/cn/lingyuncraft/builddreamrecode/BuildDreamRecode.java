package cn.lingyuncraft.builddreamrecode;

import cn.lingyuncraft.builddreamrecode.command.BuildDreamCommand;
import cn.lingyuncraft.builddreamrecode.listener.InventoryListener;
import cn.lingyuncraft.builddreamrecode.listener.SelectionListener;
import cn.lingyuncraft.builddreamrecode.utils.Configuration;
import cn.lingyuncraft.builddreamrecode.utils.Logger;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import cn.lingyuncraft.builddreamrecode.utils.Worth;
import lombok.Getter;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.hooks.VaultUtil;


public final class BuildDreamRecode extends PPlugin {

    @Getter
    private VaultUtil vaultUtil;

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    protected void preload() {
        pConfig = new Configuration(this);
        pConfig.init();
    }

    @Override
    protected void load() {
        getLogger().info("正在加载BuildDreamRecode，版本" + getDescription().getVersion());

        vaultUtil = new VaultUtil(this, true);
        Storage.get().init();
        Logger.get().init();
        Worth.get().init();

        super.registerCommand(new BuildDreamCommand(this,"builddream"));

        if (!vaultUtil.isHooks()) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getLogger().info("加载完成，作者贺兰星辰，本插件仅用于繁星工坊服务器内部使用！");
    }

    @Override
    protected void registerListener() {
        Bukkit.getPluginManager().registerEvents(new SelectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info("插件已卸载");
    }

    public CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((net.coreprotect.CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }

        return CoreProtect;
    }
}