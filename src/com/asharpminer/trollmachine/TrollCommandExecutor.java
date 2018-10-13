/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollCommandExecutor implements CommandExecutor {
    private final TrollMachine plugin;

    public TrollCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // implementation exactly as before...
        if (sender instanceof Player) {
            Player player = (Player)sender;

            if (args.length > 1) {
                sender.sendMessage("Too many arguments!");
                return false;
            }

            // check that the player is online
            OfflinePlayer offlinePlayer = plugin.getPlayerByName(args[0]);
            Player target = offlinePlayer.getPlayer();

            if (target == null) {
                sender.sendMessage(args[0] + " is not online!");
                return false;
            }
            // do something
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
