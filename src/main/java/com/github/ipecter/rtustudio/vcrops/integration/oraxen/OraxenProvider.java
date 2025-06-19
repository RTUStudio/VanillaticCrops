package com.github.ipecter.rtustudio.vcrops.integration.oraxen;

import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.nexomc.nexo.api.NexoFurniture;
import io.th0rgal.oraxen.api.OraxenFurniture;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

import java.util.Comparator;
import java.util.Optional;

public class OraxenProvider extends CustomProvider {

    @Override
    public Location place(Location location, String id) {
        NamespacedKey key = NamespacedKey.fromString(id);
        Entity entity =  OraxenFurniture.place(key == null ? id : key.getKey(), location, Rotation.NONE, BlockFace.UP);
        if (entity == null) return null;
        return entity.getLocation();
    }

    @Override
    public boolean remove(Location location) {
        Optional<Entity> entity = location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).stream()
                .filter(OraxenFurniture::isFurniture).min((o1, o2) -> {
                    double d1 = o1.getLocation().distance(location);
                    double d2 = o2.getLocation().distance(location);
                    return Double.compare(d1, d2);
                });
        return entity.filter(e -> OraxenFurniture.remove(e, null)).isPresent();
    }

}
