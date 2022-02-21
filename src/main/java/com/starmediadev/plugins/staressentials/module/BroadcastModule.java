package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.cmds.BroadcastCmd;

public class BroadcastModule extends StarEssentialsModule {
    public BroadcastModule(StarEssentials plugin) {
        super(plugin, "broadcast");
    }
    
    @Override
    protected void createCommandExecutors() {
        this.commands.put("broadcast", new BroadcastCmd(plugin));
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("settings.format", "&e[BROADCAST] &6{displayname}&8: &b{message}");
    }
}
