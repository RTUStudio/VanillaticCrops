package com.github.ipecter.rtustudio.vcrops.crop;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.data.BlockPos;
import com.github.ipecter.rtustudio.vcrops.data.ChunkPos;
import kr.rtuserver.framework.bukkit.api.core.scheduler.ScheduledTask;
import kr.rtuserver.framework.bukkit.api.scheduler.CraftScheduler;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class CropWorld {

    private final Map<Long, CropChunk> chunks = new HashMap<>();

    private final VanillaticCrops plugin;
    private final NamespacedKey blockKey;

    private final World world;
    private final Scheduler scheduler;

    public CropWorld(VanillaticCrops plugin, World world) {
        this.plugin = plugin;
        this.blockKey = new NamespacedKey(plugin, "block");

        this.world = world;
        this.scheduler = new Scheduler(plugin);

        for (Chunk chunk : world.getLoadedChunks()) loadChunk(chunk);
    }

    public void loadChunk(Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(blockKey)) {
            if (world.isChunkLoaded(chunk.getX(), chunk.getZ())) {
                chunks.put(chunk.getChunkKey(), new CropChunk(plugin, world, chunk));
            }
        }
    }

    public void unloadChunk(Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(blockKey)) {
            ChunkPos pos = new ChunkPos(chunk.getX(), chunk.getZ());
            chunks.remove(pos.getChunkKey());
        }
    }

    public CropBlock getBlock(long position) {
        CropChunk cc = chunks.computeIfPresent(new BlockPos(position).getChunkKey(), (key, value) -> {
            if (value.isEmpty()) return null;
            return value;
        });
        if (cc == null) return null;
        return cc.getBlock(position);
    }

    public void addBlock(CropBlock block) {
        long position = block.getPosition().getChunkKey();
        chunks.compute(position, (key, value) -> {
            if (value == null) return new CropChunk(plugin, world, world.getChunkAt(position));
            else return value;
        }).addBlock(block);
    }

    public void removeBlock(long position) {
        chunks.computeIfPresent(new BlockPos(position).getChunkKey(), (key, value) -> {
            value.removeBlock(position);
            if (value.isEmpty()) return null;
            return value;
        });
    }

    public void randomTick(int randomTickSpeed) {
        for (CropChunk chunk : chunks.values()) {
            if (world.isChunkLoaded(chunk.getPosition().x(), chunk.getPosition().z())) {
                chunk.randomTick(randomTickSpeed);
            }
        }
    }

    public void close() {
        scheduler.getScheduler().cancel();
        chunks.clear();
    }

    @Getter
    public class Scheduler implements Runnable {

        private final ScheduledTask scheduler;

        public Scheduler(VanillaticCrops plugin) {
            this.scheduler = CraftScheduler.runTimer(plugin, this, 1, 1);
        }

        @Override
        public void run() {
            World world = Bukkit.getWorld(getWorld().getUID());
            if (world == null) {
                scheduler.cancel();
                System.out.println("cancelled");
                return;
            }
            int randomTickSpeed = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED).intValue();
            if (randomTickSpeed > 0) randomTick(randomTickSpeed);
        }
    }

}
