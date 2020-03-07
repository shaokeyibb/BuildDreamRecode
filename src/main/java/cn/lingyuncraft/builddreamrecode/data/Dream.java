package cn.lingyuncraft.builddreamrecode.data;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.data.PData;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.flags.Timestamp;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.I18n;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Dream implements PData, Timestamp {

    public String publicID;
    public UUID author;
    public double cost;
    public String description;
    public boolean redstone;
    public boolean publicMode;
    public double fee;
    private PPlugin plugin;
    private PID id;
    private File file;
    private double version;
    private long buildTime;
    private Map<Material, Integer> blocks;

    public Dream(String publicID, UUID author, double cost, String description, boolean redstone, boolean publicMode, double fee, Map<Material, Integer> blocks) {
        this.plugin = BuildDreamRecode.getInstance();

        this.version = 2.0D;
        this.publicID = publicID;
        this.author = author;
        this.cost = cost;
        this.description = description;
        this.redstone = redstone;
        this.publicMode = publicMode;
        this.fee = fee;
        this.buildTime = System.currentTimeMillis();
        this.blocks = blocks;

        save();
    }

    public Dream(PID id, @NonNull File file) {
        this.plugin = BuildDreamRecode.getInstance();

        this.id = id;
        this.file = file;
        load(file);
    }

    @Override
    public String getTypeName() {
        return "梦境数据(" + getFileName() + ")";
    }

    @Override
    public String getFileName() {
        return BasicUtil.getNoExFileName(file.getName());
    }

    @Override
    public void load(@NonNull File file) {
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        boolean check = data.getKeys(false).containsAll(Arrays.asList(
                "Version",
                "author",
                "Cost",
                "Description",
                "HasRedstone",
                "IsPublic",
                "PublicBuyFee",
                "SavedTime",
                "BlockTotal"
        ));
        if (check) {
            this.version = data.getDouble("Version");
            this.publicID = getFileName();
            this.author = UUID.fromString(data.getString("author"));
            this.cost = data.getDouble("Cost");
            this.description = data.getString("Description");
            this.redstone = data.getBoolean("HasRedstone");
            this.publicMode = data.getBoolean("IsPublic");
            this.fee = data.getDouble("PublicBuyFee");
            this.buildTime = data.getLong("SavedTime");

            this.blocks = new HashMap<>();
            try {
                ConfigurationSection blocks = data.getConfigurationSection("BlockTotal");
                for (String material : blocks.getKeys(false)) {
                    this.blocks.put(Material.valueOf(material.toUpperCase()), blocks.getInt(material));
                }
            } catch (Throwable e) {
                plugin.lang.logError(I18n.LOAD, getTypeName(), e.toString());
            }
        } else {
            plugin.lang.logError(I18n.LOAD, getTypeName(), "数据文件不完整.");
        }
    }

    @Override
    public void reload() {
        load(this.file);
    }

    @Override
    public void save() {
        if (file == null) {
            File folder = new File(Storage.get().getFolder(), publicID);
            this.file = new File(folder, publicID + ".yml");
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        data.set("Version", this.version);
        data.set("author", this.author.toString());
        data.set("Cost", this.cost);
        data.set("Description", this.description);
        data.set("HasRedstone", this.redstone);
        data.set("IsPublic", this.publicMode);
        data.set("PublicBuyFee", this.fee);
        data.set("SavedTime", this.buildTime);

        ConfigurationSection blocks = data.createSection("BlockTotal");
        for (Material material : this.blocks.keySet()) {
            blocks.set(material.toString(), this.blocks.get(material));
        }

        try {
            data.save(file);
        } catch (IOException e) {
            plugin.lang.logError(I18n.SAVE, getTypeName(), e.toString());
            e.printStackTrace();
        }
    }

    public List<String> info() {
        String[] info = {
                "&7梦境 &c" + this.publicID + " &7▶",
                "&7释放梦境费用: &c" + this.cost,
                "&7描述: &c" + this.description,
                "&7----------"
        };
        List<String> result = new ArrayList<>(Arrays.asList(info));
        result.replaceAll(I18n::color);
        return result;
    }

    @Override
    public void init() {
        plugin.lang.logError(I18n.SAVE, getTypeName(), "尝试调用 init() 方法.");
    }

    @Override
    public void saveDefault() {
        plugin.lang.logError(I18n.SAVE, getTypeName(), "尝试调用 saveDefault() 方法.");
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(@NonNull File file) {
        this.file = file;
    }

    @Override
    public PID getID() {
        return id;
    }

    @Override
    public void setID(@NonNull PID pid) {
        this.id = pid;
    }

    @Override
    public long getTimestamp() {
        return buildTime;
    }

    @Override
    public void setTime(long l) {
        this.buildTime = l;
    }
}
