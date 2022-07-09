/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.command.PluginCommand;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.asharpminer.trollmachine.trolls.Cobweb;
import com.asharpminer.trollmachine.trolls.SetEffect;
import com.asharpminer.trollmachine.trolls.SetPotionEffect;
import com.asharpminer.trollmachine.trolls.TrollExecutor;
import com.asharpminer.trollmachine.trolls.Lightning;


public class TrollCommandExecutor implements CommandExecutor {
    private final TrollMachine plugin;
    private Logger logger = null;
    private Map<UUID, Integer> peopleTrolled = new HashMap<UUID, Integer>();  // The number of people trolled and not past the cooldown
    private Map<UUID, Long> lastTrolled = new HashMap<UUID, Long>(); // last time a player trolled someone
    private List<TrollExecutor> trollOptions = new ArrayList<TrollExecutor>();
    private int cooldown = 5 * 60 * 1000;  //five minutes


    public TrollCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        PluginCommand c = this.plugin.getCommand("troll");  //don't forget to update plugin.yml
        c.setExecutor(this);

        trollOptions.add(new Cobweb());
        trollOptions.add(new SetEffect(EntityEffect.HURT_BERRY_BUSH, "pricks their finger"));
        trollOptions.add(new SetEffect(EntityEffect.TELEPORT_ENDER, "sees purple sparkles"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.DARKNESS, 30, "is having trouble seeing"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.CONFUSION, 12, "appears drunk"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.GLOWING, 120, "is lit up for their enemies to see"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.HUNGER, 30, "is very hungry"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.LEVITATION, 1, "drifts towards built height"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.SLOW, 15, "walks through molassas"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.SLOW_DIGGING, 30, "can't dig for @*#&"));
        trollOptions.add(new SetPotionEffect(PotionEffectType.SLOW_FALLING, 30, "defies gravity. Mostly."));
        trollOptions.add(new Lightning());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // implementation exactly as before...
        if (sender instanceof Player) {
            Player player = (Player)sender;

            if (args.length > 1) {
                sender.sendMessage("You can only troll one fellow player at a time");
                return false;
            }

            // check that the player is known and online
            OfflinePlayer offlinePlayer = plugin.getPlayerByName(args[0]);
            if(offlinePlayer == null) {
              player.sendMessage("Who's that?");
              return true;
            }

            Player target = offlinePlayer.getPlayer();
            if (target == null) {
                player.sendMessage(args[0] + " is not online!");
                return true;
            }

            // check if this backfires
            // Do the cooldown
            long currentTime = System.currentTimeMillis();
            UUID senderUuid = player.getUniqueId();

            // do the cooldown
            int cooldownsPassed = lastTrolled.containsKey(senderUuid) ? (int)(currentTime - lastTrolled.get(senderUuid)) / cooldown : 0;
            int newCount = peopleTrolled.containsKey(senderUuid) ? peopleTrolled.get(senderUuid) + 1 : 1;
            newCount = Math.max(0, ( newCount - cooldownsPassed));
            peopleTrolled.put(senderUuid, newCount);
            lastTrolled.put(senderUuid, currentTime);

            // start with 5% chance and add 15% for every count
            boolean backfired = Math.random() < (0.05 + newCount * 0.15);

            if(backfired) {
              target = player;
              player = null;
            }

           // do something slightly annoying to someone
           double randomValue = Math.random();
           int trollNum = (int) (randomValue * trollOptions.size());
           trollOptions.get(trollNum).gettem(player, target);

            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }
}
