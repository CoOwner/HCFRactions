package us.centile.hcfactions.crate.command;

import us.centile.hcfactions.crate.command.subcommand.*;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class CrateCommand extends PluginCommand {

    public CrateCommand() {
        new CrateCreateCommand();
        new CrateDeleteCommand();
        new CrateItemsCommand();
        new CrateKeyCommand();
        new CrateListCommand();
    }

    @Command(name = "ccrate", permission = "crate.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender player = command.getSender();
        player.sendMessage(ChatColor.RED + "/ccreate list");
        player.sendMessage(ChatColor.RED + "/ccrate create <name>");
        player.sendMessage(ChatColor.RED + "/ccrate delete <name>");
        player.sendMessage(ChatColor.RED + "/ccrate items <name>");
        player.sendMessage(ChatColor.RED + "/ccrate key <name> <amount> <player>");
    }
}
