package us.centile.hcfactions.inventory.command;

import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GiveInventoryCommand extends PluginCommand {

    @Command(name = "giveinv", aliases = {"sendinv", "cpto"}, permission = "inventory.clone")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        Player toSend;
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/" + command.getLabel() + " <player>");
            return;
        } else {
            toSend = Bukkit.getPlayer(StringUtils.join(args));
            if (toSend == null) {
                player.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found.");
                return;
            }
        }

        toSend.getInventory().setContents(player.getInventory().getContents());
        toSend.getInventory().setArmorContents(player.getInventory().getArmorContents());
        player.sendMessage(ChatColor.RED + "Inventory successfully sent.");
    }
}
