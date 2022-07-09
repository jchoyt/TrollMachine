package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;

import com.asharpminer.trollmachine.TrollMachine;

public class Cobweb extends TrollExecutor{
  public Cobweb(TrollMachine plugin){
    this.plugin = plugin;
  }

  public boolean gettim(Player sender, Player target){
    Location loc = target.getLocation();
    World w = target.getWorld();
    Block  b = w.getBlockAt(loc);
    b.setType(Material.COBWEB);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " finds themself in a sticky situation.");
    }

    return true;
  }
}
