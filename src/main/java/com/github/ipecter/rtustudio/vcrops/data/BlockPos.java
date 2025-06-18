package com.github.ipecter.rtustudio.vcrops.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

// https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Data_types#Position
public record BlockPos(int x, int y, int z) {

    public BlockPos(long packed) {
        //this((int) ((packed << 37) >> 37), (int) (packed >>> 54), (int) ((packed << 10) >> 37)); deprecated
        this((int) (packed >> 38), (int) (packed << 52 >> 52), (int) (packed << 26 >> 38));
    }

    public BlockPos(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockPos(Block block) {
        this(block.getX(), block.getY(), block.getZ());
    }

    // Not compatible with vanilla
    public long getBlockKey() {
        //return ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27) | ((long) y << 54); deprecated
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF);
    }

    // from Bukkit API
    public long getChunkKey() {
        return ((long) (z >> 4) << 32) | ((x >> 4) & 0xFFFFFFFFL);
    }

    public Location toLocation(World world) {
        return new Location(world, x + 0.5, y + 0.5, z + 0.5);
    }

}
