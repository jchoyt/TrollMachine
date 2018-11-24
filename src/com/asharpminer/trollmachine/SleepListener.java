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
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;


public class SleepListener implements Listener {

    private TrollMachine plugin = null;

    public SleepListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        String who = event.getPlayer().getName();
        plugin.asyncBroadcast("Goodnight dear " + who + "! May you dream wonderful dreams.", 20L);
    }
}
