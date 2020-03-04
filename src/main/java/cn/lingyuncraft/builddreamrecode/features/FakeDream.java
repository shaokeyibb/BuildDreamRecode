package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.utils.WorldEditUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class FakeDream {

    public static void sendFakeDream(Player player,String publicID){
        Clipboard clip = WorldEditUtils.getSchematicFromFile(publicID);
        Region region = clip.getRegion();
        for (BlockVector3 v : region) {
            BlockData data = BukkitAdapter.adapt(clip.getBlock(v));
            Location location = new Location(player.getWorld(),player.getLocation().getBlockX()+v.getBlockX()-region.getMinimumPoint().getBlockX(),player.getLocation().getBlockY()+v.getBlockY()-region.getMinimumPoint().getBlockY(),player.getLocation().getBlockZ()+v.getBlockZ()-region.getMinimumPoint().getBlockZ());
            player.sendBlockChange(location,data);
        }
        player.sendMessage("筑梦系统>> 公众ID为"+publicID+"的筑梦预览已释放，如需取消预览请重新连接到服务器");
    }
}
