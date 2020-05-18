package us.centile.hcfactions.misc.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class FocusCommand extends PluginCommand {

    @Command(name = "faction.focus", aliases = {"f.focus", "focus"}, inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /focus <player>");
            return;
        }

        Player toCheck = Bukkit.getPlayer(StringUtils.join(args));

        if (toCheck == null) {
            player.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found online.");
            return;
        }

        if(toCheck.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.RED + "You can't focus yourself.");
            return;
        }

        Profile profile = Profile.getByPlayer(player);
        Profile toCheckProfile = Profile.getByPlayer(toCheck);

        if(profile == null || toCheckProfile == null) {
            return;
        }

        PlayerFaction faction = profile.getFaction();

        if (faction == null) {
            player.sendMessage(this.main.getLanguageConfig().getString("ERROR.NOT_IN_FACTION"));
            return;
        }

        if(faction.getOnlinePlayers().contains(toCheck)) {
            player.sendMessage(ChatColor.RED + "You can't focus your faction members.");
            return;
        }

        if(toCheckProfile.getFaction() != null && faction.getAllies().contains(toCheckProfile.getFaction())) {
            player.sendMessage(ChatColor.RED + "You can't focus your allies.");
            return;
        }

        faction.setFocusPlayer(toCheck.getUniqueId());
        faction.sendMessage(ChatColor.LIGHT_PURPLE + toCheck.getName() + ChatColor.YELLOW + " has been focused by " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.YELLOW + ".");

        for(Player member : faction.getOnlinePlayers()) {

            Profile memberProfile = Profile.getByPlayer(member);

            if(memberProfile != null) {
                memberProfile.updateTab();
            }
        }

    }

}
