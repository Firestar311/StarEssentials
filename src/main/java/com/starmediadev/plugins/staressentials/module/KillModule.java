package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.KillCmd;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class KillModule extends StarEssentialsModule {
    public KillModule(StarEssentials plugin) {
        super(plugin, "kill");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("messages.self", "&eYou have killed yourself.");
        this.defaultConfigValues.put("messages.other", "&eYou have killed &b{target}");
        this.defaultConfigValues.put("messages.target", "&eYou have been killed by &b{player}");
        //Add a permission to prevent death from another player using /kill, but add an override section in config to bypass this fact
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new KillCmd());
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(KillModule.class, this);
    }
    
    @Override
    protected void loadValuesFromConfig() {
        
    }
    
    @Override
    protected void saveConfigSettings() {
        
    }
}
