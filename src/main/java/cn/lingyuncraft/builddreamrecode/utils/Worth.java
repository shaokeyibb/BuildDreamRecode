package cn.lingyuncraft.builddreamrecode.utils;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import lombok.NonNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.config.PConfig;

public class Worth extends PConfig {

    private static Worth worth;

    public Worth(@NonNull PPlugin plugin) {
        super(plugin, "Worth", "筑梦价格表文件");
    }

    public static Worth get() {
        if (worth == null) {
            worth = new Worth(BuildDreamRecode.getInstance());
        }
        return worth;
    }

    @Override
    public void saveDefault() {
        plugin.saveResource("Worth.yml", false);
    }
}
