package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.EntityNames;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;

import static org.bukkit.entity.EntityType.*;

public record SpawnmobCmd(StarEssentials plugin) implements CommandExecutor {
    
    private static final Set<EntityType> BLACKLISTED_TYPES = EnumSet.of(DROPPED_ITEM, AREA_EFFECT_CLOUD, LEASH_HITCH, PAINTING, ARROW, SPLASH_POTION, THROWN_EXP_BOTTLE, ITEM_FRAME,
            WITHER_SKULL, SPECTRAL_ARROW, SHULKER_BULLET, ARMOR_STAND, EVOKER_FANGS, MARKER, UNKNOWN);
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players can use that command."));
            return true;
        }
        
        boolean spawnAtLocation = false;
        
        if (!(args.length > 0)) {
            player.sendMessage(MCUtils.color("&cYou must provide a mob type"));
            return true;
        }
        
        int flagLocation = -1;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-l")) {
                spawnAtLocation = true;
                flagLocation = i;
                break;
            }
        }
        
        if (flagLocation > -1) {
            String[] newArgs = new String[args.length - 1];
            for (int i = 0; i < args.length; i++) {
                if (i < flagLocation) {
                    newArgs[i] = args[i];
                } else {
                    if (i + 1 < args.length) {
                        newArgs[i] = args[i + 1];
                    }
                }
            }
            args = newArgs;
        }
        
        String rawMobType = args[0].toUpperCase();
        int amount = 1;
        
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount == 1) {
                    player.sendMessage(MCUtils.color("&7Ignoring spawn amount of 1 as that is the command default."));
                }
                
                if (amount <= 0) {
                    player.sendMessage(MCUtils.color("&7Ignoring the spawn amount of " + amount + ", defaulting to 1."));
                    amount = 1;
                }
                
                int limit = plugin.getConfig().getInt("spawnmob.settings.limit");
                if (amount > limit) {
                    player.sendMessage(MCUtils.color("&cThe amount " + amount + " is greater than the " + limit + " limit in the config, changing the amount to the limit."));
                    amount = limit;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(MCUtils.color("&cYou did not provide a valid whole number."));
                return true;
            }
        }
        
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(rawMobType);
        } catch (IllegalArgumentException e) {
            player.sendMessage(MCUtils.color("&cYou provided an invalid entity type"));
            System.out.println(rawMobType);
            return true;
        }
        
        if (BLACKLISTED_TYPES.contains(entityType)) {
            player.sendMessage(MCUtils.color("&cSpawning that entity type is not supported by this command."));
            return true;
        }
        
        Location spawnLocation;
        if (spawnAtLocation) {
            spawnLocation = player.getLocation();
        } else {
            spawnLocation = player.getTargetBlock(null, 100).getLocation();
            if (spawnLocation == null) {
                player.sendMessage(MCUtils.color("&cYou must be looking at a block within 100 blocks"));
                return true;
            }
            spawnLocation.setY(spawnLocation.getY() + 1);
        }
        
        for (int i = 0; i < amount; i++) {
            player.getWorld().spawnEntity(spawnLocation, entityType);
        }
        
        if (amount == 1) {
            player.sendMessage(MCUtils.color(plugin.getConfig().getString("spawnmob.messages.single").replace("{entity}", EntityNames.getDefaultName(entityType))));
        } else {
            player.sendMessage(MCUtils.color(plugin.getConfig().getString("spawnmob.messages.multiple").replace("{entity}", EntityNames.getDefaultName(entityType)).replace("{amount}", amount + "")));
        }
        
        return true;
    }
}
