package us.centile.hcfactions.profile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.centile.hcfactions.combatlogger.CombatLogger;
import us.centile.hcfactions.crate.Crate;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.glowstone.GlowstoneEvent;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.util.player.PlayerUtility;
import us.centile.hcfactions.util.player.SimpleOfflinePlayer;
import us.centile.permissions.profile.Profile;

import java.io.IOException;
import java.util.StringJoiner;


public class ProfileAutoSaver extends BukkitRunnable {

    private JavaPlugin plugin;

    public ProfileAutoSaver(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        Faction.save();

        try {
            SimpleOfflinePlayer.save(plugin);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (us.centile.hcfactions.profile.Profile profile : us.centile.hcfactions.profile.Profile.getProfiles()) {
            profile.save();
        }

        for (Mode mode : Mode.getModes()) {
            mode.save();
        }

        for (CombatLogger logger : CombatLogger.getLoggers()) {
            logger.getEntity().remove();
        }

        for (Event event : EventManager.getInstance().getEvents()) {
            if (event instanceof KothEvent) {
                ((KothEvent) event).save();
            }
            else if (event instanceof GlowstoneEvent) {
                ((GlowstoneEvent) event).save();
            }
        }

        for (Crate crate : Crate.getCrates()) {
            crate.save();
        }

    }

}
