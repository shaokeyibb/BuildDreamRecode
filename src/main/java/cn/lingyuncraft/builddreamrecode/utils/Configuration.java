package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.NonNull;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PConfig;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

import java.io.File;

public class Configuration extends PConfig {

    public static boolean GLOBAL_SHOP;

    public static String GAMEMODE;
    public static String DATAFOLDER;

    public static boolean RESIDENCE_HOOK;
    public static boolean WORLDGUARD_HOOK;

    public static double PRICE_ITEM;
    public static double PRICE_FEE;
    public static double PRICE_BUILDDREAM;
    public static double PRICE_REDSTONE;
    public static double PRICE_BALANCE;

    public static boolean CHECK_BARRIER;
    public static boolean CHECK_BEDROCK;
    public static boolean CHECK_REDSTONE;

    public Configuration(@NonNull PPlugin plugin) {
        super(plugin, "config", "主配置文件");
    }

    @Override
    public void saveDefault() {
        plugin.saveDefaultConfig();
    }

    @Override
    public void load(@NonNull File file) {
        if (!config.getBoolean("Enable")) {
            plugin.lang.log("插件未设置开启, 自动关闭...", LocaleUtil.Type.ERROR, false);
            plugin.getPluginLoader().disablePlugin(BuildDreamRecode.getInstance());
        } else {
            GLOBAL_SHOP = config.getBoolean("Shop");
            GAMEMODE = config.getString("Server.Gamemode");
            DATAFOLDER = config.getString("Server.Datafolder");

            RESIDENCE_HOOK = config.getBoolean("Hook.Residence");
            WORLDGUARD_HOOK = config.getBoolean("Hook.WorldGuard");

            PRICE_ITEM = config.getDouble("Price.Item");
            PRICE_FEE = config.getDouble("Price.Fee");
            PRICE_BUILDDREAM = config.getDouble("Price.BuildDream");
            PRICE_REDSTONE = config.getDouble("Price.Redstone");
            PRICE_BALANCE = config.getDouble("Price.BalanceMultiple");

            CHECK_BARRIER = config.getBoolean("Check.Barrier");
            CHECK_BEDROCK = config.getBoolean("Check.Bedrock");
            CHECK_REDSTONE = config.getBoolean("Check.Redstone");

            if (Configuration.GAMEMODE.equals(GameMode.SURVIVAL.toString())) {
                plugin.lang.log("本服务器正在使用 &c生存服 &7模式运行.", LocaleUtil.Type.INFO, false);
            } else {
                plugin.lang.log("本服务器正在使用 &a创造服 &7模式运行.", LocaleUtil.Type.INFO, false);
            }
        }
    }

    public static double price(Material material) {
        return Worth.get().getConfig().getDouble("Worth." + material, -1);
    }
}
