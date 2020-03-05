package cn.lingyuncraft.builddreamrecode.features;

import cn.lingyuncraft.builddreamrecode.BuildDreamRecode;
import cn.lingyuncraft.builddreamrecode.data.Dream;
import cn.lingyuncraft.builddreamrecode.utils.Configuration;
import cn.lingyuncraft.builddreamrecode.utils.Logger;
import cn.lingyuncraft.builddreamrecode.utils.Storage;
import cn.lingyuncraft.builddreamrecode.utils.WorldEditUtils;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import lombok.NonNull;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.serverct.parrot.parrotx.utils.LocaleUtil;

import java.io.IOException;
import java.util.*;

public class DreamControl {

    public static void buildDream(@NonNull String publicID, @NonNull org.bukkit.World world, @NonNull Location loc1, @NonNull Location loc2, @NonNull UUID author, @NonNull String description, @NonNull boolean isPublic, @NonNull double publicBuyFee) throws WorldEditException, IOException, InvalidConfigurationException {
        BuildDreamRecode plugin = (BuildDreamRecode) BuildDreamRecode.getInstance();
        com.sk89q.worldedit.world.World WEWorld = BukkitAdapter.adapt(world);
        BlockVector3 min = BlockVector3.at(loc1.getX(), loc1.getY(), loc1.getZ());
        BlockVector3 max = BlockVector3.at(loc2.getX(), loc2.getY(), loc2.getZ());
        CuboidRegion region = new CuboidRegion(WEWorld, min, max);

        boolean pass = true;

        double cost = 0;
        boolean hasRedstone = false;
        Player user = Bukkit.getPlayer(author);
        Map<Material, Integer> blocks = new HashMap<>();
        List<Material> materials = new ArrayList<>();
        List<Material> inventoryBlocks = new ArrayList<>();

        if (user == null) {
            plugin.lang.logError("筑梦", publicID, "作者 Player 对象为 null.");
            return;
        }

        if (Configuration.GAMEMODE.equals("SURVIVAL")) {
            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "生存服无法筑梦, 请前往创造服筑梦."));
        } else {
            for (BlockVector3 v : region) {
                Material material = BukkitAdapter.adapt(world, v).getBlock().getType();

                if (Configuration.CHECK_BEDROCK && !user.hasPermission("BuildDream.bypass.BEDROCK") && material == Material.BEDROCK) {
                    pass = false;
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在基岩, 筑梦失败."));
                    break;
                }
                if (Configuration.CHECK_BARRIER && !user.hasPermission("BuildDream.bypass.BARRIER") && material == Material.BARRIER) {
                    pass = false;
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在屏障, 筑梦失败."));
                    break;
                }
                if (!user.hasPermission("BuildDream.bypass.COMMANDBLOCK")) {
                    if (material == Material.COMMAND_BLOCK || material == Material.CHAIN_COMMAND_BLOCK || material == Material.REPEATING_COMMAND_BLOCK) {
                        pass = false;
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在命令方块, 筑梦失败."));
                        break;
                    } else if (material == Material.STRUCTURE_BLOCK || material == Material.STRUCTURE_VOID) {
                        pass = false;
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在结构方块, 筑梦失败."));
                        break;
                    }
                }
                if (!user.hasPermission("BuildDream.bypass.ENDER") && material == Material.END_PORTAL_FRAME) {
                    pass = false;
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在末地传送门, 筑梦失败."));
                    break;
                }
                if (!user.hasPermission("BuildDream.bypass.CHEST")) {
                    if (BukkitAdapter.adapt(world, v).getBlock().getState() instanceof InventoryHolder) {
                        pass = false;
                        if (!inventoryBlocks.contains(material)) {
                            inventoryBlocks.add(material);
                            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在可存放物品的方块(&c" + material.name() + "&7), 筑梦失败."));
                            continue;
                        }
                    }
                }
                if (!user.hasPermission("BuildDream.bypass.ORE")) {
                    switch (material) {
                        case REDSTONE_ORE:
                        case COAL_ORE:
                        case DIAMOND_ORE:
                        case EMERALD_ORE:
                        case GOLD_ORE:
                        case IRON_ORE:
                        case LAPIS_ORE:
                        case NETHER_QUARTZ_ORE:
                            pass = false;
                            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在矿物, 筑梦失败."));
                            break;
                    }
                }
                if (Configuration.CHECK_REDSTONE && !user.hasPermission("BuildDream.bypass.REDSTONE")) {
                    switch (material) {
                        case REDSTONE:
                        case REDSTONE_BLOCK:
                        case REDSTONE_TORCH:
                        case REDSTONE_WALL_TORCH:
                        case REDSTONE_LAMP:
                        case REDSTONE_WIRE:
                            hasRedstone = true;
                    }
                }

                blocks.put(material, blocks.getOrDefault(material, 0) + 1);


                if (Configuration.price(material) == -1) {
                    pass = false;
                    if (!materials.contains(material)) {
                        materials.add(material);
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "区域内存在未注册至筑梦价格表的方块(&c" + material + "&7), 筑梦失败"));
                    }
                    continue;
                }
                cost += Configuration.price(material);
            }
            if (hasRedstone) {
                cost += Configuration.PRICE_REDSTONE;
            }
            cost *= Configuration.PRICE_BALANCE;
            if (!pass) {
                return;
            }
            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.INFO, "方块价值统计:"));
            for (Material material : blocks.keySet()) {
                double single = Configuration.price(material);
                double total = single * blocks.get(material);
                String report = "  &c" + material + " &7▶ " + "&c" + blocks.get(material) + " &7块, " + "共 &c" + total + " &7金币. (" + single + "/块)";
                plugin.lang.send(user, plugin.lang.color(report));
            }

            double money = plugin.getVaultUtil().getBalances(user);
            double buildCost = Configuration.PRICE_BUILDDREAM;
            if (money < buildCost && !user.hasPermission("BuildDream.bypass.BuildDreamPrice")) {
                plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "筑梦失败, 您没有足够的金币支付 &c" + buildCost + " &7金币的筑梦手续费, 您当前拥有 &c" + money + " &7金币."));
            } else {
                plugin.getVaultUtil().take(user, buildCost);

                WorldEditUtils.saveSchematicToFile(publicID, region);
                Storage.get().save(new Dream(publicID, author, cost, description, hasRedstone, isPublic, publicBuyFee, blocks));
                Logger.get().logBuild(publicID, user);

                money = plugin.getVaultUtil().getBalances(user);
                plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.INFO, "筑梦成功, 释放梦境价格共 &c" + cost + " &7金币, 扣除筑梦手续费 &c" + buildCost + " &7金币后您当前剩余 &c" + money + " &7金币."));
            }
        }
    }

    public static void releaseDream(@NonNull String publicID, @NonNull UUID author) {
        BuildDreamRecode plugin = (BuildDreamRecode) BuildDreamRecode.getInstance();
        Player user = Bukkit.getPlayer(author);

        if (user == null) {
            plugin.lang.logError("筑梦", publicID, "作者 Player 对象为 null.");
            return;
        }

        if (Configuration.GAMEMODE.equals("CREATIVE")) {
            plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "请勿在创造服释放梦境, 请前往生存服释放."));
        } else {
            Dream dream = Storage.get().get(publicID);
            if (dream == null) {
                plugin.lang.logError("筑梦", publicID, "Dream 对象为 null.");
                return;
            }
            double cost = dream.cost;
            boolean hasRedstone = dream.redstone;
            double money = plugin.getVaultUtil().getBalances(user);
            if (money < cost) {
                if (hasRedstone) {
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "释放梦境失败, 您没有足够的金币支付 &c" + cost + " &7金币的释放梦境费用(&c包含红石附加费&7), 您当前拥有 &c" + money + " &7金币."));
                } else {
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "释放梦境失败, 您没有足够的金币支付 &c" + cost + " &7金币的释放梦境费用, 您当前拥有 &c" + money + " &7金币."));
                }
            } else {
                boolean pass = true;
                BlockVector3 min = WorldEditUtils.getSchematicFromFile(publicID).getMinimumPoint();
                BlockVector3 max = WorldEditUtils.getSchematicFromFile(publicID).getMaximumPoint();
                BlockVector3 max2 = max.subtract(min);
                World WEWorld = BukkitAdapter.adapt(user.getWorld());
                BlockVector3 realMin = BukkitAdapter.adapt(user.getLocation()).toVector().toBlockPoint();
                BlockVector3 realMax = BlockVector3.at(max2.getBlockX() + realMin.getBlockX(), max2.getBlockY() + realMin.getBlockY(), max2.getBlockZ() + realMin.getBlockZ());
                CuboidRegion region = new CuboidRegion(WEWorld, realMin, realMax);
                for (BlockVector3 vector3 : region) {
                    Material material = BukkitAdapter.adapt(user.getWorld(), vector3).getBlock().getType();
                    List<Material> checkList = new ArrayList<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER, Material.LAVA));
                    if (!checkList.contains(material)) {
                        plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.WARN, "由于周围有方块阻挡, 梦境释放失败."));
                        pass = false;
                        break;
                    }
                }
                if (pass) {
                    WorldEditUtils.pasteSchematic(publicID, user.getLocation());
                    for (BlockVector3 vector3 : region) {
                        Material material = BukkitAdapter.adapt(user.getWorld(), vector3).getBlock().getType();
                        CoreProtectAPI coreProtectAPI = plugin.getCoreProtect();
                        if (coreProtectAPI != null) {
                            Location loc = new Location(user.getWorld(), vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
                            coreProtectAPI.logPlacement("#[BuildDream]" + user.getDisplayName(), loc, material, loc.getBlock().getBlockData());
                        } else {
                            plugin.lang.logError(LocaleUtil.LOAD, "CoreProtect API", "对象为 null.");
                        }
                    }
                    Logger.get().logRelease(publicID, user);
                    plugin.getVaultUtil().take(user, cost);
                    money = plugin.getVaultUtil().getBalances(user);
                    plugin.lang.send(user, plugin.lang.build(plugin.localeKey, LocaleUtil.Type.INFO, "释放梦境成功, 扣除释放梦境费用共 &c" + cost + " &7金币, 您当前剩余 &c" + money + " &7金币."));
                }
            }
        }
    }
}
