package us.centile.hcfactions.crate.command.subcommand;

import us.centile.hcfactions.crate.Crate;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class CrateListCommand extends PluginCommand {
    @Command(name = "ccrate.list", permission = "crate.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(ChatColor.GREEN + "Listing all registered crates:");
        for (Crate crate : Crate.getCrates()) {
            sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + crate.getName());
        }

    }
}
