/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.scheduler.BukkitScheduler;
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
    private int cakes = 0;
    private List<String> welcomes = new ArrayList<String>();

    public PlayerChatListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        welcomes.addAll(plugin.loadFile("special_welcome.txt", true));

        // set up cooldown for cakes
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(cakes > 0) cakes--;
            }
        }, 0L, 5L * 60L * 20L); // minutes * sec/min * ticks/sec
    }

    @EventHandler //(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player p = event.getPlayer();

        if(message.toLowerCase().contains("i'm hungry")) {
            if(cakes < 5) {
                plugin.runCommand("give " + p.getName() + " minecraft:cake", 20L);
                cakes++;
            }
            else {
                plugin.runCommand("kick " + p.getName() + " Don't abuse the bot.", 20L);
            }
        }

        if(message.toLowerCase().contains("@kelly")){
            plugin.asyncBroadcast("...also paging @alsokelly", 20L);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if(Math.random() < 10.03) {
            int quoteNum = ( int ) ( Math.random() * welcomes.size() );
            event.setJoinMessage(ChatColor.YELLOW + "Welcome, " + event.getPlayer().getName() + "! " + welcomes.get( quoteNum ));
        }
    }
}
