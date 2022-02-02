package com.starmediadev.plugins.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class PlayerValueCmd implements CommandExecutor {
    protected StarEssentials plugin;
    protected String permission;
    protected CmdAction cmdAction;
    
    public PlayerValueCmd(StarEssentials plugin, String permission, CmdAction cmdAction) {
        this.plugin = plugin;
        this.permission = permission;
        this.cmdAction = cmdAction;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
    
        Player target = null;
    
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage(MCUtils.color("&cYou must provide a target for the command."));
                return true;
            }
        } else if (sender instanceof Player player) {
            if (args.length == 0) {
                target = player;
            }
        } else {
            sender.sendMessage(MCUtils.color("&cOnly the Console and Players can use that command."));
            return true;
        }
    
        //Logically this should be fine as I have covered all the other cases up to this point. I could use a Method to get the target, but why bother?
        if (target == null) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MCUtils.color("&cYou provided an invalid target."));
                return true;
            }
        }
        
        cmdAction.performAction(target, target.getName().equals(sender.getName()), sender);
        return true;
    }
    
    public interface CmdAction {
        void performAction(Player target, boolean self, CommandSender sender);
    }
}
