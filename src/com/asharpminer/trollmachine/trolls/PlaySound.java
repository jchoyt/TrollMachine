package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Sound;
import java.util.Random;
import com.asharpminer.trollmachine.TrollCommandExecutor;

public class PlaySound extends TrollExecutor{

  private Random r = new Random();
  private Sound sound;
  private String message;

  public PlaySound(TrollCommandExecutor controller, Sound sound, String senderMessage ){
    setController(controller);
    this.sound = sound;
    this.message = senderMessage;
  }

  public boolean gettem(Player sender, Player target){
    Location loc = target.getLocation();
    World w = target.getWorld();

    Location source = new Location(
      w,
      loc.getX() + createOffset(20),
      loc.getY() + createOffset(20),
      loc.getZ() + createOffset(20) );

    target.playSound(source, sound, 20.0F, 1.0F);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " " + message);
      controller.sendDelayedMessage(target, "Compliments of " + sender.getName(), 100L);
    } else {
      target.sendMessage("Well that backfired.");
    }

    return true;
  }

  private double createOffset(int magnitude) {
    return ((r.nextDouble() - 0.5 ) * magnitude);
  }
}
