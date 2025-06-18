package com.github.ipecter.rtustudio.vcrops.data;


import org.bukkit.Chunk;

public record ChunkPos(int x, int z) {

    public ChunkPos(long packed) {
        this((int) packed, (int) (packed >> 32));
    }

    public ChunkPos(Chunk chunk) {
        this(chunk.getX(), chunk.getZ());
    }

    public long getChunkKey() {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

}
