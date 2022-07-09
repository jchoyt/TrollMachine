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
    this.effectToApply = e;
    this.senderMessage = senderMessage;
  }

  public boolean gettim(Player sender, Player target){
    target.playEffect(effectToApply);

    if(null != sender) {
      sender.sendMessage(target.getDisplayName() + " " + senderMessage);
    }

    return true;
  }
}
