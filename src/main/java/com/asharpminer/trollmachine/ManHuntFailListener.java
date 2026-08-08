/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */
package com.asharpminer.trollmachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;


import io.github.tejedu.manhunt.AfkEvent;


public class ManHuntFailListener implements Listener {

    private TrollMachine plugin = null;
    private List<Material> materialList = new ArrayList<Material>();
    private Random random = new Random();
    private Logger log = null;

    public ManHuntFailListener(TrollMachine plugin) {
        this.plugin = plugin;  // Store the plugin in situations where you need it.
        log = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        log.info("ManHunt listener registered");

        //load the materials from the prize list
        materialList.addAll(NonParticipationPrizes.crapPrizes);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTargetAfk(AfkEvent event) {
        log.info(event.getPlayer().getName() + " failed to register as the target");
        awardPrize(event.getPlayer());
    }

    /*
     * Give prize to recepient
    */
    public void awardPrize(Player awardPrizeTo) {
        // only give pity prizes 50% of the time
        if(Math.random() < 0.5) return;

        ItemStack prize = randomItemStack();
        String prizeName = prize.getType().name().replace("_", " ");

        // award the prize
        PlayerInventory inventory = awardPrizeTo.getInventory();
        Map<Integer,ItemStack> leftovers = inventory.addItem(new ItemStack[] { prize });

        // drop extras at their feet if the inventory is full
        if(!leftovers.isEmpty()) {
            World world = awardPrizeTo.getWorld();
            Location location = awardPrizeTo.getLocation();
            // Drop items at their feet
            for(ItemStack itemStack : leftovers.values()) {
                world.dropItem(location, itemStack);
            }
        }

        //announce it
        plugin.asyncBroadcast( "" + ChatColor.ITALIC + ChatColor.LIGHT_PURPLE +
            awardPrizeTo.getName() + ChatColor.RESET +
            " is either asleep or distracted. TrollMachine to the rescue! " +
            ChatColor.ITALIC + ChatColor.LIGHT_PURPLE + awardPrizeTo.getName() +
            ChatColor.RESET + " was awarded " + prize.getAmount() + " " + prizeName + "!" , 20L);
    }

    /*
     * Create a random ItemStack from the Prize list. There will be 1 to
     * (5/max stack size) tiems in the stack.
     */
    private ItemStack randomItemStack() {
        ItemStack item;
        Material material = materialList.get(random.nextInt(materialList.size()));
        Integer limit = material.getMaxStackSize();
        if(limit > 5) limit = 5;
        return new ItemStack(material, 1 + random.nextInt(limit));
    }

}
