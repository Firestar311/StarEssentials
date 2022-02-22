package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.module.HomeModule;
import com.starmediadev.plugins.staressentials.objects.Home;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record HomeCmd(HomeModule module) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        if (!player.hasPermission("staressentials.home.set")) {
            player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(MCUtils.color("&cYou must provide a home name."));
            return true;
        }
    
        OfflinePlayer target = null;
        String rawHomeName;
        boolean other = false;
        
        if (args[0].contains(":")) {
            String[] split = args[0].split(":");
            if (player.hasPermission("staressentials.home.set.others")) {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if (offlinePlayer.getName().equalsIgnoreCase(split[0])) {
                        target = offlinePlayer;
                        break;
                    }
                }
                
                if (target == null) {
                    player.sendMessage(MCUtils.color("&cThat player name is either invalid or has never joined the server."));
                    return true;
                }
                
                rawHomeName = split[1];
                other = true;
            } else {
                player.sendMessage(MCUtils.color("&cYou do not have permission to set homes for other players."));
                return true;
            }
        } else {
            target = player;
            rawHomeName = args[0];
        }
    
        Set<Home> homes = module.getHomes(target.getUniqueId());
        Stream<Home> matchingHomes = homes.stream().filter(home -> home.getName().equalsIgnoreCase(rawHomeName));
        long homesWithName = matchingHomes.count();
        if (homesWithName > 0) {
            player.sendMessage(MCUtils.color("&cA home with that name does not exist."));
            return true;
        }
        
        if (homesWithName > 1) {
            player.sendMessage(MCUtils.color("&4Critical Problem: Multiple homes exist with that name. This should never happen, contact Plugin Developer"));
            return true;
        }
    
        Home home = matchingHomes.toList().get(0);
        player.teleport(home.getLocation());
    
        if (!other) {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.teleporthome.self").replace("{homename}", rawHomeName)));
        } else {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.teleporthome.other").replace("{homename}", rawHomeName).replace("{player}", target.getName())));
        }
    
        return true;
    }
}
