package com.asharpminer.trollmachine;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class TrollMachineTest {

    // TrollMachine extends JavaPlugin, whose real constructor requires a
    // running server's PluginClassLoader. Mockito's inline mock maker builds
    // the instance without calling that constructor, so CALLS_REAL_METHODS
    // lets us exercise the plugin's actual logic (loadFile, sendQuote,
    // getPlayerByName) against stubbed Bukkit dependencies.
    private TrollMachine plugin;

    @Before
    public void setUp() {
        plugin = mock(TrollMachine.class, org.mockito.Mockito.CALLS_REAL_METHODS);
    }

    // ---- loadFile ----

    @Test
    public void loadFile_preservesCaseWhenCaseSensitive() {
        doReturn(new StringReader("Hello\nWorld")).when(plugin).loadResource("jokes.txt");

        List<String> lines = plugin.loadFile("jokes.txt", true);

        assertEquals(Arrays.asList("Hello", "World"), lines);
    }

    @Test
    public void loadFile_lowercasesWhenNotCaseSensitive() {
        doReturn(new StringReader("Hello\nWorld")).when(plugin).loadResource("jokes.txt");

        List<String> lines = plugin.loadFile("jokes.txt", false);

        assertEquals(Arrays.asList("hello", "world"), lines);
    }

    @Test
    public void loadFile_returnsEmptyListWhenResourceMissing() {
        doReturn(null).when(plugin).loadResource("missing.txt");

        List<String> lines = plugin.loadFile("missing.txt", true);

        assertTrue(lines.isEmpty());
    }

    // ---- sendQuote ----

    @Test
    public void sendQuote_broadcastsThePickedQuoteWithPrefixAndPostfix() {
        plugin.random = new FixedRandom(0.0); // picks index 0
        List<String> quotes = Arrays.asList("first", "second", "third");

        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            plugin.sendQuote(quotes, "PRE-", "-POST");
            bukkit.verify(() -> Bukkit.broadcastMessage("PRE-first-POST"));
        }
    }

    @Test
    public void sendQuote_picksTheLastQuoteWhenRandomIsNearOne() {
        plugin.random = new FixedRandom(0.999999); // picks the last index
        List<String> quotes = Arrays.asList("first", "second", "third");

        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            plugin.sendQuote(quotes, "", "");
            bukkit.verify(() -> Bukkit.broadcastMessage("third"));
        }
    }

    @Test
    public void sendQuote_wrapsMessagesLongerThan256Characters() {
        plugin.random = new FixedRandom(0.0);
        String longQuote = "word ".repeat(80).trim(); // ~399 chars, well over the 256 limit
        List<String> quotes = List.of(longQuote);

        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            plugin.sendQuote(quotes, "", "");
            // one broadcast per <=256-char chunk, so a ~400 char quote needs at least two
            bukkit.verify(() -> Bukkit.broadcastMessage(anyString()), atLeast(2));
        }
    }

    // ---- getPlayerByName ----

    @Test
    public void getPlayerByName_matchesIgnoringCase() {
        OfflinePlayer alice = mock(OfflinePlayer.class);
        doReturn("Alice").when(alice).getName();
        OfflinePlayer bob = mock(OfflinePlayer.class);
        doReturn("Bob").when(bob).getName();

        Server server = mock(Server.class);
        doReturn(new OfflinePlayer[]{alice, bob}).when(server).getOfflinePlayers();
        doReturn(server).when(plugin).getServer();

        assertSame(bob, plugin.getPlayerByName("bOB"));
    }

    @Test
    public void getPlayerByName_returnsNullWhenNoPlayerHasEverJoined() {
        Server server = mock(Server.class);
        doReturn(new OfflinePlayer[0]).when(server).getOfflinePlayers();
        doReturn(server).when(plugin).getServer();

        assertNull(plugin.getPlayerByName("nobody"));
    }
}
