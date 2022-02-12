package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record TimeCmd(StarEssentials plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        World world = MCUtils.getWorld(sender);
        if (world == null) {
            sender.sendMessage(MCUtils.color("&cOnly console and players can use that command."));
            return true;
        }
        
        enum TimeFormat {
            TWELVE, TWENTY_FOUR, UNDEFINED
        }
        
        String configTimeFormat = plugin.getConfig().getString("time.displayformat");
        
        TimeFormat timeFormat = switch (configTimeFormat) {
            case "12", "12h", "12hr", "12hour", "12 hour", "12 hours" -> TimeFormat.TWELVE;
            case "24", "24h", "24hr", "24hour", "24 hour", "24 hours" -> TimeFormat.TWENTY_FOUR;
            default -> TimeFormat.UNDEFINED;
        };
        
        if (timeFormat == TimeFormat.UNDEFINED) {
            sender.sendMessage(MCUtils.color("&cThe value for the time format in the config is invalid, defaulting to the 24 hour format."));
            timeFormat = TimeFormat.TWENTY_FOUR;
        }
        
        if (args.length == 0) {
            String formattedTime = switch (timeFormat) {
                case TWELVE -> MCUtils.getWorldTimeAs12Hr(world);
                case TWENTY_FOUR -> MCUtils.getWorldTimeAs24Hr(world);
                case UNDEFINED -> null;
            };
            sender.sendMessage(MCUtils.color(plugin.getConfig().getString("time.current").replace("{world}", world.getName()).replace("{time}", formattedTime)));
            return true;
        }
        
        if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("s")) {
            int time;
            
            if (!(args.length > 1)) {
                sender.sendMessage(MCUtils.color("&cYou must provide a time."));
                return true;
            }
            
            String timeArg = args[1];
            if (timeArg.contains(":")) {
                String[] timeArray;
                if (timeArg.contains("am") || timeArg.contains("pm")) {
                    String meridian;
                    if (timeArg.contains("am")) {
                        meridian = "am";
                    } else {
                        meridian = "pm";
                    }
                    timeArg = timeArg.replace("am", "").replace("pm", "");
                    timeArray = timeArg.split(":");
                    int hours;
                    try {
                        hours = Integer.parseInt(timeArray[0]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MCUtils.color("&cThe value provided for hours is not a valid number."));
                        return true;
                    }
                    
                    if (hours < 0) {
                        sender.sendMessage(MCUtils.color("&cThe hours value is negative, time must be a positive number."));
                        return true;
                    }
                    
                    if (hours > 12) {
                        sender.sendMessage(MCUtils.color("&cThe value for the hours is invalid for am"));
                        return true;
                    }
                    
                    if (hours == 12) {
                        hours = 0;
                    }
                    
                    if (meridian.equals("am")) {
                        time = MCUtils.ticksPerHour * hours;
                    } else {
                        time = MCUtils.ticksPerHour * (hours + 12);
                    }
                } else {
                    timeArray = timeArg.split(":");
                    int hours = getTimeArgument(sender, "hours", timeArray[0]);
                    if (hours == -1) {
                        return true;
                    }
                    time = MCUtils.ticksPerHour * hours;
                }
                int minutes = getTimeArgument(sender, "minutes", timeArray[1]);
                if (minutes == -1) {
                    return true;
                }
                time += MCUtils.ticksPerMinute * minutes;
    
                int seconds;
                try {
                    seconds = Integer.parseInt(timeArray[2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    seconds = 0;
                } catch (NumberFormatException e) {
                    sender.sendMessage(MCUtils.color("&cYou provided an invalid number for the seconds value."));
                    return true;
                }
                
                time += MCUtils.ticksPerSecond * seconds;
                time -= 6000;
            } else {
                try {
                    time = Integer.parseInt(timeArg);
                } catch (NumberFormatException e) {
                    time = MCUtils.nameToTicks.get(timeArg.toLowerCase());
                }
            }
           
            world.setTime(time);
            String formattedTime = switch (timeFormat) {
                case TWELVE -> MCUtils.getWorldTimeAs12Hr(world);
                case TWENTY_FOUR -> MCUtils.getWorldTimeAs24Hr(world);
                case UNDEFINED -> null;
            };
            sender.sendMessage(MCUtils.color(plugin.getConfig().getString("time.set").replace("{world}", world.getName()).replace("{time}", formattedTime)));
        }
        
        return true;
    }
    
    private int getTimeArgument(CommandSender sender, String type, String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage(MCUtils.color("&cYou did not provide a " + type + " value."));
            return -1;
        } catch (NumberFormatException e) {
            sender.sendMessage(MCUtils.color("&cYou provided an invalid number for the " + type + " value."));
            return -1;
        }
    }
    
    private String formatTimeUnit(long time) {
        if (time == 0) {
            return "00";
        } else if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }
}
