package com.github.ipecter.rtustudio.vcrops.integration.nexo;

import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.nexomc.nexo.api.NexoFurniture;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

import java.util.Optional;

public class NexoProvider extends CustomProvider {

    @Override
    public void place(Location location, String id) {
        NamespacedKey key = NamespacedKey.fromString(id);
        NexoFurniture.place(key == null ? id : key.getKey(), location, Rotation.NONE,  BlockFace.UP);
    }

    @Override
    public boolean remove(Location location) {
        Optional<Entity> entity = location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).stream()
                .filter(NexoFurniture::isFurniture).min((o1, o2) -> {
                    double d1 = o1.getLocation().distance(location);
                    double d2 = o2.getLocation().distance(location);
                    return Double.compare(d1, d2);
                });
        return entity.filter(NexoFurniture::remove).isPresent();
    }

}
