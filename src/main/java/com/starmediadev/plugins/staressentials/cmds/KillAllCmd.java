package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starmcutils.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;

public record KillAllCmd(StarEssentials plugin) implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String basePerm = "staressentials.command.killall";
        if (!sender.hasPermission("staressentials.command.killall")) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        String entityTarget;
    
        boolean remove = false, killTamed = false;
        
        if (args.length == 0) {
            if (!sender.hasPermission(basePerm + ".all")) {
                sender.sendMessage("&cYou do not have permission to kill all entities");
                return true;
            }
            
            entityTarget = "all";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                if (arg.startsWith("-r")) {
                    remove = true;
                } else if (arg.startsWith("-t")) {
                    if (sender.hasPermission(basePerm + ".flag.killtamed")) {
                        killTamed = true;
                    } else {
                        sender.sendMessage(MCUtils.color("&7&oIgnoring -t flag as you do not have permission to use it."));
                    }
                } else {
                    sb.append(arg).append(" ");
                }
            }
            if (sb.isEmpty()) {
                entityTarget = "all";
            } else {
                entityTarget = sb.toString().trim();
            }
        }
        
        World targetWorld;
        if (sender instanceof ConsoleCommandSender) {
            targetWorld = Bukkit.getWorld(ServerProperties.getLevelName());
        } else if (sender instanceof Player player) {
            targetWorld = player.getWorld();
        } else {
            sender.sendMessage(MCUtils.color("&cOnly the Console or a Player can use that command."));
            return true;
        }
        
        Class<? extends Entity> classType;
        try {
            EntityType entityType = EntityType.valueOf(entityTarget.toUpperCase().replace(" ", "_"));
            classType = entityType.getEntityClass();
        } catch (IllegalArgumentException e) {
            classType = switch (entityTarget.toLowerCase()) {
                case "animals", "animal" -> Animals.class;
                case "boss" -> Boss.class;
                case "creature", "creatures" -> Creature.class;
                case "hanging" -> Hanging.class;
                case "item", "items" -> Item.class;
                case "living" -> LivingEntity.class;
                case "mob", "mobs" -> Mob.class;
                case "monster", "monsters" -> Monster.class;
                case "npc", "npcs" -> NPC.class;
                case "vehicle", "vehicles", "minecart", "minecarts" -> Vehicle.class;
                default -> Entity.class;
            };
        }
        
        if (classType.equals(Player.class)) {
            sender.sendMessage(MCUtils.color("&cYou cannot kill players with this command."));
            return true;
        }
        
        int totalKilled = 0;
    
        for (Entity entity : targetWorld.getEntities()) {
            if (classType.isAssignableFrom(entity.getClass()) && !(entity instanceof Player)) {
                if (entity instanceof Tameable tameable) {
                    if (tameable.isTamed()) {
                        if (!killTamed) {
                            continue;
                        }
                    }
                }
                if (remove || !(entity instanceof Damageable damageable)) {
                    entity.remove();
                } else {
                    damageable.setHealth(0);
                }
                totalKilled++;
            }
        }
        
        String output = plugin.getConfig().getString("killall.format");
        output = output.replace("{total}", totalKilled + "").replace("{type}", entityTarget.replace("_", " "));
        sender.sendMessage(MCUtils.color(output));
        return true;
    }
}
