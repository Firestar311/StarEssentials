package com.starmediadev.plugins.staressentials.listeners;

import com.starmediadev.plugins.staressentials.module.PlayerStatsModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public record GodListener(PlayerStatsModule module) implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            
            e.setCancelled(module.isPlayerInGodMode(player));
        }
    }
    
    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player player) {
            e.setCancelled(module.isPlayerInGodMode(player));
        }
    }
}
