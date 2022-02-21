package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd;
import com.starmediadev.plugins.staressentials.listeners.GodListener;
import org.bukkit.entity.Player;

import java.util.*;

import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessage;
import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessageValue;

public class PlayerStatsModule extends StarEssentialsModule {
    
    private Set<UUID> playersInGodMode = new HashSet<>();
    
    public PlayerStatsModule(StarEssentials plugin) {
        super(plugin, "playerstats");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        defaultConfigValues.put("settings.feed.self", "&aYou have fed yourself.");
        defaultConfigValues.put("settings.feed.other", "&aYou have fed &b{target}");
        defaultConfigValues.put("settings.feed.target", "&aYou have been fed by &b{player}");
    
        defaultConfigValues.put("settings.fly.self", "&aYou have toggled flight to &b{value}");
        defaultConfigValues.put("settings.fly.other", "&aYou have toggled flight for &b{target} &ato &b{value}");
        defaultConfigValues.put("settings.fly.target", "&aYour flight has been toggled to &b{value} &aby &b{player}");
    
        defaultConfigValues.put("settings.heal.self", "&aYou have healed yourself.");
        defaultConfigValues.put("settings.heal.other", "&aYou have healed &b{target}");
        defaultConfigValues.put("settings.heal.target", "&aYou have been healed by &b{player}");
    
        defaultConfigValues.put("settings.god.self", "&aYou have toggled god mode to &b{value}");
        defaultConfigValues.put("settings.god.other", "&aYou have toggled god mode for &b{target} &ato &b{value}");
        defaultConfigValues.put("settings.god.target", "&aYour god mode has been toggled to &b{value} &aby &b{player}");
    }
    
    @Override
    protected void createCommandExecutors() {
        commands.put("feed", new PlayerActionCmd(plugin, "staressentials.command.feed", (target, self, sender) -> {
            target.setFoodLevel(20);
            target.setSaturation(10);
            sendActionMessage(this, target, self, sender, "settings.feed");
        }));
    
        commands.put("fly", new PlayerActionCmd(plugin, "staressentials.command.fly", (target, self, sender) -> {
            target.setAllowFlight(!target.getAllowFlight());
            sendActionMessageValue(this, target, self, sender, "settings.fly", target.getAllowFlight());
        }));
    
        commands.put("heal", new PlayerActionCmd(plugin, "staressentials.command.heal", (target, self, sender) -> {
            target.setHealth(20);
            sendActionMessage(this, target, self, sender, "settings.heal");
        }));
    
        commands.put("god", new PlayerActionCmd(plugin, "staressentials.command.god", (target, self, sender) -> {
            if (playersInGodMode.contains(target.getUniqueId())) {
                playersInGodMode.remove(target.getUniqueId());
            } else {
                playersInGodMode.add(target.getUniqueId());
            }
        
            sendActionMessageValue(this, target, self, sender, "settings.god", playersInGodMode.contains(target.getUniqueId()));
        }));
    }
    
    @Override
    protected void createEventListeners() {
        this.listeners.add(new GodListener(plugin));
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
    
    public boolean isPlayerInGodMode(Player player) {
        return this.playersInGodMode.contains(player.getUniqueId());
    }
}
