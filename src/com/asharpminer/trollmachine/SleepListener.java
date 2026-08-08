/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.List;
import java.util.Random;
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
    private Logger logger = null;
    // package-private so tests can inject a seeded Random for deterministic behavior
    Random random = new Random();

    public SleepListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        logger.config("Sleep listener registered");
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        // Only say goodnight occasionally and when people actually get into the bed
        if(!shouldSayGoodnight(event.getBedEnterResult())) return;
        String who = event.getPlayer().getName();
        logger.fine( who + " has slept");
        plugin.asyncBroadcast(ChatColor.DARK_AQUA + "Goodnight dear " + who + "! May you dream wonderful dreams.", 20L);
    }

    /** Decides, for a given bed-enter outcome, whether to say goodnight this time. */
    boolean shouldSayGoodnight(PlayerBedEnterEvent.BedEnterResult result) {
        return !(random.nextDouble() > 0.02 || result.equals(PlayerBedEnterEvent.BedEnterResult.valueOf("OK")));
    }
}
