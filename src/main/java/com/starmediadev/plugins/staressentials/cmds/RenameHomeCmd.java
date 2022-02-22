package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.module.HomeModule;
import com.starmediadev.plugins.staressentials.objects.Home;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

public record RenameHomeCmd(HomeModule module) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        if (!player.hasPermission("staressentials.home.rename")) {
            player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        if (!(args.length > 1)) {
            player.sendMessage(MCUtils.color("&cUsage: /renamehome <home> <newname>"));
            return true;
        }
        
        OfflinePlayer target = null;
        String rawHomeName;
        boolean other = false;
        
        if (args[0].contains(":")) {
            String[] split = args[0].split(":");
            if (player.hasPermission("staressentials.home.rename.others")) {
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
                player.sendMessage(MCUtils.color("&cYou do not have permission to delete homes from other players."));
                return true;
            }
        } else {
            target = player;
            rawHomeName = args[0];
        }
        
        Set<Home> homes = module.getHomes(target.getUniqueId());
        List<Home> filteredHomes = homes.stream().filter(ph -> ph.getName().equalsIgnoreCase(rawHomeName)).toList();
        
        if (filteredHomes.size() == 0) {
            player.sendMessage(MCUtils.color("&cA home with that name does not exist"));
            return true;
        }
    
        if (filteredHomes.size() > 1) {
            player.sendMessage(MCUtils.color("&4Critical Problem: Multiple homes exist with that name. This should never happen, contact Plugin Developer"));
            return true;
        }
        
        Home home = filteredHomes.get(0);
        
        String newName = args[1];
    
        List<Home> newNameFilteredHomes = homes.stream().filter(ph -> ph.getName().equalsIgnoreCase(newName)).toList();
        
        if (newNameFilteredHomes.size() > 0) {
            player.sendMessage(MCUtils.color("&cA home with the new name already exists."));
            return true;
        }
    
        Matcher matcher = Home.NAME_PATTERN.matcher(newName);
        if (matcher.matches()) {
            player.sendMessage(MCUtils.color("&cInvalid character(s) in the home name. Only a-z, 0-9 are allowed."));
            return true;
        }
    
        home.setName(newName);
    
        if (!other) {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.renamehome.self").replace("{newhomename}", newName).replace("{oldhomename}", rawHomeName)));
        } else {
            if (target.isOnline()) {
                Player onlineTarget = (Player) target;
                onlineTarget.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.message.renamehome.target").replace("{oldhomename}", rawHomeName).replace("{player}", player.getName()).replace("{newhomename}", newName)));
            }
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.renamehome.other").replace("{oldhomename}", rawHomeName).replace("{target}", target.getName()).replace("{newhomename}", newName)));
        }
        
        return true;
    }
}
