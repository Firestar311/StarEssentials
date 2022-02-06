package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.plugins.starmcutils.util.ServerProperties;
import com.starmediadev.utils.helper.TimeHelper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public record WeatherCmd(StarEssentials plugin) implements CommandExecutor {
    
    public static final long MAX_WEATHER_DURATION = 1999999999000L, DEFAULT_DURATION = 300000L;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        World world;
        if (sender instanceof ConsoleCommandSender) {
            world = Bukkit.getWorld(ServerProperties.getLevelName());
        } else if (sender instanceof Player player) {
            world = player.getWorld();
        } else {
            sender.sendMessage(MCUtils.color("&cOnly players can the Console can use that command."));
            return true;
        }
        
        if (world == null) {
            sender.sendMessage(MCUtils.color("&cCould not determine the world for the command context."));
            return true;
        }
        
        if (!(args.length > 0)) {
            sender.sendMessage(MCUtils.color("&cYou must provide a weather type."));
            return true;
        }
        
        enum WeatherType {
            CLEAR, THUNDER, STORM
        }
        
        WeatherType type;
        try {
            type = WeatherType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(MCUtils.color("&cInvalid weather type: " + args[0]));
            return true;
        }
        
        long rawDuration = DEFAULT_DURATION;
        
        if (args.length > 1) {
            try {
                rawDuration = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {}
            
            if (rawDuration == DEFAULT_DURATION) {
                try {
                    rawDuration = TimeHelper.parseTime(args[1]);
                } catch (Exception e) {}
            }
            
            if (rawDuration == DEFAULT_DURATION) {
                sender.sendMessage(MCUtils.color("&cInvalid duration argument, using the default value."));
            }
        }
        
        if (rawDuration >= MAX_WEATHER_DURATION) {
            sender.sendMessage(MCUtils.color("&cThe duration " + args[1] + " is higher than the maxium duration of " + TimeHelper.niceTime((int) (MAX_WEATHER_DURATION / 1000))));
            return true;
        }
        
        if (rawDuration < 0) {
            sender.sendMessage(MCUtils.color("&cThe duration " + args[1] + " is lower than 0. This is not a valid duration amount."));
            return true;
        }
    
        int duration = (int) (rawDuration / 1000);
        
        switch (type) {
            case STORM -> {
                world.setStorm(true);
                world.setWeatherDuration(duration);
            }
            case THUNDER -> {
                world.setThundering(true);
                world.setThunderDuration(duration);
            }
            case CLEAR -> {
                world.setStorm(false);
                world.setThundering(false);
                world.setClearWeatherDuration(duration);
            }
        }
        
        sender.sendMessage(MCUtils.color(plugin.getConfig().getString("weather.format").replace("{type}", type.name().toLowerCase()).replace("{duration}", TimeHelper.niceTime(duration)).replace("{world}", world.getName())));
        return true;
    }
}
