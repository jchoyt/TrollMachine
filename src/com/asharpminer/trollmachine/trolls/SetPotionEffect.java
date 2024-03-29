package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.asharpminer.trollmachine.TrollMachine;

public class SetPotionEffect extends TrollExecutor{
  private PotionEffect effectToApply;
  private String senderMessage;

  public SetPotionEffect(PotionEffectType e, int duration, String senderMessage ){ //duration in seconds
    // setController(controller);
    this.effectToApply = new PotionEffect(e, duration*20, 4);
    this.senderMessage = senderMessage;
  }

  public boolean gettem(Player sender, Player target){
    target.addPotionEffect(effectToApply);
    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " " + senderMessage);
      target.sendMessage("Compliments of " + sender.getName());
    } else {
      target.sendMessage("Well that backfired.");
    }

    return true;
  }
}
