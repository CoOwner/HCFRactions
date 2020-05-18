package us.centile.hcfactions.kits.command.subcommand;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import us.centile.hcfactions.crate.Crate;
import us.centile.hcfactions.kits.Kit;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class KitListCommand extends PluginCommand {
    @Command(name = "kit.list", permission = "kit.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(ChatColor.GREEN + "Listing all registered kits:");
        for (Kit kit : Kit.getKits()) {
            sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + kit.getName());
        }

    }
}
