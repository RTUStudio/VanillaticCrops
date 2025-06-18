package com.github.ipecter.rtustudio.vcrops;

import com.github.ipecter.rtustudio.vcrops.commands.MainCommand;
import com.github.ipecter.rtustudio.vcrops.configuration.CropConfig;
import com.github.ipecter.rtustudio.vcrops.integration.VanillaListener;
import com.github.ipecter.rtustudio.vcrops.integration.abstracts.CustomProvider;
import com.github.ipecter.rtustudio.vcrops.integration.itemsadder.ItemsAdderListener;
import com.github.ipecter.rtustudio.vcrops.integration.itemsadder.ItemsAdderProvider;
import com.github.ipecter.rtustudio.vcrops.integration.nexo.NexoListener;
import com.github.ipecter.rtustudio.vcrops.integration.nexo.NexoProvider;
import com.github.ipecter.rtustudio.vcrops.integration.oraxen.OraxenListener;
import com.github.ipecter.rtustudio.vcrops.integration.oraxen.OraxenProvider;
import com.github.ipecter.rtustudio.vcrops.listeners.ChunkLoad;
import com.github.ipecter.rtustudio.vcrops.listeners.WorldLoad;
import com.github.ipecter.rtustudio.vcrops.manager.CropManager;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import lombok.Getter;

import java.util.Random;

@Getter
public class VanillaticCrops extends RSPlugin {

    @Getter
    private static VanillaticCrops instance;

    private final Random random = new Random();

    private CropConfig cropConfig;

    private CropManager cropManager;

    private CustomProvider customProvider;

    @Override
    public void enable() {
        instance = this;

        this.cropConfig = new CropConfig(this);

        this.cropManager = new CropManager(this);

        registerCustom();
        registerEvent(new VanillaListener(this));

        registerEvent(new WorldLoad(this));
        registerEvent(new ChunkLoad(this));
        registerCommand(new MainCommand(this), true);
    }

    private void registerCustom() {
        if (getFramework().isEnabledDependency("Nexo")) {
            this.customProvider = new NexoProvider();
            registerEvent(new NexoListener(this));
            return;
        }
        if (getFramework().isEnabledDependency("ItemsAdder")) {
            this.customProvider = new ItemsAdderProvider();
            registerEvent(new ItemsAdderListener(this));
            return;
        }
        if (getFramework().isEnabledDependency("Oraxen")) {
            this.customProvider = new OraxenProvider();
            registerEvent(new OraxenListener(this));
            return;
        }
    }
}
