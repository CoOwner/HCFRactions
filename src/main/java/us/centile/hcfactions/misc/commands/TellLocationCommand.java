package us.centile.hcfactions.misc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.factions.type.SystemFaction;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.LocationSerialization;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class TellLocationCommand extends PluginCommand {

    @Command(name = "telllocation", inGameOnly = true, aliases = "tl")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Profile.getByPlayer(player);

        if(profile == null) {
            return;
        }

        PlayerFaction faction = profile.getFaction();

        if (faction == null) {
            player.sendMessage(this.main.getLanguageConfig().getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        faction.sendMessage(this.main.getLanguageConfig().getString("ANNOUNCEMENTS.FACTION.PLAYER_FACTION_CHAT").replace("%PLAYER%", player.getName()).replace("%MESSAGE%", ChatColor.YELLOW + this.getCords(player)).replace("%FACTION%", faction.getName()));
    }

    private String getCords(Player player) {
        return "[" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "]";
    }
}
