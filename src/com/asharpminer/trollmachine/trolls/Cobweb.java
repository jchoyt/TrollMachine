package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;

import com.asharpminer.trollmachine.TrollMachine;

public class Cobweb extends TrollExecutor{

  // sets the block at the player's feed to cobweb
  public boolean gettem(Player sender, Player target){
    Location loc = target.getLocation();
    World w = target.getWorld();
    Block  b = w.getBlockAt(loc);
    b.setType(Material.COBWEB);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " finds themself in a sticky situation.");
      target.sendMessage("Free string compliments of " + sender.getName());
    } else {
      target.sendMessage("Well that backfired.");
    }
    return true;
  }
}
