package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PConfig;
import org.serverct.parrot.parrotx.utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Logger extends PConfig {

    private static Logger logger;

    public Logger(@NonNull PPlugin plugin) {
        super(plugin, "Logger", "日志文件");
    }

    public static Logger get() {
        if (logger == null) {
            logger = new Logger(BuildDreamRecode.getInstance());
        }
        return logger;
    }

    @Override
    public void init() {
        setFile(new File(Storage.get().getPluginFolder(), "Logger.yml"));
        super.init();
    }

    @Override
    public void saveDefault() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logBuild(String publicID, Player user) {
        config.set(publicID, null);
        config.set(publicID + ".Build." + user.getName(), TimeUtil.getDefaultFormatDate(new Date(System.currentTimeMillis())));
        save();
        plugin.lang.logAction("创建 " + user.getName() + " 的梦境", publicID);
    }

    public void logRelease(String publicID, Player user) {
        config.set(publicID, null);
        config.set(publicID + ".Release." + user.getName(), TimeUtil.getDefaultFormatDate(new Date(System.currentTimeMillis())));
        save();
        plugin.lang.logAction("释放 " + user.getName() + " 的梦境", publicID);
    }
}
