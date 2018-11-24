/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/* Returns a fortune cookie fortune to the player */
public class JokeCommandExecutor implements CommandExecutor {
    private TrollMachine plugin;
    private List<String> jokes = new ArrayList<String>();
    private List<String> allJokes = new ArrayList<String>();  // all jokes
    private Logger logger = null;

    public JokeCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        jokes = plugin.loadFile("one-liners.txt", true);  // jokes that don't get kelly awkward questions
        allJokes.addAll(jokes);
        allJokes.addAll(plugin.loadFile("blue-one-liners.txt", true));
        logger = plugin.getLogger();
        PluginCommand c = this.plugin.getCommand("joke");  //don't forget to update plugin.yml
        c.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //verify a player is sending this
        if (sender instanceof Player) {
            Player player = (Player)sender;
            // clean up our act if Kaitlyn is online
            if(plugin.isKaitlynOnline()) {
                plugin.sendQuote(jokes, ChatColor.GRAY +
                    player.getName() + " asked for a joke. " +
                    ChatColor.LIGHT_PURPLE, "");
            } else {
                plugin.sendQuote(allJokes, ChatColor.GRAY +
                    player.getName() + " asked for a joke. " +
                    ChatColor.LIGHT_PURPLE, "");
            }
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
