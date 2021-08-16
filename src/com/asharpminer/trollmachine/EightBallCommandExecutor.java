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
public class EightBallCommandExecutor implements CommandExecutor {
    private TrollMachine plugin;
    private List<String> answers = new ArrayList<String>();
    private Logger logger = null;

    public EightBallCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        answers.addAll(plugin.loadFile("8ball.txt", true));
        logger = plugin.getLogger();
        PluginCommand c = this.plugin.getCommand("8ball");  //don't forget to update plugin.yml
        c.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("You gotta give me some question to work with here!");
            return true;
        }
        //verify a player is sending this
        if (sender instanceof Player) {
            Player player = (Player)sender;
            plugin.sendQuote(answers, ChatColor.AQUA +
                player.getName() + " asked, \"" +
                String.join(" ", args) + "\". \n" +
                ChatColor.GOLD + "The magic 8 ball says, \""
                , "\"");
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
