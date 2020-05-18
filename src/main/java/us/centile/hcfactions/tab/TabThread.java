package us.centile.hcfactions.tab;

import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import us.centile.hcfactions.util.player.PlayerUtility;

import java.util.*;

public class TabThread extends Thread
{
    private Plugin protocolLib;
    
    public TabThread() {
        this.protocolLib = Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib");
        this.setName("Tab Thread");
        this.setDaemon(false);
    }
    
    @Override
    public void run() {
        while (this.protocolLib != null && this.protocolLib.isEnabled()) {
            for (final Player online : PlayerUtility.getOnlinePlayers()) {
                try {
                    TabHandler.updatePlayer(online);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(250L);
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }
}
