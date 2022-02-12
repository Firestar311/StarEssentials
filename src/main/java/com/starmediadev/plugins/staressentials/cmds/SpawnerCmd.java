package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.EntityNames;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public record SpawnerCmd(StarEssentials plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        if (!(player.hasPermission("staressentials.command.spawner"))) {
            player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
    
        Block block = player.getTargetBlock(null, 50);
        if (block == null) {
            player.sendMessage(MCUtils.color("&cYou are not looking at a block."));
            return true;
        }
        
        if (block.getType() != Material.SPAWNER) {
            player.sendMessage(MCUtils.color("&cYou are not looking at a spawner."));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(MCUtils.color("&cYou must provide a mob type."));
            return true;
        }
    
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            player.sendMessage(MCUtils.color("&cYou provided an invalid mob type."));
            return true;
        }
        
        if (!entityType.isSpawnable()) {
            player.sendMessage(MCUtils.color("&cThat entity type cannot be spawned by a spawner."));
            return true;
        }
    
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        
        if (spawner.getSpawnedType() == entityType) {
            player.sendMessage(MCUtils.color("&cThe spawner is already spawning " + EntityNames.getName(entityType)));
            return true;
        }
        
        spawner.setSpawnedType(entityType);
        spawner.update();
        
        player.sendMessage(MCUtils.color(plugin.getConfig().getString("spawner.set").replace("{entity}", EntityNames.getName(entityType))));
        return true;
    }
}
