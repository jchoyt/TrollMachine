/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import com.asharpminer.trollmachine.trolls.*;
import com.asharpminer.trollmachine.TrollCommandExecutor;

public class TrollCommandExecutor implements CommandExecutor {
    private final TrollMachine plugin;
    private Logger logger = null;
    private Map<UUID, Integer> peopleTrolled = new HashMap<UUID, Integer>();  // The number of people trolled and not past the cooldown
    private Map<UUID, Long> lastTrolled = new HashMap<UUID, Long>(); // last time a player trolled someone
    private List<TrollExecutor> trollOptions = new ArrayList<TrollExecutor>();
    private int cooldown = 5 * 60 * 1000;  //five minutes
    // package-private so tests can inject a seeded Random for deterministic backfire/pick behavior
    Random random = new Random();


    public TrollCommandExecutor(TrollMachine plugin) {
        this(plugin, null);
    }

    /**
     * @param trollOptions the effects /troll can pick from; pass null to use the
     *                      real, default set. Tests can supply their own lightweight
     *                      {@link TrollExecutor} stubs instead, since building the real
     *                      effects touches Bukkit's Sound/PotionEffectType registries,
     *                      which need a live server.
     */
    TrollCommandExecutor(TrollMachine plugin, List<TrollExecutor> trollOptions) {
        this.plugin = plugin;           // Store the plugin in situations where you need it.
        logger = plugin.getLogger();
        PluginCommand c = this.plugin.getCommand("troll");  //don't forget to update plugin.yml
        c.setExecutor(this);

        this.trollOptions = trollOptions != null ? trollOptions : buildDefaultTrollOptions();
    }

    private List<TrollExecutor> buildDefaultTrollOptions() {
        List<TrollExecutor> options = new ArrayList<TrollExecutor>();
        options.add(new Cobweb(this));
        options.add(new Lightning(this));
        options.add(new SetEffect(EntityEffect.HURT_BERRY_BUSH, "pricks their finger"));
        options.add(new SetEffect(EntityEffect.TELEPORT_ENDER, "sees purple sparkles"));
        options.add(new SetPotionEffect(PotionEffectType.DARKNESS, 30, "is having trouble seeing"));
        options.add(new SetPotionEffect(PotionEffectType.NAUSEA, 12, "appears drunk"));
        options.add(new SetPotionEffect(PotionEffectType.GLOWING, 120, "is lit up for their enemies to see"));
        options.add(new SetPotionEffect(PotionEffectType.HUNGER, 30, "is very hungry"));
        options.add(new SetPotionEffect(PotionEffectType.LEVITATION, 1, "drifts towards built height"));
        options.add(new SetPotionEffect(PotionEffectType.SLOWNESS, 15, "walks through molassas"));
        options.add(new SetPotionEffect(PotionEffectType.MINING_FATIGUE, 30, "can't dig for @*#&"));
        options.add(new SetPotionEffect(PotionEffectType.SLOW_FALLING, 30, "defies gravity. Mostly"));
        options.add(new PlaySound(this, Sound.ENTITY_CREEPER_PRIMED, "hears a creeper"));
        options.add(new PlaySound(this, Sound.ENTITY_ENDERMAN_SCREAM, "hears an angry Enderman"));
        options.add(new PlaySound(this, Sound.ENTITY_GHAST_SCREAM, "hears a distorted cry"));
        options.add(new PlaySound(this, Sound.ENTITY_WARDEN_DIG, "hears something digging"));
        return options;
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
            UUID senderUuid = player.getUniqueId();
            int newCount = registerTroll(senderUuid, System.currentTimeMillis());
            boolean backfired = rollBackfire(newCount);

            if(backfired) {
              target = player;
              player = null;
            }

           // do something slightly annoying to someone
           pickTrollOption().gettem(player, target);

            return true;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
    }

    /**
     * Updates the cooldown bookkeeping for a player who just used /troll and
     * returns their resulting "trolled recently" count, which drives the
     * backfire odds in {@link #rollBackfire}.
     */
    int registerTroll(UUID senderUuid, long currentTime) {
        int cooldownsPassed = lastTrolled.containsKey(senderUuid) ? (int)(currentTime - lastTrolled.get(senderUuid)) / cooldown : 0;
        int newCount = peopleTrolled.containsKey(senderUuid) ? peopleTrolled.get(senderUuid) + 1 : 1;
        newCount = Math.max(0, ( newCount - cooldownsPassed));
        peopleTrolled.put(senderUuid, newCount);
        lastTrolled.put(senderUuid, currentTime);
        return newCount;
    }

    /** Start with a 5% chance and add 15% for every recent troll. */
    boolean rollBackfire(int newCount) {
        return random.nextDouble() < (0.05 + newCount * 0.15);
    }

    /** Picks one of the registered troll effects at random. */
    TrollExecutor pickTrollOption() {
        int trollNum = (int) (random.nextDouble() * trollOptions.size());
        return trollOptions.get(trollNum);
    }

    public void sendDelayedMessage( Player target, String message, long tickDelay) {
      BukkitScheduler scheduler = target.getServer().getScheduler();
      scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
          @Override
          public void run() {
              target.sendMessage(message);
          }
      }, tickDelay);
    }
}
