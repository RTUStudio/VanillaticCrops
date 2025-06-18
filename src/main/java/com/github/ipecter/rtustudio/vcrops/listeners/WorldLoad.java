package com.github.ipecter.rtustudio.vcrops.listeners;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.crop.CropBlock;
import com.github.ipecter.rtustudio.vcrops.data.BlockPos;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldLoad extends RSListener<VanillaticCrops> {

    public WorldLoad(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLoad(WorldLoadEvent e) {
        getPlugin().getCropManager().addWorld(e.getWorld());
    }

    @EventHandler
    public void onUnload(WorldUnloadEvent e) {
        getPlugin().getCropManager().removeWorld(e.getWorld().getUID());
    }
}
