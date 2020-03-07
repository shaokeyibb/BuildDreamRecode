package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.WorldEditUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.I18n;

public class FakeDream {

    public static void sendFakeDream(Player user, String publicID) {
        PPlugin plugin = BuildDreamRecode.getInstance();
        Clipboard clip = WorldEditUtils.getSchematicFromFile(publicID);
        Region region = clip.getRegion();

        for (BlockVector3 v : region) {
            BlockData data = BukkitAdapter.adapt(clip.getBlock(v));
            Location location = new Location(user.getWorld(), user.getLocation().getBlockX() + v.getBlockX() - region.getMinimumPoint().getBlockX(), user.getLocation().getBlockY() + v.getBlockY() - region.getMinimumPoint().getBlockY(), user.getLocation().getBlockZ() + v.getBlockZ() - region.getMinimumPoint().getBlockZ());
            user.sendBlockChange(location, data);
        }

        I18n.send(user, plugin.lang.build(plugin.localeKey, I18n.Type.INFO, "公众ID为 &c" + publicID + " &7的筑梦预览已释放, 如需取消预览请重新连接到服务器."));
    }
}
