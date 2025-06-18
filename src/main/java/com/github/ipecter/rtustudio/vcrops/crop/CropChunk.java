package com.github.ipecter.rtustudio.vcrops.crop;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.data.BlockPos;
import com.github.ipecter.rtustudio.vcrops.data.ChunkPos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.IntStream;

@Getter
@ToString
@RequiredArgsConstructor
public class CropChunk {

    private static final BlockPos[] subChunk = new BlockPos[4096];

    static {
        int index = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    subChunk[index++] = new BlockPos(x, y, z);
                }
            }
        }
    }

    private final VanillaticCrops plugin;
    private final NamespacedKey namespacedKey;

    private final World world;
    private final Chunk chunk;

    private final ChunkPos position;

    private final int worldMinHeight;

    private final Map<Integer, Set<Long>> subChunks = new HashMap<>();
    private final Map<Long, CropBlock> blocks = new HashMap<>();

    public CropChunk(VanillaticCrops plugin, World world, Chunk chunk) {
        this.plugin = plugin;
        this.namespacedKey = new NamespacedKey(plugin, "block");

        this.world = world;
        this.chunk = chunk;

        this.position = new ChunkPos(chunk);
        this.worldMinHeight = chunk.getWorld().getMinHeight();

        long[] positions = chunk.getPersistentDataContainer().get(namespacedKey, PersistentDataType.LONG_ARRAY);
        if (positions == null) return;
        for (long pos : positions) {
            BlockPos blockPos = new BlockPos(pos);
            CropBlock block = new CropBlock(plugin, world, chunk, blockPos); // chunk.getWorld().getBlockAt(blockPos.x(), blockPos.y(), blockPos.z()),
            subChunks.compute(index(block.getPosition().y()), (key, value) -> {
                if (value == null) value = new HashSet<>();
                value.add(pos);
                return value;
            });
            blocks.put(pos, block);
        }

    }

    private void addBlock(int index, CropBlock block) {
        BlockPos pos = block.getPosition();
        subChunks.compute(index, (key, value) -> {
            if (value == null) value = new HashSet<>();
            value.add(pos.getBlockKey());
            return value;
        });
        blocks.put(pos.getBlockKey(), block);
        System.out.println(blocks);
        Long[] positions = blocks.keySet().toArray(new Long[0]);
        chunk.getPersistentDataContainer().set(namespacedKey, PersistentDataType.LONG_ARRAY, ArrayUtils.toPrimitive(positions));
    }

    private int index(int y) {
        return (y - worldMinHeight) / 16;
    }

    public void addBlock(CropBlock block) {
        int index = index(block.getPosition().y());
        addBlock(index, block);
    }

    public void removeBlock(BlockPos pos) {
        removeBlock(pos.getBlockKey());
    }

    public void removeBlock(long pos) {
        int index = index(new BlockPos(pos).y());
        subChunks.computeIfPresent(index, (key, value) -> {
            value.remove(pos);
            if (value.isEmpty()) return null;
            return value;
        });
        blocks.remove(pos);
    }

    public boolean isEmpty() {
        return blocks.isEmpty();
    }

    public void randomTick(int randomTickSpeed) {
        for (int index : subChunks.keySet()) {
            IntStream stream = new Random().ints(randomTickSpeed, 0, 4096);
            int[] array = stream.toArray();
            for (int pos : array) {
                BlockPos sub = subChunk[pos];
                BlockPos blockPos = new BlockPos(sub.x() + (position.x() * 16), worldMinHeight + sub.y() + (index * 16), sub.z() + (position.z() * 16));
                tickBlock(blockPos);
            }
        }
    }

    private void tickBlock(BlockPos pos) {
        CropBlock crop = blocks.get(pos.getBlockKey());
        if (crop == null) return;
        Block block = chunk.getWorld().getBlockAt(pos.x(), pos.y(), pos.z());
        if (getRawBrightness(block) >= 9) {
            Block under = block.getRelative(BlockFace.DOWN);
            if (under.getBlockData() instanceof Farmland farmland) {
                float speed = farmland.getMoisture() > 0 ? 5.0f : 1.0f;
                int modifier = 100;
                if (getPlugin().getRandom().nextFloat() < (modifier / (100.0f * Math.floor((25.0F / speed) + 1)))) {
                    if (handleBlockGrowEvent(block)) if (!crop.grow()) {
                        removeBlock(pos);
                    }
                }
            }
        }
    }


    private int getRawBrightness(Block target) {
        final int sky = target.getLightFromSky();
        if (sky == 15) return 15;
        final int block = target.getLightFromBlocks();
        return Math.max(sky, block);
    }

    private boolean handleBlockGrowEvent(Block block) {
        BlockGrowEvent event = new BlockGrowEvent(block, block.getState());
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
}
