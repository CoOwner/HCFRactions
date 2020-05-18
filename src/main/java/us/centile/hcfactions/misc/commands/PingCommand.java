package us.centile.hcfactions.misc.commands;

import us.centile.hcfactions.util.PluginCommand;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends PluginCommand {

    @Command(name = "ping", aliases = {"lag", "ms"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        Player toCheck;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /ping <player>");
                return;
            }
            toCheck = (Player) sender;
        } else {
            toCheck = Bukkit.getPlayer(StringUtils.join(args));
        }

        if (toCheck == null) {
            sender.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found online.");
            return;
        }

        sender.sendMessage(ChatColor.BLUE + toCheck.getName() + (toCheck.getName().endsWith("s") ? "'" : "'s") + ChatColor.GRAY + " current ping is " + ChatColor.WHITE + getPing(toCheck) + "ms" + ChatColor.GRAY + ".");
        if (sender instanceof Player && !toCheck.getName().equals(sender.getName())) {
            Player senderPlayer = (Player) sender;
            sender.sendMessage(ChatColor.GRAY + "Ping difference: " + ChatColor.WHITE + (Math.max(getPing(senderPlayer), getPing(toCheck)) - Math.min(getPing(senderPlayer), getPing(toCheck)) + "ms") + ChatColor.GRAY + ".");
        }
    }

    public int getPing(Player player) {
        int ping = ((CraftPlayer)player).getHandle().ping;

        if (ping >= 100) {
            return ping - 30;
        }

        if (ping >= 50) {
            return ping - 20;
        }

        if (ping >= 20) {
            return ping - 10;
        }

        return ping;
    }

}
