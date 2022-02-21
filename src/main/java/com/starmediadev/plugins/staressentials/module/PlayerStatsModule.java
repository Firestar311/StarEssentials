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
        
    }
    
    @Override
    protected void createCommandExecutors() {
        commands.put("feed", new PlayerActionCmd(plugin, "staressentials.command.feed", (target, self, sender) -> {
            target.setFoodLevel(20);
            target.setSaturation(10);
            sendActionMessage(plugin, target, self, sender, "feed");
        }));
    
        commands.put("fly", new PlayerActionCmd(plugin, "staressentials.command.fly", (target, self, sender) -> {
            target.setAllowFlight(!target.getAllowFlight());
            sendActionMessageValue(plugin, target, self, sender, "fly", target.getAllowFlight());
        }));
    
        commands.put("heal", new PlayerActionCmd(plugin, "staressentials.command.heal", (target, self, sender) -> {
            target.setHealth(20);
            sendActionMessage(plugin, target, self, sender, "heal");
        }));
    
        commands.put("god", new PlayerActionCmd(plugin, "staressentials.command.god", (target, self, sender) -> {
            if (playersInGodMode.contains(target.getUniqueId())) {
                playersInGodMode.remove(target.getUniqueId());
            } else {
                playersInGodMode.add(target.getUniqueId());
            }
        
            sendActionMessageValue(plugin, target, self, sender, "god", playersInGodMode.contains(target.getUniqueId()));
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
