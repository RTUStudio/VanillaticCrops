package com.github.ipecter.rtustudio.vcrops.integration.nexo;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomListener;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class NexoListener extends CustomListener {

    public NexoListener(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    private void onPlace(NexoFurniturePlaceEvent e) {
        onPlace(e.getPlayer(), e.getBaseEntity().getLocation(), "nexo:" + e.getMechanic().getItemID());
    }

    @EventHandler
    private void onBreak(NexoFurnitureBreakEvent e) {
        onBreak(e.getPlayer(), e.getBaseEntity().getLocation(), "nexo:" + e.getMechanic().getItemID());
    }

}