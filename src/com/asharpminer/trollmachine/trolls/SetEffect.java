package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.EntityEffect;

import com.asharpminer.trollmachine.TrollMachine;

public class SetEffect extends TrollExecutor{
  private EntityEffect effectToApply;
  private String senderMessage;

  public SetEffect(EntityEffect e, String senderMessage ){
    // setController(controller);
    this.effectToApply = e;
    this.senderMessage = senderMessage;
  }

  public boolean gettem(Player sender, Player target){
    target.playEffect(effectToApply);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " " + senderMessage);
      target.sendMessage("Compliments of " + sender.getName());
    } else {
      target.sendMessage("Well that backfired.");
    }
    return true;
  }
}
