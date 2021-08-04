/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/* Returns a fortune cookie fortune to the player */
public class WolverineCommandExecutor implements CommandExecutor {
    private TrollMachine plugin;
    private Logger logger = null;
    private List<UUID> allowedUsers = new ArrayList<UUID>();

    public WolverineCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        plugin.getCommand("wolverine").setExecutor(this);
        allowedUsers.add(plugin.kaitlynUuid); //kaityizzy
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //verify a player is sending this
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //verify it's kelly, kaity, or me
            if(allowedUsers.contains(player.getUniqueId())) {
                // give user regen effect for 10 seconds
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 2)); // seconds * ticks per second
            }
            else {
                //set player on Fire
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 4)); // seconds * ticks per second
                //player.setFoodLevel(8);
            }
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
