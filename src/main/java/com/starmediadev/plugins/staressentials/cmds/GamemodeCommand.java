package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.utils.helper.StringHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public record GamemodeCommand(StarEssentials plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("staressentials.command.gamemode")) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(MCUtils.color("&cUsage: /" + label + " <gamemode> [player]"));
            return true;
        }
        
        GameMode gameMode = switch (args[0].toLowerCase()) {
            case "creative", "c" -> GameMode.CREATIVE;
            case "survival", "s" -> GameMode.SURVIVAL;
            case "adventure", "a" -> GameMode.ADVENTURE;
            case "spectator", "sp" -> GameMode.SPECTATOR;
            default -> null;
        };
        
        if (gameMode == null) {
            sender.sendMessage(MCUtils.color("&cYou provided an invalid gamemode."));
            return true;
        }
        
        if (!sender.hasPermission("staressentials.command.gamemode." + gameMode.name().toLowerCase())) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that gamemode."));
            return true;
        }
        
        Player target = null;
        boolean self = false;
        if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(MCUtils.color("&cYou must provide a target."));
                return true;
            }
            
            target = Bukkit.getPlayer(args[1]);
        } else if (sender instanceof Player player) {
            if (args.length == 1) {
                target = player;
                self = true;
            } else if (args.length > 1) {
                if (!sender.hasPermission("staressentials.command.gamemode." + gameMode.name().toLowerCase() + ".others")) {
                    sender.sendMessage(MCUtils.color("&cYou do not have permission to set other player's gamemode to " + gameMode.name().toLowerCase()));
                    return true;
                }
                target = Bukkit.getPlayer(args[1]);
            }
        }
        
        if (target == null) {
            sender.sendMessage(MCUtils.color("&cYou provided an invalid target."));
            return true;
        }
        
        target.setGameMode(gameMode);
        PlayerActionCmd.sendActionMessageValue(plugin, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(gameMode.name()));
        return true;
    }
}
