package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.Target;

public abstract class InventoryOpenCmd implements CommandExecutor {
    private StarEssentials plugin;
    private String permission;
    
    public InventoryOpenCmd(StarEssentials plugin, String permission) {
        this.plugin = plugin;
        this.permission = permission;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        Player target;
        if (args.length == 0) {
            target = player;
        } else {
            if (!sender.hasPermission(permission + ".other")) {
                sender.sendMessage(MCUtils.color("&cYou do not have permission to do open that inventory."));
                return true;
            }
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(MCUtils.color("&cYou provided an invalid target."));
                return true;
            }
        }
        
        player.openInventory(getInventory(target));
        
        return true;
    }
    
    public abstract Inventory getInventory(Player player);
}
