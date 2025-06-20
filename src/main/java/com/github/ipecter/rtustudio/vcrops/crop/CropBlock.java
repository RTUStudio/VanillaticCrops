package com.github.ipecter.rtustudio.vcrops.crop;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.configuration.CropConfig;
import com.github.ipecter.rtustudio.vcrops.data.BlockPos;
import kr.rtuserver.framework.bukkit.api.registry.CustomFurnitures;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

@Getter
@ToString
public class CropBlock {

    private final VanillaticCrops plugin;
    private final BlockPos position;

    private final World world;
    private final Chunk chunk;
    private final CropConfig config;

    public CropBlock(VanillaticCrops plugin, Location location) {
        this(plugin, location.getWorld(), location.getChunk(), new BlockPos(location));
    }

    public CropBlock(VanillaticCrops plugin, World world, Chunk chunk, BlockPos position) {
        this.plugin = plugin;

        this.world = world;
        this.chunk = chunk;

        this.position = position;
        this.config = plugin.getCropConfig();
    }

    public boolean grow() {
        return grow(1);
    }

    public boolean grow(int add) {
        Location loc = position.toLocation(world);
        String id = CustomFurnitures.to(loc);
        if (id == null) return false;
        Crop crop = config.getFromStage(id);
        if (crop == null) {
            System.out.println(id);
            return false;
        }
        List<String> stages = crop.stages();
        if (stages.getLast().equals(id)) return false;
        for (int i = 0; i < stages.size() - 1; i++) {
            String stage = stages.get(i);
            if (id.equals(stage)) {
                if (plugin.getCustomProvider().remove(loc)) {
                    plugin.getCustomProvider().place(loc, stages.get(Math.min(i + add, stages.size() - 1)));
                    return true;
                } else return false;
            }
        }
        return false;
    }

}
