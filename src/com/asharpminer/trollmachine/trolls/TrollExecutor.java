package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;

import com.asharpminer.trollmachine.TrollMachine;
import com.asharpminer.trollmachine.TrollCommandExecutor;

public abstract class TrollExecutor {
  protected TrollCommandExecutor controller;

  public void setController(TrollCommandExecutor c){
    this.controller = c;
  }
  /**
   * Afflicts the target and notifies the sender what happened.  If sender is
   * null, it backfired and no sender notification is necessary.
   */
  public abstract boolean gettem( Player sender, Player target);

}
