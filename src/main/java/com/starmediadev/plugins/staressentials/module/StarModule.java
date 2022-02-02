package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class StarModule {
    protected StarEssentials starEssentials;
    protected String name;
    protected boolean enabled;
    
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
}
