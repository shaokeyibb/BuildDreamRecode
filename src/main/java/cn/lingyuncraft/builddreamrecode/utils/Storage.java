package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.data.Dream;
import lombok.Getter;
import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PFolder;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.utils.BasicUtil;

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
        super.init();
    }

    @Override
    public void load(File file) {
        File[] files = file.listFiles(pathname -> pathname.getName().endsWith(".yml"));
        if (files != null && files.length != 0) {
            String key = BasicUtil.getNoExFileName(file.getName());
            dataMap.put(key, new Dream(new PID(plugin, "DREAM_" + key), file));
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
        return dataMap.getOrDefault(publicID, null);
    }

    public void save(Dream dream) {
        dataMap.put(dream.publicID, dream);
        dream.save();
    }
}

