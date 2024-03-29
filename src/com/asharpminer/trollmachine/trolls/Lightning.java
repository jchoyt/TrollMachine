package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.Random;
import com.asharpminer.trollmachine.TrollCommandExecutor;

public class Lightning extends TrollExecutor{

  private Random r = new Random();

  public Lightning(TrollCommandExecutor controller){
    setController(controller);
  }

  public boolean gettem(Player sender, Player target){
    Location loc = target.getLocation();
    World w = target.getWorld();

    Location strikeLocation = new Location(
      w,
      loc.getX() + createOffset(20),
      loc.getY(),
      loc.getZ() + createOffset(20) );

    w.strikeLightning(strikeLocation);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " is not happy with Thor.");
      controller.sendDelayedMessage(target, "Light show compliments of " + sender.getName(), 140L);
    } else {
      target.sendMessage("Well that backfired.");
    }

    return true;
  }

  private double createOffset(int magnitude) {
    return ((r.nextDouble() - 0.5 ) * magnitude);
  }
}
