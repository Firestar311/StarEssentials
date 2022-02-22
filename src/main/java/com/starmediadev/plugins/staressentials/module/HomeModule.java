package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.staressentials.cmds.DelHomeCmd;
import com.starmediadev.plugins.staressentials.cmds.HomeCmd;
import com.starmediadev.plugins.staressentials.cmds.RenameHomeCmd;
import com.starmediadev.plugins.staressentials.cmds.SetHomeCmd;
import com.starmediadev.plugins.staressentials.objects.Home;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class HomeModule extends StarEssentialsModule {
    
    private Map<UUID, Set<Home>> homes = new HashMap<>();
    
    public HomeModule(StarEssentials plugin) {
        super(plugin, "homes");
    }
    
    @Override
    protected void registerDefaultConfigValues() {
        this.defaultConfigValues.put("settings.messages.sethome.self", "&eYou set a home with the name &b{homename}");
        this.defaultConfigValues.put("settings.messages.sethome.other", "&eYou set a home for player &b{target} with the name &b{homename}");
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
    protected void createCommandExecutors() {
        this.commands.put("sethome", new SetHomeCmd(this));
        this.commands.put("home", new HomeCmd(this));
        this.commands.put("delhome", new DelHomeCmd(this));
        this.commands.put("renamehome", new RenameHomeCmd(this));
    }
    
    public boolean addHome(Home home) {
        if (homes.containsKey(home.getPlayer())) {
            return homes.get(home.getPlayer()).add(home);
        } else {
            return !homes.put(home.getPlayer(), new HashSet<>(Collections.singleton(home))).isEmpty();
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
        return new HashSet<>(this.homes.get(player));
    }
    
    @Override
    protected void loadValuesFromConfig() {
        YamlConfiguration config = getConfig().getConfiguration();
        if (config.contains("homes")) {
            ConfigurationSection homesSection = config.getConfigurationSection("homes");
            homesSection.getKeys(false).forEach(puuid -> {
                UUID uuid = UUID.fromString(puuid);
                ConfigurationSection playerHomesSection = config.getConfigurationSection("puuid");
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
        this.homes.forEach((player, homes) -> {
            YamlConfiguration config = getConfig().getConfiguration();
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
