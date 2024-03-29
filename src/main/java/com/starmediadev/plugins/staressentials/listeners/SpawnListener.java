package com.starmediadev.plugins.staressentials.listeners;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.module.SpawnModule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public record SpawnListener(StarEssentials plugin, SpawnModule module) implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (module.teleportOnJoin() || !e.getPlayer().hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> e.getPlayer().teleport(module.getSpawn()), 1L);
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (!e.isBedSpawn() || !e.isAnchorSpawn()) {
            e.setRespawnLocation(module.getSpawn());
        }
    }
}
