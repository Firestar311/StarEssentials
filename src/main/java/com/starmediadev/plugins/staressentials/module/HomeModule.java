package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.cmds.*;
import com.starmediadev.plugins.staressentials.lampcmds.HomeCmds;
import com.starmediadev.plugins.staressentials.objects.Home;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.*;

public class HomeModule extends StarEssentialsModule {
    
    private Map<UUID, Set<Home>> homes = new HashMap<>();
    
    public HomeModule(StarEssentials plugin) {
        super(plugin, "homes");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("settings.messages.sethome.self", "&eYou set a home with the name &b{homename}");
        this.defaultConfigValues.put("settings.messages.sethome.other", "&eYou set a home for player &b{target} &ewith the name &b{homename}");
        this.defaultConfigValues.put("settings.messages.sethome.target", "&e{player} &eset a home for you with the name &b{value}");
        this.defaultConfigValues.put("settings.messages.delhome.self", "&eYou deleted a home with the name &b{homename}");
        this.defaultConfigValues.put("settings.messages.delhome.other", "&eYou deleted &b{player}&e's home called {homename}");
        this.defaultConfigValues.put("settings.messages.delhome.target", "&b{player} &edeleted your home called &b{homename}");
        this.defaultConfigValues.put("settings.messages.teleporthome.self", "&eYou teleported to your home &b{homename}");
        this.defaultConfigValues.put("settings.messages.teleporthome.other", "&eYou teleported to &b{player}&e's home &b{homename}");
        this.defaultConfigValues.put("settings.messages.listhomes.self", "&eHomes: &b{homelist}");
        this.defaultConfigValues.put("settings.messages.listhomes.other", "&b{player}&e's Homes: &b{homelist}");
        this.defaultConfigValues.put("settings.messages.renamehome.self", "&eYou renamed the home &b{oldhomename} &eto &b{newhomename}");
        this.defaultConfigValues.put("settings.messages.renamehome.other", "&eYou renamed &b{player}&e's home &b{oldhomename} &eto &b{newhomename}");
        this.defaultConfigValues.put("settings.messages.renamehome.target", "&b{player} &erenamed your home &b{oldhomename} &eto &b{newhomename}");
    }
    
    @Override
    protected void registerLampDependencies(BukkitCommandHandler commandHandler) {
        commandHandler.registerDependency(HomeModule.class, this);
    }
    
    @Override
    protected void registerLampCommands(BukkitCommandHandler commandHandler) {
        commandHandler.register(new HomeCmds());
    }
    
    public void addHome(Home home) {
        if (homes.containsKey(home.getPlayer())) {
            homes.get(home.getPlayer()).add(home);
        } else {
            homes.put(home.getPlayer(), new HashSet<>(Collections.singleton(home)));
        }
    }
    
    public String removeHome(UUID player, String homeName) {
        if (homes.containsKey(player)) {
            Set<Home> homes = this.homes.get(player);
            if (homes.isEmpty()) {
                return "{player} does not have any homes.";
            }
    
            List<Home> filteredHomes = homes.stream().filter(ph -> ph.getName().equalsIgnoreCase(homeName)).toList();
            if (filteredHomes.size() == 0) {
                return "{player} does not have a home with that name.";
            }
            
            homes.remove(filteredHomes.get(0));
        } else {
            return "{player} does not have any homes.";
        }
        return "";
    }
    
    public Set<Home> getHomes(UUID player) {
        Set<Home> homes = this.homes.get(player);
        if (homes == null) {
            return new HashSet<>();
        }
        return new HashSet<>(homes);
    }
    
    public Home findHome(UUID player, String name) {
        Set<Home> homes = this.homes.get(player);
        Home home = null;
    
        for (Home h : homes) {
            if (name.equalsIgnoreCase(h.getName())) {
                home = h;
                break;
            }
        }
        
        return home;
    }
    
    @Override
    protected void loadValuesFromConfig() {
        YamlConfiguration config = getConfig().getConfiguration();
        if (config.contains("homes")) {
            ConfigurationSection homesSection = config.getConfigurationSection("homes");
            homesSection.getKeys(false).forEach(puuid -> {
                UUID uuid = UUID.fromString(puuid);
                ConfigurationSection playerHomesSection = homesSection.getConfigurationSection(puuid);
                if (playerHomesSection != null) {
                    playerHomesSection.getKeys(false).forEach(hn -> {
                        String name = playerHomesSection.getString(hn + ".name");
                        long dateCreated = Long.parseLong(playerHomesSection.getString(hn + ".datecreated"));
                        boolean defaultHome = playerHomesSection.getBoolean(hn + ".defaulthome");
                        Location location = playerHomesSection.getLocation(hn + ".location");
                        
                        Home home = new Home(uuid, name, location, dateCreated, defaultHome);
                        addHome(home);
                    });
                }
            });
        }
    }
    
    @Override
    protected void saveConfigSettings() {
        YamlConfiguration config = getConfig().getConfiguration();
        config.set("homes", null);
        this.config.save();
        this.homes.forEach((player, homes) -> {
            homes.forEach(home -> {
                String basePath = "homes." + player.toString() + "." + home.getName().toLowerCase().replace(" ", "_");
                config.set(basePath + ".name", home.getName());
                config.set(basePath + ".datecreated", home.getDateCreated() + "");
                config.set(basePath + ".defaulthome", home.isDefaultHome());
                config.set(basePath + ".location", home.getLocation());
            });
        });
    }
}
