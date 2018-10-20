/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/* Returns a fortune cookie fortune to the player */
public class FranklinCommandExecutor implements CommandExecutor {
    private TrollMachine plugin;
    private Logger logger = null;
    private List<UUID> allowedUsers = new ArrayList<UUID>();

    public FranklinCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        plugin.getCommand("franklin").setExecutor(this);
        //allowedUsers.add(UUID.fromString("61d70a19-7338-4e31-a83d-b8c62ba6ec21")); //Entomo
        //allowedUsers.add(new UUID("558b43b7-173c-4951-bdbd-976adb246c09")); //injudicious
        allowedUsers.add(UUID.fromString("645c676e-4d5f-49eb-8909-575da044ed62")); //kaityizzy
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //verify a player is sending this
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //verify it's kelly, kaity, or me
            if(allowedUsers.contains(player.getUniqueId())) {
                // summon the Franklin
                Location playerLocation = player.getLocation();
                // /summon chicken ~ ~ ~ {CustomName:"{\"text\":\"Franklin\"}"}
                plugin.runCommand(String.format("summon chicken %f %f %f {CustomName:\"{\\\"text\\\":\\\"Franklin\\\"}\"}",
                    playerLocation.getX(),
                    playerLocation.getY() + 3,
                    playerLocation.getZ()), 20L);
            }
            else {
                //set player on Fire
                player.setFireTicks(7 * 20);  // seconds * ticks per second
            }
            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
