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

import java.util.Set;

public record ListHomeCmd(HomeModule module) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MCUtils.color("&cOnly players may use that command."));
            return true;
        }
        
        if (!player.hasPermission("staressentials.home.list")) {
            player.sendMessage(MCUtils.color("&cYou do not have permission to use that command."));
            return true;
        }
        
        OfflinePlayer target = null;
        boolean other = false;
        
        if (args.length == 0) {
            other = false;
            target = player;
        }
        
        if (args.length > 0) {
            String[] split = args[0].split(":");
            if (player.hasPermission("staressentials.home.list.others")) {
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
                
                other = true;
            } else {
                player.sendMessage(MCUtils.color("&cYou do not have permission to set homes for other players."));
                return true;
            }
        }
        
        Set<Home> homes = module.getHomes(target.getUniqueId());
        StringBuilder sb = new StringBuilder();
        homes.forEach(home -> sb.append(home.getName()).append(" "));
        String homeList = sb.toString().trim();
        if (!other) {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.listhomes.self").replace("{homelist}", homeList)));
        } else {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("settings.messages.listhomes.other").replace("{homelist}", homeList).replace("{player}", target.getName())));
        }
        return true;
    }
}
