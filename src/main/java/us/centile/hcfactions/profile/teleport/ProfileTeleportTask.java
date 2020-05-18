package us.centile.hcfactions.profile.teleport;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.factions.events.player.PlayerInitiateFactionTeleportEvent;
import us.centile.hcfactions.profile.Profile;

public class ProfileTeleportTask extends BukkitRunnable {

    @Getter private PlayerInitiateFactionTeleportEvent event;

    public ProfileTeleportTask(PlayerInitiateFactionTeleportEvent event) {
        this.event = event;

        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void run() {
        if (!(event.isCancelled())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(event.getLocation());
                }
            }.runTask(FactionsPlugin.getInstance());

            Profile.getByPlayer(event.getPlayer()).setTeleportWarmup(null);
        }
    }

}