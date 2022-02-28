package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.BroadcastCmd;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class BroadcastModule extends StarEssentialsModule {
    public BroadcastModule(StarEssentials plugin) {
        super(plugin, "broadcast");
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(BroadcastModule.class, this);
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new BroadcastCmd());
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("settings.format", "&e[BROADCAST] &6{displayname}&8: &b{message}");
    }
}
