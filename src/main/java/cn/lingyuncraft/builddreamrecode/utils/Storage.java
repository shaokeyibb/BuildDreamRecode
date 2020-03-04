package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Storage {

    public static void saveStorageYAMLFile(String publicID, UUID author, double cost, String description, boolean hasRedstone, boolean isPublic, double publicBuyFee, HashMap<Material,Integer> MaterialNumber) throws IOException {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        getStorageYAMLFile(publicID).createNewFile();
        YamlConfiguration storage = new YamlConfiguration();
        storage.set("Version",2.0);
        storage.set("PublicID", publicID);
        storage.set("author", author.toString());
        storage.set("Cost", cost);
        storage.set("Description", description);
        storage.set("HasRedstone", hasRedstone);
        storage.set("IsPublic", isPublic);
        storage.set("PublicBuyFee", publicBuyFee);
        storage.set("SavedTime", currentTime);
        storage.set("BlockTotal",null);
        for (Material m:MaterialNumber.keySet()){
            storage.set("BlockTotal."+m,MaterialNumber.get(m));
        }
        storage.save(getStorageYAMLFile(publicID));
    }

    public static File getStorageYAMLFile(@NonNull String publicID) {
        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            return new File(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "storage" + File.separator + publicID + File.separator + publicID + ".yml");
        } else {
            return new File(Configuration.getServerSurvivalServerPluginDatafolder() + File.separator + "storage" + File.separator + publicID + File.separator + publicID + ".yml");
        }
    }

    public static File getSchematicFile(@NonNull String publicID) {
        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            return new File(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "storage" + File.separator + publicID + File.separator + publicID + ".schematic");
        } else {
            return new File(Configuration.getServerSurvivalServerPluginDatafolder() + File.separator + "storage" + File.separator + publicID + File.separator + publicID + ".schematic");
        }
    }

    public static String getPluginFolderPath() {
        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            return BuildDreamRecode.getInstance().getDataFolder().toString();
        } else {
            return Configuration.getServerSurvivalServerPluginDatafolder();
        }
    }

    public static File getStorageFolder() {
        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            return new File(BuildDreamRecode.getInstance().getDataFolder().toString() + File.separator + "storage");
        } else {
            return new File(Configuration.getServerSurvivalServerPluginDatafolder() + File.separator + "storage");
        }
    }

    public static void checkFileOrCreate(@NonNull File file) throws IOException {
        if (!file.exists()) {
            if (file.isDirectory()) {
                file.mkdir();
            } else if (file.isFile()) {
                file.createNewFile();
            }
        }
    }

    @Getter
    public static ArrayList<String> DreamList = new ArrayList<>();

    public static void loadToDreamList() throws IOException, InvalidConfigurationException {
        YamlConfiguration storage = new YamlConfiguration();
        File file = Storage.getStorageFolder();
        File[] fs = file.listFiles();
        if (!(fs == null)) {
            for (File f : fs) {
                if (f.isDirectory()) {
                    storage.load(Storage.getStorageYAMLFile(f.getName()));
                    String publicID = storage.getString("PublicID");
                    getDreamList().add(publicID);
                }
            }
        }
    }
}

