package com.github.ipecter.rtustudio.vcrops.commands;

import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import com.github.ipecter.rtustudio.vcrops.VanillaticCrops;

import java.util.List;

public class MainCommand extends RSCommand<VanillaticCrops> {

    public MainCommand(VanillaticCrops plugin) {
        super(plugin, "vcrop");
    }

    @Override
    public void reload(RSCommandData data) {
        getPlugin().getCropConfig().reload();
    }

}
