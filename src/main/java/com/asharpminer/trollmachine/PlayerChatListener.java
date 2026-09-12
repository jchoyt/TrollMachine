/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.logging.Logger;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;


public class PlayerChatListener implements Listener {

    private TrollMachine plugin = null;
    private Logger logger = null;
    private int cakes = 0;
    private List<String> welcomes = new ArrayList<String>();
    private final Set<UUID> blockedPlayers = new HashSet<>();

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

        // blockedPlayers.add(plugin.entomoUuid);  //for testing purposes
        blockedPlayers.add(plugin.kaitlynUuid);
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
        if(Math.random() < 0.03) {
            int quoteNum = ( int ) ( Math.random() * welcomes.size() );
            event.setJoinMessage(ChatColor.YELLOW + "Welcome, " + event.getPlayer().getName() + "! " + welcomes.get( quoteNum ));
        }
    }

    private final List<String> msgCommands = Arrays.asList("/msg", "/tell", "/w", "/whisper");
    /**  Prevents kaity from getting private messages */
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String[] args = message.split(" ");
        String command = args[0].toLowerCase();

        // Check if the command is a private messaging command
        if (msgCommands.contains(command)) {
            if (args.length < 2) return; // Not enough arguments to target a player

            String targetName = args[1];
            Player targetPlayer = Bukkit.getPlayerExact(targetName);

            if (targetPlayer != null && blockedPlayers.contains(targetPlayer.getUniqueId())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cThis player is currently not receiving private messages.");
            }
        }
    }
}
