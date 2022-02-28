package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.HomeModule;
import com.starmediadev.plugins.staressentials.objects.Home;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

public class HomeCmds {
    
    private static final String DELHOME_PERM = "staressentials.commands.home.delete";
    private static final String HOME_PERM = "staressentials.command.home.teleport";
    private static final String HOME_LIST_PERM = "staressentials.command.home.list";
    private static final String RENAME_HOME_PERM = "staressentials.command.home.rename";
    private static final String SET_HOME_PERM = "staressentials.command.home.set";
    @Dependency
    private HomeModule homeModule;
    
    @Command({"delhome", "deletehome"})
    @CommandPermission(DELHOME_PERM)
    public void deleteHome(Player player, @Named("homeName") String homeName) {
        Object[] info = getHome(player, DELHOME_PERM, homeName);
        Home home = (Home) info[0];
        OfflinePlayer target = (OfflinePlayer) info[1];
        boolean other = (boolean) info[2];
        
        String message = homeModule.removeHome(home.getPlayer(), home.getName());
        if (!Objects.equals(message, "")) {
            player.sendMessage(MCUtils.color(message.replace("{player}", target.getName())));
        } else {
            if (!other) {
                player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.delhome.self").replace("{homename}", home.getName())));
            } else {
                if (target.isOnline()) {
                    Player onlineTarget = (Player) target;
                    onlineTarget.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.delhome.target").replace("{homename}", home.getName()).replace("{player}", player.getName())));
                }
                player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.delhome.other").replace("{homename}", home.getName()).replace("{target}", target.getName())));
            }
        }
    }
    
    @Command("home")
    @CommandPermission(HOME_PERM)
    public void home(Player player, @Named("homeName") @Optional String homeName) {
        Object[] info = getHome(player, HOME_PERM, homeName);
        Home home = (Home) info[0];
        OfflinePlayer target = (OfflinePlayer) info[1];
        boolean other = (boolean) info[2];
        
        player.teleport(home.getLocation());
        
        if (!other) {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.teleporthome.self").replace("{homename}", home.getName())));
        } else {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.teleporthome.other").replace("{homename}", home.getName()).replace("{player}", target.getName())));
        }
    }
    
    @Command("listhomes")
    @CommandPermission(HOME_LIST_PERM)
    public void listHomes(Player player, @Named("target") @Optional OfflinePlayer target) {
        boolean other = false;
        if (target != null) {
            other = player.getUniqueId().equals(target.getUniqueId());
        } else {
            target = player;
        }
        
        Set<Home> homes = homeModule.getHomes(target.getUniqueId());
        StringBuilder sb = new StringBuilder();
        homes.forEach(home -> sb.append(home.getName()).append(" "));
        String homeList = sb.toString().trim();
        if (!other) {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.listhomes.self").replace("{homelist}", homeList)));
        } else {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.listhomes.other").replace("{homelist}", homeList).replace("{player}", target.getName())));
        }
    }
    
    @Command("renamehome")
    @CommandPermission(RENAME_HOME_PERM)
    public void renameHome(Player player, @Named("homeName") String homeName, @Named("newHomeName") String newHomeName) {
        Object[] info = getHome(player, RENAME_HOME_PERM, homeName);
        Home home = (Home) info[0];
        OfflinePlayer target = (OfflinePlayer) info[1];
        boolean other = (boolean) info[2];
        
        Matcher matcher = Home.NAME_PATTERN.matcher(newHomeName);
        if (matcher.matches()) {
            throw new CommandErrorException("You have an invalid character in the new home name. Only a-z, 0-9 is allowed");
        }
        
        if (homeModule.findHome(target.getUniqueId(), newHomeName) != null) {
            throw new CommandErrorException("A home already exists for the new home name.");
        }
        
        home.setName(newHomeName);
        
        if (!other) {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.renamehome.self").replace("{newhomename}", newHomeName).replace("{oldhomename}", homeName)));
        } else {
            if (target.isOnline()) {
                Player onlineTarget = (Player) target;
                onlineTarget.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.message.renamehome.target").replace("{oldhomename}", homeName).replace("{player}", player.getName()).replace("{newhomename}", newHomeName)));
            }
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.renamehome.other").replace("{oldhomename}", newHomeName).replace("{target}", target.getName()).replace("{newhomename}", newHomeName)));
        }
    }
    
    @Command("sethome")
    @CommandPermission(SET_HOME_PERM)
    public void setHome(Player player, @Named("homeName") String homeName) {
        Object[] info = getHome(player, RENAME_HOME_PERM, homeName);
        Home home = (Home) info[0];
        OfflinePlayer target = (OfflinePlayer) info[1];
        boolean other = (boolean) info[2];
    
        Matcher matcher = Home.NAME_PATTERN.matcher(homeName);
        if (matcher.matches()) {
            throw new CommandErrorException("You have an invalid character in the home name. Only a-z, 0-9 is allowed");
        }
    
        if (home != null) {
            throw new CommandErrorException("A home already exists for the new home name.");
        }
    
        if (!other) {
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.sethome.self").replace("{homename}", homeName)));
        } else {
            if (target.isOnline()) {
                Player onlineTarget = (Player) target;
                onlineTarget.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.sethome.target").replace("{homename}", homeName).replace("{player}", player.getName())));
            }
            player.sendMessage(MCUtils.color(homeModule.getConfig().getConfiguration().getString("settings.messages.sethome.other").replace("{homename}", homeName).replace("{target}", target.getName())));
        }
    }
    
    private Object[] getHome(Player player, String permission, String homeName) {
        OfflinePlayer target = null;
        String rawHomeName;
        boolean other = false;
        
        if (homeName.contains(":")) {
            String[] split = homeName.split(":");
            if (player.hasPermission(permission + ".others")) {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if (offlinePlayer.getName().equalsIgnoreCase(split[0])) {
                        target = offlinePlayer;
                        break;
                    }
                }
                
                if (target == null) {
                    throw new CommandErrorException("&cThat player name is either invalid or has never joined the server.");
                }
                
                rawHomeName = split[1];
                other = true;
            } else {
                throw new CommandErrorException("&cYou do not have to do that to other players.");
            }
        } else {
            target = player;
            rawHomeName = homeName;
        }
        
        Home home = homeModule.findHome(target.getUniqueId(), rawHomeName);
        return new Object[]{home, target, other};
    }
}
