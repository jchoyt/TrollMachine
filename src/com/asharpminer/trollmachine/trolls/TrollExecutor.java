package com.asharpminer.trollmachine.trolls;

import org.bukkit.entity.Player;

import com.asharpminer.trollmachine.TrollMachine;

public abstract class TrollExecutor {

  /**
   * Afflicts the target and notifies the sender what happened.  If sender is
   * null, it backfired and no sender notification is necessary.
   */
  public abstract boolean gettim( Player sender, Player target);

}
