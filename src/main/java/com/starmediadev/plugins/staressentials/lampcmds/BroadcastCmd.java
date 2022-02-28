package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.BroadcastModule;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

public class BroadcastCmd {
    
    @Dependency
    private BroadcastModule broadcastModule;
    
    @Command({"broadcast", "bc"})
    @CommandPermission("staressentials.command.broadcast")
    @Description("Broadcasts a message to the whole server.")
    public void broadcast(CommandSender sender, @Named("message") String message) {
        String senderName;
        if (sender instanceof ConsoleCommandSender) {
            senderName = "&4Console";
        } else if (sender instanceof Player player) {
            senderName = player.getDisplayName();
        } else {
            throw new CommandErrorException("Only console and players can use that command.");
        }
    
        String format = broadcastModule.getConfig().getConfiguration().getString("settings.format");
        if (format == null) {
            throw new CommandErrorException("&cThere is a problem with the broadcast format in the config. Contact your server admin.");
        }
    
        format = format.replace("{displayname}", senderName).replace("{message}", message);
        Bukkit.broadcastMessage(MCUtils.color(format));
    }
}
