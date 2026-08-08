package io.github.tejedu.manhunt;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Compile-only stand-in for the real class of the same name in
 * https://github.com/jchoyt/ManHunt, used only when the real ManHunt.jar
 * (see libs/README.md) isn't present locally — e.g. in CI, where ManHunt
 * is a soft dependency and not built from source. This is never packaged
 * or run; see build.gradle for where it's conditionally added to the
 * compile classpath.
 */
public class AfkEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public AfkEvent(Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
