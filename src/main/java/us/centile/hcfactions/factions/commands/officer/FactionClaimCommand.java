package us.centile.hcfactions.factions.commands.officer;

import us.centile.hcfactions.factions.claims.ClaimProfile;
import us.centile.hcfactions.factions.commands.FactionCommand;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.mode.ModeType;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.claims.Claim;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionClaimCommand extends FactionCommand {
    @Command(name = "f.claim", aliases = {"faction.claim", "factions.claim", "factions.claimland", "f.claimland", "faction.claimland"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for(Mode mode : Mode.getModes()) {
            if(mode.getModeType() == ModeType.EOTW && mode.isActive()) {
                player.sendMessage(langConfig.getString("ERROR.EOTW_CLAIM"));
                return;
            }
        }

        Profile profile = Profile.getByPlayer(player);
        player.getInventory().remove(Faction.getWand());

        Faction faction;
        if (command.getArgs().length >= 1 && player.hasPermission("hcf.admin")) {
            String name = command.getArgs(0);
            Faction faction1 = PlayerFaction.getAnyByString(name);
            if (faction1 != null) {
                faction = faction1;
                player.sendMessage(langConfig.getString("FACTION_OTHER.CLAIMING_FOR_OTHER").replace("%FACTION%", faction.getName()));
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            faction = Profile.getByPlayer(player).getFaction();

            if (faction == null) {
                player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                return;
            }

            PlayerFaction playerFaction = (PlayerFaction) faction;

            if (!playerFaction.getLeader().equals(player.getUniqueId()) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("hcf.system")) {
                player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
                return;
            }

        }

        if (!(profile.isViewingMap())) {
            if (!Claim.getNearbyClaimsAt(player.getLocation(), 64).isEmpty()) {
                Bukkit.dispatchCommand(player, "f map");
            }
        }

        player.getInventory().addItem(Faction.getWand());
        player.sendMessage(langConfig.getString("FACTION_CLAIM.RECEIVED_WAND"));
        new ClaimProfile(player, faction);
    }
}
