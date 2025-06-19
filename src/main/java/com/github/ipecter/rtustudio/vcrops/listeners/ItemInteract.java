package com.github.ipecter.rtustudio.vcrops.listeners;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteract extends RSListener<VanillaticCrops> {

    public ItemInteract(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {

    }
}
