package com.asharpminer.trollmachine;

import com.asharpminer.trollmachine.trolls.TrollExecutor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrollCommandExecutorTest {

    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    private static final int STUB_OPTION_COUNT = 5;

    private TrollMachine plugin;
    private TrollCommandExecutor executor;

    @Before
    public void setUp() {
        plugin = mock(TrollMachine.class);
        doReturn(mock(PluginCommand.class)).when(plugin).getCommand("troll");

        // The real troll effects touch Bukkit's Sound/PotionEffectType registries,
        // which need a live server. Stub effects let us test the selection/cooldown
        // logic in isolation instead.
        List<TrollExecutor> stubOptions = new ArrayList<TrollExecutor>();
        for (int i = 0; i < STUB_OPTION_COUNT; i++) {
            stubOptions.add(new StubTrollExecutor());
        }
        executor = new TrollCommandExecutor(plugin, stubOptions);
    }

    // ---- registerTroll: cooldown bookkeeping ----

    @Test
    public void registerTroll_startsAtOneForANewSender() {
        assertEquals(1, executor.registerTroll(UUID.randomUUID(), 0L));
    }

    @Test
    public void registerTroll_incrementsWithinTheCooldownWindow() {
        UUID sender = UUID.randomUUID();
        executor.registerTroll(sender, 0L);

        assertEquals(2, executor.registerTroll(sender, 1000L)); // well inside 5 minutes
    }

    @Test
    public void registerTroll_decaysOneStepPerCooldownWindowElapsed() {
        UUID sender = UUID.randomUUID();
        executor.registerTroll(sender, 0L);
        executor.registerTroll(sender, 1000L);
        executor.registerTroll(sender, 2000L); // count is now 3

        int count = executor.registerTroll(sender, 2000L + FIVE_MINUTES); // exactly one window later
        assertEquals(3, count); // (3 + 1 new) - 1 decay
    }

    @Test
    public void registerTroll_neverGoesBelowZero() {
        UUID sender = UUID.randomUUID();
        executor.registerTroll(sender, 0L);

        int count = executor.registerTroll(sender, 0L + FIVE_MINUTES * 10L); // 10 windows later
        assertEquals(0, count);
    }

    // ---- rollBackfire: 5% base chance, +15% per recent troll ----

    @Test
    public void rollBackfire_baseChanceIsFivePercentWithNoRecentTrolls() {
        executor.random = new FixedRandom(0.04);
        assertTrue(executor.rollBackfire(0));

        executor.random = new FixedRandom(0.05);
        assertFalse(executor.rollBackfire(0));
    }

    @Test
    public void rollBackfire_addsFifteenPercentPerRecentTroll() {
        executor.random = new FixedRandom(0.19);
        assertTrue(executor.rollBackfire(1)); // threshold 0.05 + 0.15 = 0.20

        executor.random = new FixedRandom(0.20);
        assertFalse(executor.rollBackfire(1));
    }

    // ---- pickTrollOption ----

    @Test
    public void pickTrollOption_spansTheFullOptionListWithoutError() {
        // guards against an off-by-one at the randomValue-near-1.0 boundary
        for (double v = 0.0; v < 1.0; v += 0.01) {
            executor.random = new FixedRandom(v);
            assertNotNull(executor.pickTrollOption());
        }
    }

    @Test
    public void pickTrollOption_choosesDifferentEffectsAcrossTheRange() {
        executor.random = new FixedRandom(0.0);
        TrollExecutor lowest = executor.pickTrollOption();

        executor.random = new FixedRandom(0.999999);
        TrollExecutor highest = executor.pickTrollOption();

        assertNotSame(lowest, highest);
    }

    // ---- onCommand: argument validation / messaging (no Bukkit world interaction) ----

    @Test
    public void onCommand_rejectsNonPlayerSenders() {
        CommandSender console = mock(CommandSender.class);

        boolean result = executor.onCommand(console, mock(Command.class), "troll", new String[]{"bob"});

        assertFalse(result);
        verify(console).sendMessage("You must be a player!");
    }

    @Test
    public void onCommand_rejectsMoreThanOneArgument() {
        Player sender = mock(Player.class);

        boolean result = executor.onCommand(sender, mock(Command.class), "troll", new String[]{"a", "b"});

        assertFalse(result);
        verify(sender).sendMessage("You can only troll one fellow player at a time");
    }

    @Test
    public void onCommand_repliesWhenTargetHasNeverJoined() {
        Player sender = mock(Player.class);
        doReturn(null).when(plugin).getPlayerByName("nobody");

        boolean result = executor.onCommand(sender, mock(Command.class), "troll", new String[]{"nobody"});

        assertTrue(result);
        verify(sender).sendMessage("Who's that?");
    }

    @Test
    public void onCommand_repliesWhenTargetIsKnownButOffline() {
        Player sender = mock(Player.class);
        OfflinePlayer offline = mock(OfflinePlayer.class);
        doReturn(null).when(offline).getPlayer(); // known, but not currently online
        doReturn(offline).when(plugin).getPlayerByName("bob");

        boolean result = executor.onCommand(sender, mock(Command.class), "troll", new String[]{"bob"});

        assertTrue(result);
        verify(sender).sendMessage("bob is not online!");
    }
}
