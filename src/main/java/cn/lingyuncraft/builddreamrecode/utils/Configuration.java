package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration {

    public static File file = new File(BuildDreamRecode.getInstance().getDataFolder(), "config.yml");
    public static File storage = new File(BuildDreamRecode.getInstance().getDataFolder(), "storage");

    @Getter
    public static boolean EnablePublicShop;
    @Getter
    public static String ServerServerType;
    @Getter
    public static String ServerSurvivalServerPluginDatafolder;
    @Getter
    public static boolean HookResidence;
    @Getter
    public static boolean HookWorldGuard;
    @Getter
    public static boolean HookCMICMIWorthSystemEnable;
    @Getter
    public static File HookCMICMIWorthSystemWorthFile;
    @Getter
    public static double PriceDefaultBuyPricePerItem;
    @Getter
    public static double PriceDefaultPublicBuyFee;
    @Getter
    public static double PriceBuildDreamPrice;
    @Getter
    public static double PriceRedstoneExPrice;
    @Getter
    public static double PriceBalanceExPriceMultiple;
    @Getter
    public static boolean CheckEnableBarrierCheck;
    @Getter
    public static boolean CheckEnableBedrockCheck;
    @Getter
    public static boolean CheckEnableRedstoneCheck;

    public static YamlConfiguration config = new YamlConfiguration();

    public static void checkFirstRun() throws IOException, InvalidConfigurationException {
        if (!BuildDreamRecode.getInstance().getDataFolder().exists()) {
            BuildDreamRecode.getInstance().getDataFolder().mkdir();
            BuildDreamRecode.getInstance().getLogger().info("插件文件夹不存在，创建文件夹");
        }
        if (!file.exists()) {
            BuildDreamRecode.getInstance().saveDefaultConfig();
            BuildDreamRecode.getInstance().getLogger().info("插件配置文件不存在，释放文件");
        }
        if (!storage.exists()) {
            storage.mkdir();
            BuildDreamRecode.getInstance().getLogger().info("归档文件夹不存在，创建文件夹");
        }
        if (!new File(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "logger.yml").exists()) {
            new File(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "logger.yml").createNewFile();
            BuildDreamRecode.getInstance().getLogger().info("记录文件不存在，创建记录文件");
        }

        config.load(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "config.yml");

        if (config.getBoolean("FirstRun")) {
            BuildDreamRecode.getInstance().getLogger().info("检测到插件为第一次运行，请配置完成后再重载插件");
            config.set("FirstRun", false);
            config.save(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "config.yml");
        }
    }

    public static void loadConfig() throws IOException, InvalidConfigurationException {
        config.load(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "config.yml");
        if (!config.getBoolean("Enable")) {
            BuildDreamRecode.getInstance().getLogger().info("插件未设置开启，自动关闭...");
            BuildDreamRecode.getInstance().getPluginLoader().disablePlugin(BuildDreamRecode.getInstance());
        } else {
            EnablePublicShop = config.getBoolean("EnablePublicShop");
            ServerServerType = config.getString("Server.ServerType");
            ServerSurvivalServerPluginDatafolder = config.getString("Server.SurvivalServerPluginDatafolder");
            HookResidence = config.getBoolean("Hook.Residence");
            HookWorldGuard = config.getBoolean("Hook.WorldGuard");
            HookCMICMIWorthSystemEnable = config.getBoolean("Hook.CMI.CMIWorthSystem.Enable");
            if (!HookCMICMIWorthSystemEnable) {
                HookCMICMIWorthSystemWorthFile = new File(config.getString("Hook.CMI.CMIWorthSystem.WorthFile"));
                BuildDreamRecode.getInstance().getLogger().info("正使用非CMI价格表模式");
            }else{
                BuildDreamRecode.getInstance().getLogger().info("正使用CMI价格表模式");
            }
            PriceDefaultBuyPricePerItem = config.getDouble("Price.DefaultBuyPricePerItem");
            PriceDefaultPublicBuyFee = config.getDouble("Price.DefaultPublicBuyFee");
            PriceBuildDreamPrice = config.getDouble("Price.BuildDreamPrice");
            PriceRedstoneExPrice = config.getDouble("Price.RedstoneExPrice");
            PriceBalanceExPriceMultiple = config.getDouble("Price.BalanceExPriceMultiple");
            CheckEnableBarrierCheck = config.getBoolean("Check.EnableBarrierCheck");
            CheckEnableBedrockCheck = config.getBoolean("Check.EnableBedrockCheck");
            CheckEnableRedstoneCheck = config.getBoolean("Check.EnableRedstoneCheck");
        }
    }

    public static void check() {
        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            BuildDreamRecode.getInstance().getLogger().info("本服务器正在使用 生存服 模式运行");
        } else {
            BuildDreamRecode.getInstance().getLogger().info("本服务器正在使用 创造服 模式运行");
        }
    }

}
