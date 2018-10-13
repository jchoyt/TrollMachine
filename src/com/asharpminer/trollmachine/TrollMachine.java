/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import org.bukkit.Bukkit;
import java.util.Collection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitScheduler;

public final class TrollMachine extends JavaPlugin {
    @Override
    public void onEnable() {
        // load up listeners
        new PlayerChatListener(this);
        //getCommand("troll").setExecutor(new TrollCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }

    /*
     * If a player has ever logged into this server, this returns the OfflinePlayer
     * representation of that player. Returns null if the player has never logged
     * to this server. To see if the player is currently online, use OfflinePlayer.getPlayer()
     * which will return a Player object if they are online or null if they are not.
     */
    public OfflinePlayer getPlayerByName(String name) {
        OfflinePlayer[]  offlinePlayers = this.getServer().getOfflinePlayers();
        OfflinePlayer targetPlayer = null;
        for(OfflinePlayer p: offlinePlayers) {
            if(name.equalsIgnoreCase(p.getName())) {
                targetPlayer = p;
                break;
            }
        }
        return targetPlayer;
    }

    public void runCommand(String command, long delay){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }, delay);
    }
}
