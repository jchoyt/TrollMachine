/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerChatListener implements Listener {

    private TrollMachine plugin = null;
    private Logger logger = null;

    public PlayerChatListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler //(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player p = event.getPlayer();

        if(message.toLowerCase().contains("i'm hungry")) {
            plugin.runCommand("give " + p.getName() + " minecraft:cake", 20L);
        }

        if(message.toLowerCase().contains("@kelly")){
            plugin.asyncBroadcast("...also paging @alsokelly", 20L);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if(Math.random() < 0.05) {
            event.setJoinMessage(ChatColor.YELLOW + "Welcome, " + event.getPlayer().getName() + "! Friendly reminder that Kelly is not the boss.");
        }
    }
}
