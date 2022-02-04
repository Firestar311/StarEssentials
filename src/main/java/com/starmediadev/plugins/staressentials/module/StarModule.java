package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class StarModule {
    //TODO configs specific to module, requires a change to the PlayerActionCmd send message methods
    protected StarEssentials starEssentials;
    protected String name;
    protected boolean enabled = true;
    
    protected Map<String, CommandExecutor> commands = new HashMap<>();
    protected Set<Listener> listeners = new HashSet<>();
    
    public StarModule(StarEssentials starEssentials, String name) {
        this.starEssentials = starEssentials;
        this.name = name;
    }
    
    public abstract void createCommandExecutors();
    public abstract void createEventListeners();
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public final void registerCommands() {
        this.commands.forEach((name, executor) -> starEssentials.getCommand(name).setExecutor(executor));
    }
    
    public final void registerListeners() {
        this.listeners.forEach(listener -> starEssentials.getServer().getPluginManager().registerEvents(listener, starEssentials));
    }
}
