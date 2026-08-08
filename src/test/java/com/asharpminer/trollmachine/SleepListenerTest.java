package com.asharpminer.trollmachine;

import org.bukkit.Server;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SleepListenerTest {

    private SleepListener listener;

    @Before
    public void setUp() {
        TrollMachine plugin = mock(TrollMachine.class);
        Server server = mock(Server.class);
        doReturn(mock(PluginManager.class)).when(server).getPluginManager();
        doReturn(server).when(plugin).getServer();
        doReturn(Logger.getLogger("TrollMachineTest")).when(plugin).getLogger();

        listener = new SleepListener(plugin);
    }

    @Test
    public void shouldSayGoodnight_trueOnLuckyRollWithNonOkResult() {
        listener.random = new FixedRandom(0.01); // <= 2% chance

        assertTrue(listener.shouldSayGoodnight(BedEnterResult.NOT_POSSIBLE_HERE));
    }

    @Test
    public void shouldSayGoodnight_falseOnUnluckyRoll() {
        listener.random = new FixedRandom(0.5); // > 2% chance

        assertFalse(listener.shouldSayGoodnight(BedEnterResult.NOT_POSSIBLE_HERE));
    }

    @Test
    public void shouldSayGoodnight_falseWhenBedEntryWasOk() {
        // Current behavior: even on a lucky roll, an OK bed-entry result
        // suppresses the goodnight message. This test documents that
        // as-is behavior rather than asserting it's the intended design.
        listener.random = new FixedRandom(0.0);

        assertFalse(listener.shouldSayGoodnight(BedEnterResult.OK));
    }
}
