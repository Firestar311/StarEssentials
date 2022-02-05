package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd;
import com.starmediadev.plugins.staressentials.listeners.SpawnListener;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starmcutils.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessage;

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
    }
    
    @Override
    public void createCommandExecutors() {
        this.commands.put("spawn", new PlayerActionCmd(plugin, "staressentials.command.spawn", (target, self, sender) -> {
            target.teleport(spawn);
            sendActionMessage(plugin, target, self, sender, "spawn");
        }));
        
        this.commands.put("setspawn", (sender, cmd, label, args) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(MCUtils.color("&cOnly players can use this command."));
                return true;
            }
            
            if (!player.hasPermission("staressentials.command.spawn.set")) {
                player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            }
    
            spawn = player.getLocation();
            player.sendMessage(MCUtils.color(plugin.getConfig().getString("spawn.set")));
            return true;
        });
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
