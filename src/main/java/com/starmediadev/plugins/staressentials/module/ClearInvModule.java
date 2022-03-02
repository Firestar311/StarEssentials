package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.ClearInventoryCmd;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class ClearInvModule extends StarEssentialsModule {
    public ClearInvModule(StarEssentials plugin) {
        super(plugin, "clearinv");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("messages.self", "&eYou have cleared your inventory.");
        this.defaultConfigValues.put("messages.other", "&eYou have cleared the inventory of &b{target}");
        this.defaultConfigValues.put("messages.target", "&eYour inventory has been cleared by &b{player}");
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new ClearInventoryCmd());
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(ClearInvModule.class, this);
    }
}
