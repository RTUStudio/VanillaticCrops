package com.github.ipecter.rtustudio.vcrops.listeners;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.crop.CropWorld;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ChunkLoad extends RSListener<VanillaticCrops> {

    public ChunkLoad(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLoad(ChunkLoadEvent e) {
        CropWorld world = getPlugin().getCropManager().getWorld(e.getWorld().getUID());
        if (world == null) return;
        world.loadChunk(e.getChunk());
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent e) {
        CropWorld world = getPlugin().getCropManager().getWorld(e.getWorld().getUID());
        if (world == null) return;
        world.unloadChunk(e.getChunk());
    }
}
