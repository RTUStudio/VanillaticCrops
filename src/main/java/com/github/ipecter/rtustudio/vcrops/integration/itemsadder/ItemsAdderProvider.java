package com.github.ipecter.rtustudio.vcrops.integration.itemsadder;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomListener;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.nexomc.nexo.api.NexoFurniture;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceSuccessEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;

import java.util.Optional;

public class ItemsAdderProvider extends CustomProvider {

    @Override
    public Location place(Location location, String id) {
        NamespacedKey key = NamespacedKey.fromString(id);
        CustomFurniture cf = CustomFurniture.spawnPreciseNonSolid(key == null ? id : key.getKey(), location);
        if (cf == null) return null;
        return cf.getEntity().getLocation();
    }

    @Override
    public boolean remove(Location location) {
        Optional<Entity> entity = location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5).stream()
                .min((o1, o2) -> {
                    double d1 = o1.getLocation().distance(location);
                    double d2 = o2.getLocation().distance(location);
                    return Double.compare(d1, d2);
                });
        if (entity.isPresent()) {
            CustomFurniture.remove(entity.get(), false);
            return true;
        } else return false;
    }

}
