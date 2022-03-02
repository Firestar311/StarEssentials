package com.starmediadev.plugins.staressentials.util;

import com.starmediadev.plugins.staressentials.module.StarEssentialsModule;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.exception.CommandErrorException;

public final class SEUtils {
    public static Pair<Player, Boolean> getPlayerTarget(CommandSender sender, Player player, String permission) {
        if (sender instanceof ConsoleCommandSender && player == null) {
            throw new CommandErrorException("You must provide a player as the console");
        }
    
        Player target;
        boolean self = false;
        if (player == null) {
            target = (Player) sender;
            self = true;
        } else {
            target = player;
            if (!sender.hasPermission(permission + ".other")) {
                throw new CommandErrorException("You do not have permission to do that to other players.");
            }
        }
        return new Pair<>(target, self);
    }
    
    public static void sendActionMessageValue(JavaPlugin plugin, Player target, boolean self, CommandSender sender, String msgBase, Object value) {
        if (self) {
            sender.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".self").replace("{value}", value + "")));
        } else {
            sender.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".other").replace("{target}", target.getName()).replace("{value}", value + "")));
            target.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".target").replace("{player}", sender.getName()).replace("{value}", value + "")));
        }
    }
    
    public static void sendActionMessage(JavaPlugin plugin, Player target, boolean self, CommandSender sender, String msgBase) {
        if (self) {
            target.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".self")));
        } else {
            sender.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".other").replace("{target}", target.getName())));
            target.sendMessage(MCUtils.color(plugin.getConfig().getString(msgBase + ".target").replace("{player}", sender.getName())));
        }
    }
    
    public static void sendActionMessageValue(StarEssentialsModule module, Player target, boolean self, CommandSender sender, String msgBase, Object value) {
        if (self) {
            sender.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".self").replace("{value}", value + "")));
        } else {
            sender.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".other").replace("{target}", target.getName()).replace("{value}", value + "")));
            target.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".target").replace("{player}", sender.getName()).replace("{value}", value + "")));
        }
    }
    
    public static void sendActionMessage(StarEssentialsModule module, Player target, boolean self, CommandSender sender, String msgBase) {
        if (self) {
            target.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".self")));
        } else {
            sender.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".other").replace("{target}", target.getName())));
            target.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString(msgBase + ".target").replace("{player}", sender.getName())));
        }
    }
}
