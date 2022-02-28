package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.SpawnCmds;
import com.starmediadev.plugins.staressentials.listeners.SpawnListener;
import com.starmediadev.plugins.starmcutils.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class SpawnModule extends StarEssentialsModule {
    
    private boolean teleportOnJoin = false;
    private Location spawn;
    
    public SpawnModule(StarEssentials plugin) {
        super(plugin, "spawn");
    }
    
    @Override
    protected void loadValuesFromConfig() {
        this.teleportOnJoin = config.getConfiguration().getBoolean("settings.teleportonjoin");
        if (config.getConfiguration().contains("settings.location")) {
            spawn = config.getConfiguration().getLocation("settings.location");
        }
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("settings.teleportonjoin", false);
        this.defaultConfigValues.put("settings.location", Bukkit.getWorld(ServerProperties.getLevelName()).getSpawnLocation());
        this.defaultConfigValues.put("messages.self", "&eYou have teleported to spawn.");
        this.defaultConfigValues.put("messages.other", "&eYou have sent &b{target} &eto spawn");
        this.defaultConfigValues.put("messages.target", "&eYou have been sent to spawn by &b{player}");
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(SpawnModule.class, this);
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new SpawnCmds());
    }
    
    @Override
    protected void saveConfigSettings() {
        YamlConfiguration config = this.config.getConfiguration();
        config.set("settings.teleportonjoin", teleportOnJoin);
        config.set("settings.location", spawn);
    }
    
    @Override
    public void createEventListeners() {
        this.listeners.add(new SpawnListener(plugin));
    }
    
    public Location getSpawn() {
        return spawn;
    }
    
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
    
    public boolean teleportOnJoin() {
        return this.teleportOnJoin;
    }
}
