package us.centile.hcfactions.event.koth.procedure.command;

import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class KothRemoveCommand extends PluginCommand {

    @Command(name = "koth.remove", aliases = {"koth.delete", "removekoth", "kothremove"}, permission = "koth.remove")
    public void onCommand(CommandArgs command) {

        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/koth remove <koth>");
            return;
        }

        Event event = EventManager.getInstance().getByName(args[0]);

        if (event == null || (!(event instanceof KothEvent))) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid KoTH.");
            return;
        }


        KothEvent koth = (KothEvent) event;
        sender.sendMessage(ChatColor.YELLOW + "(" + koth.getName() + ") KoTH has been removed.");
        koth.remove();

    }
}
