package com.github.ipecter.rtustudio.vcrops.configuration;

import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;
import com.github.ipecter.rtustudio.vcrops.crop.Crop;
import com.google.common.io.Files;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import kr.rtuserver.framework.bukkit.api.platform.FileResource;
import kr.rtuserver.framework.yaml.configuration.ConfigurationSection;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CropConfig {

    private final VanillaticCrops plugin;

    private final Map<String, Crop> crops = new HashMap<>();

    public Crop getFromStage(String stage) {
        for (Crop crop : crops.values()) {
            if (crop.stages().contains(stage)) return crop;
        }
        return null;
    }

    public Crop getFromSeed(String seed) {
        for (Crop crop : crops.values()) {
            if (crop.seed().equals(seed)) return crop;
        }
        return null;
    }

    public CropConfig(VanillaticCrops plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (!new File(plugin.getDataFolder() + "/Configs/Crops/").exists())
            FileResource.createFileCopy(plugin, "Configs/Crops", "Example.yml");
        File[] files = FileResource.createFolder(plugin.getDataFolder() + "/Configs/Crops").listFiles();
        if (files == null) return;
        crops.clear();
        for (File file : files) {
            String name = file.getName();
            if (!name.endsWith(".yml")) continue;
            Config config = new Config(name);
        }
    }


    class Config extends RSConfiguration<VanillaticCrops> {

        private final String name;

        public Config(String name) {
            super(plugin, "Configs/Crops", name, null);
            this.name = Files.getNameWithoutExtension(name);
            setup(this);
        }

        private void init() {
            for (String key : getConfig().getKeys(false)) {
                ConfigurationSection section = getConfigurationSection(key);
                if (section == null) continue;
                String plant = section.getString("plant", "");
                String seed = section.getString("seed", "");
                List<String> stages = section.getStringList("stages");
                crops.put(name, new Crop(plant, seed, stages));
            }
        }
    }
}
