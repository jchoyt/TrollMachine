/*
 * Copyright 2018 Jeffrey C. Hoyt. Released under the Aapache 2.0 license
 */

package com.asharpminer.trollmachine;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Reader;
import java.io.BufferedReader;
import org.bukkit.Bukkit;
import java.util.Collection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public final class TrollMachine extends JavaPlugin {

    private Logger logger = null;
    public UUID kaitlynUuid = UUID.fromString("645c676e-4d5f-49eb-8909-575da044ed62");

    public TrollMachine() {
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        // load up listeners
        new PlayerChatListener(this);
        new PlayerDeathListener(this); // random death messages when a player dies
        new FortuneCommandExecutor(this);  // /cookie commmand - returns a fortune cookie fortune
        new JokeCommandExecutor(this); //tells a short joke
        new FranklinCommandExecutor(this); //allows kaityizzy to summon a chicken named Franklin
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }

    /*
     * If a player has ever logged into this server, this returns the OfflinePlayer
     * representation of that player. Returns null if the player has never logged
     * to this server. To see if the player is currently online, use OfflinePlayer.getPlayer()
     * which will return a Player object if they are online or null if they are not.
     */
    public OfflinePlayer getPlayerByName(String name) {
        OfflinePlayer[]  offlinePlayers = this.getServer().getOfflinePlayers();
        OfflinePlayer targetPlayer = null;
        for(OfflinePlayer p: offlinePlayers) {
            if(name.equalsIgnoreCase(p.getName())) {
                targetPlayer = p;
                break;
            }
        }
        return targetPlayer;
    }


    public boolean isKaitlynOnline() {
        Player kaitlyn = this.getServer().getPlayer(this.kaitlynUuid);
        return ( kaitlyn != null && kaitlyn.isOnline());
    }

    public void runCommand(String command, long delay){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }, delay);
    }

    /** Broadcast a message for an asynchronous chat **/
    public void asyncBroadcast(String message, long delay){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(message);
            }
        }, delay);
    }

    /**
    * Generic function to load a text file into a List of Strings
    *
    * @param filename DOCUMENT ME!
    * @param caseSensitive DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public List<String> loadFile( String filename, boolean caseSensitive )
    {
        List<String> list = new ArrayList<String>();
        try
        {
            Reader inFile = this.getTextResource(filename);
            if(inFile == null) return list;
            BufferedReader br = new BufferedReader(inFile);
            String tmp;
            tmp = br.readLine(); // read first line of file.
            while ( tmp != null ) { // read a line until end of file.
                if ( caseSensitive ) {
                    list.add( tmp );
                }
                else {
                    list.add( tmp.toLowerCase());
                }
                tmp = br.readLine();
            }
        }
        catch ( Exception e ) {
            getLogger().log(Level.SEVERE, "Error reading config file " + filename, e);
        }
        return list;
    }

    /**
     *  Sends a random selection from quotelist
     */
    public void sendQuote( List<String> quotelist, String prefix, String postfix )
    {
        int MAXLENGTH = 256;
        if ( quotelist.size() == 0 ) {
            logger.severe("Empty quote list given. Please don't do that again." );
            return;
        }

        double randomValue = Math.random(  );
        int quoteNum = ( int ) ( randomValue * quotelist.size(  ) );
        String quote = prefix + quotelist.get( quoteNum ) + postfix;

        while ( quote.length() > MAXLENGTH ) {
            int breakPt = quote.lastIndexOf(" ", MAXLENGTH );
            Bukkit.broadcastMessage(quote.substring( 0, breakPt ));
            quote = quote.substring( breakPt - 1 );
        }
        Bukkit.broadcastMessage(quote);
    }

}
