package com.github.ipecter.rtustudio.vcrops.integration.abstracts;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.configuration.CropConfig;
import com.github.ipecter.rtustudio.vcrops.crop.Crop;
import com.github.ipecter.rtustudio.vcrops.crop.CropBlock;
import com.github.ipecter.rtustudio.vcrops.crop.CropWorld;
import com.github.ipecter.rtustudio.vcrops.data.BlockPos;
import com.nexomc.nexo.utils.wrappers.EnchantmentWrapper;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import kr.rtuserver.framework.bukkit.api.registry.CustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class CustomListener extends RSListener<VanillaticCrops> {

    protected final CropConfig config;
    private final Random random = new Random();

    public CustomListener(VanillaticCrops plugin) {
        super(plugin);
        this.config = plugin.getCropConfig();
    }

    private CropWorld world(Location location) {
        return getPlugin().getCropManager().getWorld(location.getWorld().getUID());
    }

    protected void onPlace(Player player, Location location, String id) {
        CropWorld world = world(location);
        if (world == null) return;
        player.swingMainHand();
        world.addBlock(new CropBlock(getPlugin(), location.getWorld(), location.getChunk(), new BlockPos(location)));
    }

    protected void onBreak(Player player, Location location, String id) {
        CropWorld world = world(location);
        if (world == null) return;
        world.removeBlock(new BlockPos(location).getBlockKey());
        drop(player, location, id);
    }

    protected void boneMeal(Player player, BoundingBox bb, Location location, String id) {
        CropWorld world = world(location);
        if (world == null) return;
        CropBlock block = world.getBlock(new BlockPos(location).getBlockKey());
        if (block == null) return;
        player.swingMainHand();
        if (block.grow(random.nextInt(4) + 2)) {
            spawnParticle(bb, location);
        }
    }

    private void spawnParticle(BoundingBox bb, Location location) {
        BlockPos pos = new BlockPos(location);
        double horizontalOffset = 0.5;
        double verticalOffset = bb.getHeight() / 2;
        double finalOffsetY = -0.0625 + (bb.getHeight() / 2);
        for (int i = 0; i < 15; i++) {
            double d = random.nextGaussian() * 0.02;
            double e = random.nextGaussian() * 0.02;
            double f = random.nextGaussian() * 0.02;
            double g = 0.5 - horizontalOffset;
            double h = (double) pos.x() + g + random.nextDouble() * horizontalOffset * 2.0;
            double j = (double) pos.y() + random.nextDouble() * verticalOffset;
            double k = (double) pos.z() + g + random.nextDouble() * horizontalOffset * 2.0;
            Location spawn = new Location(location.getWorld(), h, j + finalOffsetY, k);
            location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, spawn, 1, d, e, f);
        }
    }

//    public void spawnBoundingBoxParticle(World world, BoundingBox bb) {
//        Location offset = bb.getMin().toLocation(world);
//        offset.add(0,  -0.5625 + (bb.getHeight() / 2), 0);
//        Vector basisX = new Vector(Math.abs(bb.getMinX() - bb.getMaxX()), 0, 0);
//        Vector basisY = new Vector(0, Math.abs(bb.getMinY() - bb.getMaxY()), 0);
//        Vector basisZ = new Vector(0, 0, Math.abs(bb.getMinZ() - bb.getMaxZ()));
//
//        drawParticle(basisX, offset); //bottomFrontX
//        drawParticle(basisY, offset); //frontY
//        drawParticle(basisZ, offset); //bottomFrontZ
//        drawParticle(basisX, offset.clone().add(basisZ)); //bottomBackX
//        drawParticle(basisZ, offset.clone().add(basisX)); //bottomBackZ
//        drawParticle(basisY, offset.clone().add(basisX)); //rightY
//        drawParticle(basisY, offset.clone().add(basisZ)); //leftY
//        drawParticle(basisY, offset.clone().add(basisX).add(basisZ)); //backY
//        drawParticle(basisX, offset.clone().add(basisY)); //topFrontX
//        drawParticle(basisZ, offset.clone().add(basisY)); //topFrontZ
//        drawParticle(basisX, offset.clone().add(basisY).add(basisZ)); //topBackX
//        drawParticle(basisZ, offset.clone().add(basisY).add(basisX)); //topBackZ
//    }
//
//    private void drawParticle(Vector toDraw, Location offset) {
//        for (double d = 0; d <= toDraw.length() + 0.2/*looks better with 0.2 added for some reason*/; d += 0.01) {
//            Location toSpawn = offset.clone().add(toDraw.clone().multiply(d));
//            toSpawn.getWorld().spawnParticle(Particle.DUST, toSpawn, 2, 0, 0, 0, 0, new Particle.DustOptions(Color.GREEN, 0.4f));
//        }
//    }


    private void drop(Player player, Location location, String id) {
        Crop crop = config.getFromStage(id);
        if (crop == null) return;
        if (id.equals(crop.stages().getLast())) {
            dropItem(location, crop.plant());
            dropItem(location, crop.seed(), amount(player.getEquipment().getItemInMainHand()));
        } else dropItem(location, crop.seed());

    }

    private void dropItem(Location location, String id) {
        dropItem(location, id, 1);
    }

    private void dropItem(Location location, String id, int amount) {
        ItemStack itemStack = CustomItems.from(id);
        if (itemStack == null) return;
        itemStack.setAmount(amount);
        location.getWorld().dropItemNaturally(location, itemStack);
    }

    public int amount(ItemStack itemInHand) {
        if (itemInHand == null) return 1;
        ItemMeta meta = itemInHand.getItemMeta();
        if (meta != null) {
            switch (meta.getEnchantLevel(EnchantmentWrapper.FORTUNE)) {
                case 1 -> {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(1, 3.37);
                    map.put(2, 17.99);
                    map.put(3, 35.99);
                    map.put(4, 31.99);
                    map.put(5, 10.66);
                    return calculate(map);
                }
                case 2 -> {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(1, 1.45);
                    map.put(2, 9.64);
                    map.put(3, 25.70);
                    map.put(4, 34.27);
                    map.put(5, 22.85);
                    map.put(6, 6.09);
                    return calculate(map);
                }
                case 3 -> {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(1, 0.62);
                    map.put(2, 4.96);
                    map.put(3, 16.52);
                    map.put(4, 29.38);
                    map.put(5, 29.38);
                    map.put(6, 15.67);
                    map.put(7, 3.48);
                    return calculate(map);
                }
                default -> {
                    Map<Integer, Double> map = new HashMap<>();
                    map.put(1, 7.87);
                    map.put(2, 31.49);
                    map.put(3, 41.98);
                    map.put(4, 18.66);
                    return calculate(map);
                }
            }
        } else return 1;
    }


    private Integer calculate(Map<Integer, Double> weights) {
        int result = 1;
        double bestValue = Double.MAX_VALUE;
        for (Integer element : weights.keySet()) {
            if (element == null) continue;
            double value = -Math.log(random.nextDouble()) / weights.get(element);
            if (value < bestValue) {
                bestValue = value;
                result = element;
            }
        }
        return result;
    }

}
