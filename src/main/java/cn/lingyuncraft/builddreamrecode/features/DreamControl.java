package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.utils.*;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DreamControl {

    public static void buildDream(@NotNull String publicID, @NotNull org.bukkit.World world, @NotNull Location loc1, @NotNull Location loc2, @NotNull UUID author, @NotNull String description, @NotNull boolean isPublic, @NotNull double publicBuyFee) throws WorldEditException, IOException, InvalidConfigurationException {
        com.sk89q.worldedit.world.World WEWorld = BukkitAdapter.adapt(world);
        BlockVector3 min = BlockVector3.at(loc1.getX(), loc1.getY(), loc1.getZ());
        BlockVector3 max = BlockVector3.at(loc2.getX(), loc2.getY(), loc2.getZ());
        CuboidRegion region = new CuboidRegion(WEWorld, min, max);

        boolean checked = true;

        double cost = 0;
        boolean hasRedstone = false;
        Player p = Bukkit.getPlayer(author);
        HashMap<Material, Integer> mmap = new HashMap<>();
        ArrayList<Material> mlist = new ArrayList<>();

        Economy eco = new Economy();

        if (Configuration.getServerServerType().equals("SURVIVAL")) {
            p.sendMessage("筑梦系统>> 请勿在生存服筑梦，请前往创造服筑梦");
        } else {
            for (BlockVector3 v : region) {
                Material m = BukkitAdapter.adapt(world, v).getBlock().getType();

                if (Configuration.isCheckEnableBedrockCheck() && !p.hasPermission("BuildDream.bypass.Bedrock") && m == Material.BEDROCK) {
                    checked = false;
                    p.sendMessage("筑梦系统>> 区域内存在基岩，筑梦失败");
                    break;
                }
                if (Configuration.isCheckEnableBarrierCheck() && !p.hasPermission("BuildDream.bypass.BARRIER") && m == Material.BARRIER) {
                    checked = false;
                    p.sendMessage("筑梦系统>> 区域内存在屏障，筑梦失败");
                    break;
                }
                if (!p.hasPermission("BuildDream.bypass.COMMANDBLOCK")) {
                    if (m == Material.COMMAND_BLOCK || m == Material.CHAIN_COMMAND_BLOCK || m == Material.REPEATING_COMMAND_BLOCK) {
                        checked = false;
                        p.sendMessage("筑梦系统>> 区域内存在命令方块，筑梦失败");
                        break;
                    }
                }
                if (!p.hasPermission("BuildDream.bypass.COMMANDBLOCK")) {
                    if (m == Material.STRUCTURE_BLOCK || m == Material.STRUCTURE_VOID) {
                        checked = false;
                        p.sendMessage("筑梦系统>> 区域内存在结构方块，筑梦失败");
                        break;
                    }
                }
                if (!p.hasPermission("BuildDream.bypass.ender") && m == Material.END_PORTAL_FRAME) {
                    checked = false;
                    p.sendMessage("筑梦系统>> 区域内存在末地传送门，筑梦失败");
                    break;
                }
                if (!p.hasPermission("BuildDream.bypass.CHEST")) {
                    if (BukkitAdapter.adapt(world, v).getBlock().getState() instanceof InventoryHolder) {
                        checked = false;
                        p.sendMessage("筑梦系统>> 区域内存在可存放物品的方块" + m.name() + "，筑梦失败");
                        continue;
                    }
                }
                if (!p.hasPermission("BuildDream.bypass.Ore")) {
                    switch (m) {
                        case REDSTONE_ORE:
                        case COAL_ORE:
                        case DIAMOND_ORE:
                        case EMERALD_ORE:
                        case GOLD_ORE:
                        case IRON_ORE:
                        case LAPIS_ORE:
                        case NETHER_QUARTZ_ORE:
                            checked = false;
                            p.sendMessage("筑梦系统>> 区域内存在矿物，筑梦失败");
                            break;
                    }
                }
                if (Configuration.isCheckEnableRedstoneCheck() && !p.hasPermission("BuildDream.bypass.Redstone")) {
                    switch (m) {
                        case REDSTONE:
                        case REDSTONE_BLOCK:
                        case REDSTONE_TORCH:
                        case REDSTONE_WALL_TORCH:
                        case REDSTONE_LAMP:
                        case REDSTONE_WIRE:
                            hasRedstone = true;
                    }
                }

                if (!mmap.containsKey(m)){
                    mmap.put(m,1);
                }else{
                    mmap.put(m,mmap.get(m)+1);
                }


                if (eco.getBuyPrice(new ItemStack(m)) == -1) {
                    checked = false;
                    if(!mlist.contains(m)){
                        mlist.add(m);
                        p.sendMessage("筑梦系统>> 区域内存在未注册至筑梦价格表的方块" + m + "，筑梦失败");
                    }
                    continue;
                }
                cost = cost + eco.getBuyPrice(new ItemStack(m));
            }
            if (hasRedstone) {
                cost = cost + Configuration.getPriceRedstoneExPrice();
            }
            cost = cost * Configuration.getPriceBalanceExPriceMultiple();
            if (checked) {
                p.sendMessage("筑梦系统>> 方块价值统计");
                for (Material m: mmap.keySet()){
                    p.sendMessage("方块ID："+m+ "，数量："+mmap.get(m)+"，价格："+eco.getBuyPrice(new ItemStack(m))*mmap.get(m)+"\n");
                }
                if (!Economy.hasBalance(author, Configuration.getPriceBuildDreamPrice()) && !p.hasPermission("BuildDream.bypass.BuildDreamPrice")) {
                    p.sendMessage("筑梦系统>> 筑梦失败，您没有足够的金币支付" + Configuration.getPriceBuildDreamPrice() + "金币的筑梦手续费，您当前拥有" + Economy.getBalance(author));
                } else {
                    WorldEditUtils.saveSchematicToFile(publicID, region);
                    Storage.saveStorageYAMLFile(publicID, author, cost, description, hasRedstone, isPublic, publicBuyFee,mmap);
                    Logger.logBuild(publicID, author);
                    Economy.addBalance(author, -Configuration.getPriceBuildDreamPrice());
                    p.sendMessage("筑梦系统>> 筑梦成功，释放梦境价格为" + cost + "金币，扣除筑梦手续费" + Configuration.getPriceBuildDreamPrice() + "金币,当前剩余" + Economy.getBalance(author));
                }
            }
        }
    }

    public static void releaseDream(@NotNull String publicID, @NotNull UUID author) throws IOException, InvalidConfigurationException {

        Player p = Bukkit.getPlayer(author);

        if (Configuration.getServerServerType().equals("CREATIVE")) {
            p.sendMessage("筑梦系统>> 请勿在创造服释放梦境，请前往生存服释放");
        } else {
            YamlConfiguration storage = new YamlConfiguration();
            storage.load(Storage.getStorageYAMLFile(publicID));
            double cost = storage.getDouble("Cost");
            boolean hasRedstone = storage.getBoolean("HasRedstone");
            if (!Economy.hasBalance(author, cost)) {
                if (hasRedstone) {
                    p.sendMessage("筑梦系统>> 释放梦境失败，您没有足够的金币支付" + cost + "金币的释放梦境费用（包含红石附加费），您当前拥有" + Economy.getBalance(author));
                } else {
                    p.sendMessage("筑梦系统>> 释放梦境失败，您没有足够的金币支付" + cost + "金币的释放梦境费用，您当前拥有" + Economy.getBalance(author));
                }
            } else {
                boolean checked = true;
                BlockVector3 min = WorldEditUtils.getSchematicFromFile(publicID).getMinimumPoint();
                BlockVector3 max = WorldEditUtils.getSchematicFromFile(publicID).getMaximumPoint();
                BlockVector3 max2 = max.subtract(min);
                World WEWorld = BukkitAdapter.adapt(p.getWorld());
                BlockVector3 realMin = BukkitAdapter.adapt(p.getLocation()).toVector().toBlockPoint();
                BlockVector3 realMax = BlockVector3.at(max2.getBlockX() + realMin.getBlockX(), max2.getBlockY() + realMin.getBlockY(), max2.getBlockZ() + realMin.getBlockZ());
                CuboidRegion region = new CuboidRegion(WEWorld, realMin, realMax);
                for (BlockVector3 v : region) {
                    Material m = BukkitAdapter.adapt(p.getWorld(), v).getBlock().getType();
                    if (m != Material.AIR && m != Material.CAVE_AIR && m != Material.VOID_AIR && m != Material.WATER && m != Material.LAVA) {
                            p.sendMessage("筑梦系统>> 由于周围有方块阻挡，梦境释放失败");
                            checked = false;
                            break;
                    }
                }
                if (checked) {
                    WorldEditUtils.pasteSchematic(publicID, p.getLocation());
                    for (BlockVector3 v : region) {
                        Material m = BukkitAdapter.adapt(p.getWorld(), v).getBlock().getType();
                        BuildDreamRecode.getInstance().getCoreProtect().logPlacement("#[BuildDream]" + p.getDisplayName(), new Location(p.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ()), m, new Location(p.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ()).getBlock().getBlockData());
                    }
                    Logger.logRelease(publicID, author);
                    Economy.addBalance(author, -cost);
                    p.sendMessage("筑梦系统>> 释放梦境成功，扣除释放梦境费用" + cost + "金币,当前剩余" + Economy.getBalance(author));
                }
            }
        }
    }
}
