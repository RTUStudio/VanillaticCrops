package com.github.ipecter.rtustudio.vcrops.integration.itemsadder;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomListener;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceSuccessEvent;
import org.bukkit.event.EventHandler;

public class ItemsAdderListener extends CustomListener {

    public ItemsAdderListener(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    private void onPlace(FurniturePlaceSuccessEvent e) {
        onPlace(e.getPlayer(), e.getBukkitEntity().getLocation(), "itemsadder:" + e.getNamespacedID());
    }

    @EventHandler
    private void onBreak(FurnitureBreakEvent e) {
        onBreak(e.getPlayer(), e.getBukkitEntity().getLocation(), "itemsadder:" + e.getNamespacedID());
    }
}
