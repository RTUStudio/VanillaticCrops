package com.github.ipecter.rtustudio.vcrops.integration.nexo;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomListener;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.events.custom_block.NexoBlockPlaceEvent;
import com.nexomc.nexo.api.events.custom_block.stringblock.NexoStringBlockPlaceEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureInteractEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import com.nexomc.nexo.mechanics.furniture.hitbox.FurnitureHitbox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.Optional;

public class NexoListener extends CustomListener {

    public NexoListener(VanillaticCrops plugin) {
        super(plugin);
    }

    @EventHandler
    private void onBreak(NexoFurnitureBreakEvent e) {
        onBreak(e.getPlayer(), e.getBaseEntity().getLocation(), "nexo:" + e.getMechanic().getItemID());
    }

    @EventHandler
    private void onInteract(NexoFurnitureInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        Material material = item.getType();
        if (material.isAir()) return;
        if (material == Material.BONE_MEAL) {
            Location location = e.getBaseEntity().getLocation();
            BoundingBox bbb = e.getMechanic().getHitbox().interactionBoundingBoxes(location, 0).getFirst();
            boneMeal(player, bbb, location, "nexo:" + e.getMechanic().getItemID());
        }
    }
}