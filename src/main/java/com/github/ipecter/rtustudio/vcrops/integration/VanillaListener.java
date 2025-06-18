package com.github.ipecter.rtustudio.vcrops.integration;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.configuration.CropConfig;
import com.github.ipecter.rtustudio.vcrops.crop.Crop;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import kr.rtuserver.framework.bukkit.api.registry.CustomItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VanillaListener extends RSListener<VanillaticCrops> {

    private final CropConfig config;

    public VanillaListener(VanillaticCrops plugin) {
        super(plugin);
        this.config = plugin.getCropConfig();
    }

    @EventHandler
    protected void onInteractSeed(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) return;
        ItemStack item = e.getItem();
        if (item == null) return;
        BlockFace face = e.getBlockFace();
        if (face == BlockFace.UP) {
            if (block.getType() == Material.FARMLAND) {
                Location location = block.getRelative(BlockFace.UP).getLocation();
                place(location, item);
            }
        } else {
            Block air = block.getRelative(face.getOppositeFace());
            if (air.getRelative(BlockFace.DOWN).getType() == Material.FARMLAND) {
                place(air.getLocation(), item);
            }
        }
    }

    private void place(Location location, ItemStack itemStack) {
        String id = CustomItems.to(itemStack);
        if (id.isEmpty()) return;
        Crop crop = config.getFromSeed(id);
        if (crop == null) return;
        getPlugin().getCustomProvider().place(location, crop.stages().getFirst());
    }
}
