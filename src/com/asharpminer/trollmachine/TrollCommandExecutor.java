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
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.asharpminer.trollmachine.trolls.Cobweb;
import com.asharpminer.trollmachine.trolls.SetEffect;
import com.asharpminer.trollmachine.trolls.SetPotionEffect;
import com.asharpminer.trollmachine.trolls.TrollExecutor;
import com.asharpminer.trollmachine.trolls.Lightning;


public class TrollCommandExecutor implements CommandExecutor {
    private final TrollMachine plugin;
    private Map<UUID, Integer> peopleTrolled = new HashMap<UUID, Integer>();
    private Map<UUID, Long> lastTrolled = new HashMap<UUID, Long>();
    private List<TrollExecutor> trollOptions = new ArrayList<TrollExecutor>();

    public TrollCommandExecutor(TrollMachine plugin) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        trollOptions.add(new Cobweb(plugin));
        trollOptions.add(new SetEffect(plugin, EntityEffect.FIREWORK_EXPLODE, "puts on a show"));
        trollOptions.add(new SetEffect(plugin, EntityEffect.HURT_BERRY_BUSH, "pricks their finger"));
        trollOptions.add(new SetEffect(plugin, EntityEffect.LOVE_HEARTS, "fell in love"));
        trollOptions.add(new SetEffect(plugin, EntityEffect.VILLAGER_ANGRY, "appears angry"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.DARKNESS, 30, "is having trouble seeing"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.CONFUSION, 12, "appears drunk"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.GLOWING, 120, "is lit up for their enemies to see"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.HARM, 4, "stubbed their toe"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.HUNGER, 60, "is very hungry"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.LEVITATION, 8, "drifts towards built height"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.SLOW, 15, "walks through molassas"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.WITHER, 12, "withers away"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.SLOW_DIGGING, 30, "can't dig for @*#&"));
        trollOptions.add(new SetPotionEffect(plugin, PotionEffectType.SLOW_FALLING, 120, "defies gravity. Mostly."));
        trollOptions.add(new Lightning(plugin));

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
