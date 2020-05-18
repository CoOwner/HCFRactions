package us.centile.hcfactions.misc.listeners;

import org.bukkit.Bukkit;
import us.centile.hcfactions.factions.events.FactionAllyFactionEvent;
import us.centile.hcfactions.factions.events.FactionEnemyFactionEvent;
import us.centile.hcfactions.factions.events.player.PlayerJoinFactionEvent;
import us.centile.hcfactions.factions.events.player.PlayerLeaveFactionEvent;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.factions.events.player.PlayerDisbandFactionEvent;
import us.centile.hcfactions.factions.type.PlayerFaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.centile.hcfactions.profile.kit.events.ArcherTagEvent;
import us.centile.hcfactions.profile.kit.events.ArcherTagRemoveEvent;
import us.centile.hcfactions.util.player.PlayerUtility;

import java.util.HashSet;
import java.util.Set;

public class ScoreboardListeners implements Listener {

    @EventHandler
    public void onJoinFaction(PlayerJoinFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByPlayer(player);
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByPlayer(allyPlayer);
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onLeaveFaction(PlayerLeaveFactionEvent event) {
        Set<Player> toLoop = new HashSet<>(event.getFaction().getOnlinePlayers());
        toLoop.add(event.getPlayer());
        for (Player player : toLoop) {
            Profile profile = Profile.getByPlayer(player);
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByPlayer(allyPlayer);
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onDisbandFaction(PlayerDisbandFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByPlayer(player);
            profile.updateTab();
        }
        for (PlayerFaction ally : event.getFaction().getAllies()) {
            for (Player allyPlayer : ally.getOnlinePlayers()) {
                Profile profile = Profile.getByPlayer(allyPlayer);
                profile.updateTab();
            }
        }
    }


    @EventHandler
    public void onAllyFaction(FactionAllyFactionEvent event) {
        for (PlayerFaction faction : event.getFactions()) {
            for (Player player : faction.getOnlinePlayers()) {
                Profile profile = Profile.getByPlayer(player);
                profile.updateTab();
            }
        }
    }

    @EventHandler
    public void onArcherTagEvent(ArcherTagEvent event) {
        for (Player player : PlayerUtility.getOnlinePlayers() ) {
            Profile profile = Profile.getByPlayer(player);
            profile.updateTab();
        }
    }

    @EventHandler
    public void onArcherTagRemoveEvent(ArcherTagRemoveEvent event) {
        for (Player player : PlayerUtility.getOnlinePlayers() ) {
            Profile profile = Profile.getByPlayer(player);
            profile.updateTab();
        }
    }

    @EventHandler
    public void onEnemyFaction(FactionEnemyFactionEvent event) {
        for (PlayerFaction faction : event.getFactions()) {
            for (Player player : faction.getOnlinePlayers()) {
                Profile profile = Profile.getByPlayer(player);
                profile.updateTab();
            }
        }
    }

}
