package com.github.ipecter.rtustudio.vcrops.integration.oraxen;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomListener;
import io.th0rgal.oraxen.api.events.furniture.OraxenFurnitureBreakEvent;
import io.th0rgal.oraxen.api.events.furniture.OraxenFurniturePlaceEvent;
import org.bukkit.event.EventHandler;

public class OraxenListener extends CustomListener {

    public OraxenListener(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    private void onPlace(OraxenFurniturePlaceEvent e) {
        onPlace(e.getPlayer(), e.getBaseEntity().getLocation(), "oraxen:" + e.getMechanic().getItemID());
    }

    @EventHandler
    private void onBreak(OraxenFurnitureBreakEvent e) {
        onBreak(e.getPlayer(), e.getBaseEntity().getLocation(), "oraxen:" + e.getMechanic().getItemID());
    }

}