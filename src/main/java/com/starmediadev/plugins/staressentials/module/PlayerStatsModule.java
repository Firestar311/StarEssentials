package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.lampcmds.PlayerStatsCmds;
import com.starmediadev.plugins.staressentials.listeners.GodListener;
import org.bukkit.entity.Player;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.*;

public class PlayerStatsModule extends StarEssentialsModule {
    
    private Set<UUID> playersInGodMode = new HashSet<>();
    
    public PlayerStatsModule(StarEssentials plugin) {
        super(plugin, "playerstats");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        defaultConfigValues.put("settings.feed.self", "&eYou have fed yourself.");
        defaultConfigValues.put("settings.feed.other", "&eYou have fed &b{target}");
        defaultConfigValues.put("settings.feed.target", "&eYou have been fed by &b{player}");
    
        defaultConfigValues.put("settings.fly.self", "&eYou have toggled flight to &b{value}");
        defaultConfigValues.put("settings.fly.other", "&eYou have toggled flight for &b{target} &eto &b{value}");
        defaultConfigValues.put("settings.fly.target", "&eYour flight has been toggled to &b{value} &eby &b{player}");
    
        defaultConfigValues.put("settings.heal.self", "&eYou have healed yourself.");
        defaultConfigValues.put("settings.heal.other", "&eYou have healed &b{target}");
        defaultConfigValues.put("settings.heal.target", "&eYou have been healed by &b{player}");
    
        defaultConfigValues.put("settings.god.self", "&eYou have toggled god mode to &b{value}");
        defaultConfigValues.put("settings.god.other", "&eYou have toggled god mode for &b{target} &eto &b{value}");
        defaultConfigValues.put("settings.god.target", "&eYour god mode has been toggled to &b{value} &eby &b{player}");
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(PlayerStatsModule.class, this);
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new PlayerStatsCmds());
    }
    
    @Override
    protected void createEventListeners() {
        this.listeners.add(new GodListener(this));
    }
    
    @Override
    protected void loadValuesFromConfig() {
        List<String> rawPlayersInGodMode = config.getConfiguration().getStringList("godmodeplayers");
        for (String entry : rawPlayersInGodMode) {
            this.playersInGodMode.add(UUID.fromString(entry));
        }
    }
    
    @Override
    protected void saveConfigSettings() {
        List<String> rawPlayersInGodMode = new ArrayList<>();
        this.playersInGodMode.forEach(uuid -> rawPlayersInGodMode.add(uuid.toString()));
        config.getConfiguration().set("players", rawPlayersInGodMode);
    }
    
    public boolean toggleGod(Player player) {
        if (isPlayerInGodMode(player)) {
            removePlayerFromGod(player);
            return false;
        } else {
            addPlayerToGod(player);
            return true;
        }
    }
    
    public boolean isPlayerInGodMode(Player player) {
        return this.playersInGodMode.contains(player.getUniqueId());
    }
    
    public void removePlayerFromGod(Player player) {
        this.playersInGodMode.remove(player.getUniqueId());
    }
    
    public void addPlayerToGod(Player player) {
        this.playersInGodMode.add(player.getUniqueId());
    }
}
