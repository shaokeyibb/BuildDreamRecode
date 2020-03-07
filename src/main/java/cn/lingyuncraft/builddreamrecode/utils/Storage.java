package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.data.Dream;
import lombok.Getter;
import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PFolder;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.I18n;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Storage extends PFolder {

    private static Storage storage;
    @Getter
    private Map<String, Dream> dataMap = new HashMap<>();

    public Storage(@NonNull PPlugin plugin) {
        super(plugin, "Storage", "梦境数据文件夹");
    }

    public static Storage get() {
        if (storage == null) {
            storage = new Storage(BuildDreamRecode.getInstance());
        }
        return storage;
    }

    @Override
    public void init() {
        super.folder = getFolder();
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                releaseDefaultData();
                plugin.lang.log("未找到 &c" + getTypeName() + "&7, 已重新生成.", I18n.Type.WARN, false);
            } else {
                plugin.lang.log("尝试生成 &c" + getTypeName() + " &7失败.", I18n.Type.ERROR, false);
            }
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            releaseDefaultData();
            files = folder.listFiles();
        }
        if (files != null && files.length != 0) {
            for (File file : files) {
                load(file);
            }
            plugin.lang.log("共加载 &c" + getTypeName() + " &7中的 &c" + files.length + " &7个数据文件.", I18n.Type.INFO, false);
        } else {
            plugin.lang.log("&c" + getTypeName() + " &7中没有数据可供加载.", I18n.Type.WARN, false);
        }
    }

    @Override
    public void load(File file) {
        File[] files = file.listFiles(pathname -> pathname.getName().endsWith(".yml"));
        if (files != null && files.length != 0) {
            for (File targetFile : files) {
                String key = BasicUtil.getNoExFileName(targetFile.getName());
                dataMap.put(key, new Dream(new PID(plugin, "DREAM_" + key), targetFile));
            }
        }
    }

    @Override
    public File getFolder() {
        return new File(getPluginFolder(), "Storage");
    }

    public File getPluginFolder() {
        if (Configuration.GAMEMODE.equals("SURVIVAL")) {
            return plugin.getDataFolder();
        } else {
            return new File(Configuration.DATAFOLDER);
        }
    }

    public File getSchematicFile(String publicID) {
        File folder = new File(getFolder(), publicID);
        return new File(folder, publicID + ".schematic");
    }

    public Dream get(String publicID) {
        if (!dataMap.containsKey(publicID)) {
            initWithSilent();
        }
        return dataMap.getOrDefault(publicID, null);
    }

    public void save(Dream dream) {
        dataMap.put(dream.publicID, dream);
        dream.save();
    }

    public void initWithSilent() {
        super.folder = getFolder();
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                plugin.lang.logError(I18n.LOAD, getTypeName(), "无法新建文件夹.");
            }
        }
        File[] files = folder.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                load(file);
            }
        }
    }
}

