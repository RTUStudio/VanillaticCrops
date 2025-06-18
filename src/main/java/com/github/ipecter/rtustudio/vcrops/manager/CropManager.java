package com.github.ipecter.rtustudio.vcrops.manager;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.crop.CropWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CropManager {

    private final VanillaticCrops plugin;

    public CropManager(VanillaticCrops plugin) {
        this.plugin = plugin;
        for (World world : Bukkit.getWorlds()) addWorld(world);
    }

    private final Map<UUID, CropWorld> worlds = new HashMap<>();

    public void addWorld(World world) {
        worlds.put(world.getUID(), new CropWorld(plugin, world));
    }

    public void removeWorld(UUID world) {
        worlds.remove(world).close();
    }

    public CropWorld getWorld(UUID world) {
        return worlds.get(world);
    }

}
