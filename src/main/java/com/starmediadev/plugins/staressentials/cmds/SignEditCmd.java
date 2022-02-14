package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record SignEditCmd(StarEssentials plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        if (!player.hasPermission("staressentials.command.signedit")) {
            player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
    
        Block block = player.getTargetBlock(null, 50);
        if (block == null) {
            player.sendMessage(MCUtils.color("&cYou are not looking at a block."));
            return true;
        }
        
        if (!(block.getState() instanceof Sign sign)) {
            player.sendMessage(MCUtils.color("&cYou are not looking at a sign."));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(MCUtils.color("You must provide a sub command."));
            return true;
        }
        
        if (args[0].equalsIgnoreCase("setline") || args[0].equalsIgnoreCase("sl")) {
            if (!player.hasPermission("staressentials.command.signedit.setline")) {
                player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            }
            
            if (!(args.length > 1)) {
                player.sendMessage(MCUtils.color("&cYou must provide a line number."));
                return true;
            }
            
            int line;
            try {
                line = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(MCUtils.color("&cThe line number you provided was invalid."));
                return true;
            }
            
            if (!(line > 0 && line < 5)) {
                player.sendMessage(MCUtils.color("&cYou provided an invalid line number. Accepted values are 1-4"));
                return true;
            }
            
            line -= 1;
            
            String text = "";
            if (args.length > 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                text = sb.toString().trim();
            }
            
            if (text.length() > 16) {
                player.sendMessage(MCUtils.color("&cThe text you provided is greater than 16 characters."));
                return true;
            }
            
            sign.setLine(line, MCUtils.color(text));
            sign.update();
            if (text.isEmpty() || text.isBlank() || text.equals("")) {
                player.sendMessage(MCUtils.color(plugin.getConfig().getString("signedit.clearline").replace("{line}", (line + 1) + "")));   
            } else {
                player.sendMessage(MCUtils.color(plugin.getConfig().getString("signedit.setline").replace("{line}", (line + 1) + "").replace("{text}", text)));
            }
        } else if (args[0].equalsIgnoreCase("setglowing") || args[0].equalsIgnoreCase("sg")) {
            if (!player.hasPermission("staressentials.command.signedit.setglowing")) {
                player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            }
            
            if (!(args.length > 1)) {
                player.sendMessage(MCUtils.color("&cYou must provide a true or false value."));
                return true;
            }
            
            boolean value;
            if (args[1].equalsIgnoreCase("true")) {
                value = true;
            } else if (args[1].equalsIgnoreCase("false")) {
                value = false;
            } else {
                player.sendMessage(MCUtils.color("&cInvalid boolean value."));
                return true;
            }
            
            sign.setGlowingText(value);
            sign.update();
            player.sendMessage(MCUtils.color(plugin.getConfig().getString("signedit.setglowing").replace("{value}", value + "")));
        } else if (args[0].equalsIgnoreCase("setcolor") || args[0].equalsIgnoreCase("sc")) {
            if (!player.hasPermission("staressentials.command.signedit.setcolor")) {
                player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
                return true;
            }
            
            if (!(args.length > 1)) {
                player.sendMessage(MCUtils.color("&cYou must provide a dye color value."));
                return true;
            }
    
            DyeColor dyeColor;
            try {
                dyeColor = DyeColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(MCUtils.color("&cYou provided an invalid Dye Color."));
                return true;
            }
            
            sign.setColor(dyeColor);
            sign.update();
            player.sendMessage(MCUtils.color(plugin.getConfig().getString("signedit.setcolor").replace("{color}", dyeColor.name())));
        }
        
        return true;
    }
}
