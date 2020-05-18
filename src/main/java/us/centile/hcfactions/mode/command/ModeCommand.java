package us.centile.hcfactions.mode.command;

import us.centile.hcfactions.mode.command.subcommand.ModeCreateCommand;
import us.centile.hcfactions.mode.command.subcommand.ModeDeleteCommand;
import us.centile.hcfactions.mode.command.subcommand.ModeStartCommand;
import us.centile.hcfactions.mode.command.subcommand.ModeStopCommand;
import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class ModeCommand extends PluginCommand {

    public ModeCommand() {
        new ModeCreateCommand();
        new ModeDeleteCommand();
        new ModeStartCommand();
        new ModeStopCommand();
    }

    @Command(name = "mode", permission = "mode.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender player = command.getSender();
        player.sendMessage(ChatColor.RED + "/mode create <name>");
        player.sendMessage(ChatColor.RED + "/mode delete <name>");
        player.sendMessage(ChatColor.RED + "/mode start <name>");
        player.sendMessage(ChatColor.RED + "/mode stop <name>");
    }
}
