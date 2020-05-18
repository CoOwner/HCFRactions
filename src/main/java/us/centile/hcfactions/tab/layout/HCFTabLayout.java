package us.centile.hcfactions.tab.layout;

import com.google.common.collect.Lists;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.profile.cooldown.ProfileCooldownType;
import us.centile.hcfactions.profile.options.item.ProfileOptionsItemState;
import us.centile.hcfactions.tab.LayoutProvider;
import us.centile.hcfactions.tab.TabLayout;
import org.bukkit.entity.*;
import org.bukkit.*;
import us.centile.hcfactions.util.MapSorting;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.claims.Claim;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.factions.type.SystemFaction;
import us.centile.hcfactions.util.LocationSerialization;
import us.centile.hcfactions.util.player.PlayerUtility;

import java.util.*;


public class HCFTabLayout implements LayoutProvider
{
    @Override
    public TabLayout provide(final Player player, FactionsPlugin plugin) {

        final TabLayout layout = TabLayout.create(player);

        if (player == null) {
            return null;
        }

        Profile profile = Profile.getByPlayer(player);

        if (profile == null) {
            return null;
        }

        if(profile.getOptions().getModifyTabList() == ProfileOptionsItemState.TAB_VANILLA) {

            int count = 0;

            for(Player online : PlayerUtility.getOnlinePlayers() ) {
                if(count <= 60) {
                    layout.forceSet(count, this.getRelationWithPlayer(player, online) + online.getName());
                    count++;
                }
            }

            return layout;
        }


        layout.set(1, 0, plugin.getLanguageConfig().getString("TAB.BRACKETS_NAME"));

        layout.set(1, 1, ChatColor.GOLD + "Players Online");
        layout.set(1, 2, ChatColor.GRAY.toString() + PlayerUtility.getOnlinePlayers() .size() + "/" + Bukkit.getMaxPlayers());

        layout.set(0, 0, ChatColor.GOLD + "Player Info");
        layout.set(0, 1, ChatColor.GRAY + "Kills: " + profile.getKillCount());
        layout.set(0, 2, ChatColor.GRAY + "Deaths: " + profile.getDeathCount());
        layout.set(0, 3, ChatColor.GRAY + "Balance: " + "$" + profile.getBalance());

        layout.set(0, 5, ChatColor.GOLD + "Your Location");

        Claim claim = Claim.getProminentClaimAt(player.getLocation());

        if(claim != null) {
            layout.set(0, 6, this.getFixedFactionName(player, claim.getFaction()));
        } else {
            layout.set(0, 6, ChatColor.DARK_GREEN + "Wilderness");
        }

        final String direction = this.getCardinalDirection(player);
        if (direction != null) {
            layout.set(0, 7, ChatColor.GRAY + "(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [" + direction + "]");
        }
        else {
            layout.set(0, 7, ChatColor.GRAY + "(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ")");
        }

        layout.set(0, 9, ChatColor.GOLD + "Faction Info");


        PlayerFaction faction = Profile.getByPlayer(player).getFaction();

        if (faction != null) {
            layout.set(0, 10, ChatColor.GRAY + "DTR: " + faction.getDeathsTillRaidable());
            layout.set(0, 11, ChatColor.GRAY + "Online: " + faction.getOnlinePlayers().size() + "/" + faction.getMembers().size());
            layout.set(0, 12, ChatColor.GRAY + "Balance: " + "$" + faction.getBalance());
            if (faction.getHome() != null) {
                Location homeLocation = LocationSerialization.deserializeLocation(faction.getHome());
                layout.set(0, 13, ChatColor.GRAY + "Home: " + homeLocation.getBlockX() + ", " + homeLocation.getBlockZ());
            }

            layout.set(1, 4, ChatColor.GOLD + faction.getName());
            int count = 5;

            for (String member : this.getFactionOnlineUsers(faction)) {
                layout.set(1, count, member);
                ++count;
            }
        }
        else {
            layout.set(0, 10, ChatColor.GRAY + "None");
        }

        for (Event possibleEvent : EventManager.getInstance().getEvents()) {
            if (possibleEvent != null && possibleEvent instanceof KothEvent && possibleEvent.isActive()) {
                KothEvent koth = (KothEvent) possibleEvent;
                layout.set(0, 14, ChatColor.GOLD + "KOTH Info");
                layout.set(0, 15, ChatColor.GRAY + "Name: " + ChatColor.YELLOW + koth.getName());
                layout.set(0, 16, "" + ChatColor.GRAY + (FactionsPlugin.getInstance().isKitmapMode() ? this.getKitMapKothLocation(koth) : this.getKothLocation(koth)));
            }
        }

        if(profile.getOptions().getModifyTabList() == ProfileOptionsItemState.TAB_DETAILED_VANILLA) {
            layout.set(2, 0, ChatColor.RED + "Faction List");
            int count = 1;

            final Map<PlayerFaction, Integer> factionOnlineMap = new HashMap<PlayerFaction, Integer>();
            for (final Player target : PlayerUtility.getOnlinePlayers() ) {
                if (player.canSee(target)) {
                    final PlayerFaction playerFaction = Profile.getByUuid(target.getUniqueId()).getFaction();
                    if (playerFaction == null) {
                        continue;
                    }
                    factionOnlineMap.put(playerFaction, factionOnlineMap.getOrDefault(playerFaction, 0) + 1);
                }
            }

            final List<Map.Entry<PlayerFaction, Integer>> sortedMap = (List<Map.Entry<PlayerFaction, Integer>>) MapSorting.sortedValues(factionOnlineMap, (Comparator) Comparator.reverseOrder());
            for (final Map.Entry<PlayerFaction, Integer> sortedEntry : sortedMap) {
                layout.set(2, count, this.getFixedFactionName(player, sortedEntry.getKey()) + ChatColor.GRAY + " (" + sortedEntry.getValue() + ")");
                ++count;
            }
        }


        else if(profile.getOptions().getModifyTabList() == ProfileOptionsItemState.TAB_DETAILED) {
            layout.set(2, 0, ChatColor.GOLD + "End Portals");
            layout.set(2, 1, ChatColor.GRAY + "1000, 1000");
            layout.set(2, 2, ChatColor.GRAY + "in each quadrant");

            layout.set(2, 4, ChatColor.GOLD + "Kit");
            layout.set(2, 5, ChatColor.GRAY + "Prot 1, Sharp 1");

            layout.set(2, 7, ChatColor.GOLD + "Border");
            layout.set(2, 8, ChatColor.GRAY + "3000");
        }

        return layout;
    }

    private String getCardinalDirection(final Player player) {
        double rot = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rot < 0.0) {
            rot += 360.0;
        }
        return this.getDirection(rot);
    }

    private String getKothLocation(KothEvent event) {
        return (event.getZone().getCenter().getX() < 0 ? "-500" : "500") + ", " + event.getZone().getCenter().getBlockY() + ", " + (event.getZone().getCenter().getZ() < 0 ? "-500" : "500");
    }

    private String getKitMapKothLocation(KothEvent event) {
        return (event.getZone().getCenter().getX() < 0 ? "-250" : "250") + ", " + event.getZone().getCenter().getBlockY() + ", " + (event.getZone().getCenter().getZ() < 0 ? "-250" : "250");
    }

    private String getDirection(final double rot) {
        if (0.0 <= rot && rot < 22.5) {
            return "W";
        }
        if (22.5 <= rot && rot < 67.5) {
            return "NW";
        }
        if (67.5 <= rot && rot < 112.5) {
            return "N";
        }
        if (112.5 <= rot && rot < 157.5) {
            return "NE";
        }
        if (157.5 <= rot && rot < 202.5) {
            return "E";
        }
        if (202.5 <= rot && rot < 247.5) {
            return "SE";
        }
        if (247.5 <= rot && rot < 292.5) {
            return "S";
        }
        if (292.5 <= rot && rot < 337.5) {
            return "SW";
        }
        if (337.5 <= rot && rot < 360.0) {
            return "W";
        }
        return null;
    }

    private ChatColor getRelationWithPlayer(Player player, Player target) {

        PlayerFaction playerFaction = Profile.getByPlayer(player).getFaction();
        PlayerFaction targetFaction = Profile.getByPlayer(target).getFaction();

        if(player.getName().equalsIgnoreCase(target.getName())) {
            return ChatColor.DARK_GREEN;
        }

        if(playerFaction == null) {
            return ChatColor.YELLOW;
        }

        if(targetFaction == null) {
            return ChatColor.YELLOW;
        }

        if(playerFaction.equals(targetFaction)) {
            return ChatColor.DARK_GREEN;
        }

        if(playerFaction.getAllies().contains(targetFaction)) {
            return ChatColor.LIGHT_PURPLE;
        }

        if (Profile.getByPlayer(target).getCooldownByType(ProfileCooldownType.ARCHER_TAG) != null) {
            return ChatColor.RED;
        }

        if(playerFaction.getFocusPlayer() != null && playerFaction.getFocusPlayer() == target.getUniqueId()) {
            return ChatColor.DARK_RED;
        }

        return ChatColor.YELLOW;
    }

    private String getFixedFactionName(Player player, Faction faction) {

        if (faction == null) {
            return null;
        }

        if (faction instanceof SystemFaction) {
            return ((SystemFaction) faction).getColor() + faction.getName();
        }

        PlayerFaction playerFaction = Profile.getByPlayer(player).getFaction();

        if (playerFaction == null) {
            return ChatColor.GOLD + faction.getName();
        } else if (faction instanceof PlayerFaction && playerFaction.getAllies().contains(faction)) {
            return ChatColor.LIGHT_PURPLE + faction.getName();
        } else if (playerFaction.equals(faction)) {
            return ChatColor.GREEN + faction.getName();
        } else {
            return ChatColor.YELLOW + faction.getName();
        }
    }

    private List<String> getFactionOnlineUsers(PlayerFaction faction) {

        List<String> list = new ArrayList<>();

        if(faction != null) {

            Player leader = Bukkit.getPlayer(faction.getLeader());

            if(leader != null && leader.isOnline()) {
                list.add(ChatColor.DARK_GREEN + leader.getName() + ChatColor.GRAY + "**");
            }

            for(UUID officers : faction.getOfficers()) {
                Player offcier = Bukkit.getPlayer(officers);

                if(offcier != null && offcier.isOnline()) {
                    list.add(ChatColor.DARK_GREEN + offcier.getName() + ChatColor.GRAY + "*");
                }
            }

            for(Player members : faction.getNoRoleMembers()) {
                list.add(ChatColor.DARK_GREEN + members.getName());
            }
        }

        return list;
    }

}
