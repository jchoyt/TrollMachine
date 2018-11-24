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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;


public class ManHuntFailListener implements Listener {

    private TrollMachine plugin = null;
    private String manHuntFailMessage = "You survived but did not verify that you were not AFK!";

    public ManHuntFailListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler //(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if(manHuntFailMessage.equals(event.getMessage())){  //TODO also check sender
            java.util.Set<Player> recipients = event.getRecipients();
            plugin.asyncBroadcast(recipients.size() + " people got that", 20L);
        }
    }

}
