package com.github.ipecter.rtustudio.vcrops.integration.abstracts;

import org.bukkit.Location;

public abstract class CustomProvider {

    public abstract void place(Location location, String id);
    public abstract boolean remove(Location location);

}
