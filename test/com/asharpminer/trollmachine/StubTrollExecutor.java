package com.asharpminer.trollmachine;

import com.asharpminer.trollmachine.trolls.TrollExecutor;
import org.bukkit.entity.Player;

/**
 * A no-op TrollExecutor, so /troll's selection logic can be tested without
 * touching Bukkit's Sound/PotionEffectType/EntityEffect APIs.
 */
class StubTrollExecutor extends TrollExecutor {
    @Override
    public boolean gettem(Player sender, Player target) {
        return true;
    }
}
