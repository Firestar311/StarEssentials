package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public record BroadcastCmd(StarEssentials plugin) implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("staressentials.command.broadcast")) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        String senderName;
        if (sender instanceof ConsoleCommandSender) {
            senderName = "&4Console";
        } else if (sender instanceof Player player) {
            senderName = player.getDisplayName();
        } else {
            sender.sendMessage(MCUtils.color("&cOnly the Console and Players can use that command."));
            return true;
        }
        
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
        } else {
            sender.sendMessage(MCUtils.color("&cYou must provide a message to send."));
            return true;
        }
        
        String message = sb.toString().trim();
        
        String format = plugin.getBroadcastModule().getConfig().getConfiguration().getString("settings.format");
        if (format == null) {
            sender.sendMessage(MCUtils.color("&cThere is a problem with the broadcast format in the config. Contact your server admin."));
            return true;
        }
        
        format = format.replace("{displayname}", senderName).replace("{message}", message);
        Bukkit.broadcastMessage(MCUtils.color(format));
        return true;
    }
}
