package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.SignEditCmds;
import com.starmediadev.plugins.staressentials.listeners.SignListener;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class SignEditModule extends StarEssentialsModule {
    public SignEditModule(StarEssentials plugin) {
        super(plugin, "signedit");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("messages.setline", "&eYou set line &b{line} &eto {text}");
        this.defaultConfigValues.put("messages.clearline", "&eYou cleared the text on line &b{line}");
        this.defaultConfigValues.put("messages.setglowing", "&eYou set glowing on that sign to &b{value}");
        this.defaultConfigValues.put("messages.setcolor", "&eYou set the color on that sign to &b{color}");
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new SignEditCmds());
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(SignEditModule.class, this);
    }
    
    @Override
    protected void createEventListeners() {
        this.listeners.add(new SignListener());
    }
}
