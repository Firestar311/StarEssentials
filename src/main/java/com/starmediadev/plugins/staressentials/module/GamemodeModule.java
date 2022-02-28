package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.GamemodeCmds;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class GamemodeModule extends StarEssentialsModule {
    public GamemodeModule(StarEssentials plugin) {
        super(plugin, "gamemode");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("messages.self", "&eYou have set your gamemode to &b{value}");
        this.defaultConfigValues.put("messages.other", "&eYou have set &b{target}'s &egamemode to &b{value}");
        this.defaultConfigValues.put("messages.target", "&eYour gamemode has been changed to &b{value} &eby &b{player}");
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new GamemodeCmds());
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(GamemodeModule.class, this);
    }
    
    @Override
    protected void loadValuesFromConfig() {
        
    }
    
    @Override
    protected void saveConfigSettings() {
        
    }
}
