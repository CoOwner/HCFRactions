package us.centile.hcfactions.tab;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import us.centile.hcfactions.FactionsPlugin;

public class TabListener implements Listener {

    private FactionsPlugin plugin;

    public TabListener(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                TabHandler.addPlayer(event.getPlayer());
            }
        }.runTaskLater(plugin, 10L);
    }
    
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {
        TabHandler.removePlayer(event.getPlayer());
        TabLayout.remove(event.getPlayer());
    }
}
