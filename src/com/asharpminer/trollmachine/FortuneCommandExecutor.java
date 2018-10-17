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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/* Returns a fortune cookie fortune to the player */
public class FortuneCommandExecutor implements CommandExecutor {
    private TrollMachine plugin;
    private List<String> fortunes = new ArrayList<String>();
    private Logger logger = null;

    public FortuneCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        fortunes = plugin.loadFile("fortunes.txt", true);
        logger = plugin.getLogger();
        plugin.getCommand("cookie").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //verify a player is sending this
        if (sender instanceof Player) {
            Player player = (Player)sender;
            plugin.sendQuote(fortunes, ChatColor.GRAY +
                player.getName() + " requested a fortune cookie. The fortune reads: " +
                ChatColor.DARK_AQUA, "");
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
