package com.starmediadev.plugins.staressentials.objects;

import org.bukkit.Location;

import java.util.UUID;
import java.util.regex.Pattern;

public class Home {
    
    public static final Pattern NAME_PATTERN = Pattern.compile("[^abcdefghijklmnopqrstuvwxyz1234567890]", Pattern.CASE_INSENSITIVE);
    
    private final UUID player;
    private String name;
    private Location location;
    private final long dateCreated;
    private boolean defaultHome;
    
    public Home(UUID player, String name, Location location, long dateCreated, boolean defaultHome) {
        this.player = player;
        this.name = name;
        this.location = location;
        this.dateCreated = dateCreated;
        this.defaultHome = defaultHome;
    }
    
    public Home(UUID player, String name, Location location, long dateCreated) {
        this(player, name, location, dateCreated, false);
    }
    
    public UUID getPlayer() {
        return player;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public long getDateCreated() {
        return dateCreated;
    }
    
    public boolean isDefaultHome() {
        return defaultHome;
    }
    
    public void setDefaultHome(boolean defaultHome) {
        this.defaultHome = defaultHome;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
}
