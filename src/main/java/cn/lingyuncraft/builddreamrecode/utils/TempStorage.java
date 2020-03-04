package cn.lingyuncraft.builddreamrecode.utils;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class TempStorage {

    @Getter
    public static HashMap<UUID, Location> tempPos1 = new HashMap<>();
    @Getter
    public static HashMap<UUID, Location> tempPos2 = new HashMap<>();
}
