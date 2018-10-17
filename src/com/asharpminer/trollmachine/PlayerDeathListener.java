/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerDeathListener implements Listener {

    private TrollMachine plugin = null;
    private Logger logger = null;
    private List<String> deathMessages = new ArrayList<String>();


    public PlayerDeathListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        deathMessages = plugin.loadFile("death_messages.txt", true);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event)
    {
        LivingEntity deader = event.getEntity();
        logger.info("something died");

        if(deader instanceof Player){// && Math.random() < 0.3) {
            Player p = (Player)deader;
            logger.info(p.getName() + " died");
            plugin.sendQuote(deathMessages, ChatColor.GOLD + "", "");
        }
    }
}
