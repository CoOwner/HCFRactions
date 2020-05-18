package us.centile.hcfactions.event.glowstone.procedure.command;

import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.glowstone.GlowstoneEvent;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class GlowstoneRemoveCommand extends PluginCommand {

    @Command(name = "glowstone.remove", aliases = {"glowstone.delete", "glowstoneremove", "removeglowstone"}, permission = "glowstone.remove")
    public void onCommand(CommandArgs command) {

        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/glowstone remove <zone>");
            return;
        }

        Event event = EventManager.getInstance().getByName(args[0]);

        if (event == null || (!(event instanceof GlowstoneEvent))) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid Glowstone Mountain.");
            return;
        }


        GlowstoneEvent glowstoneEvent = (GlowstoneEvent) event;
        sender.sendMessage(ChatColor.YELLOW + "(" + glowstoneEvent.getName() + ") Glowstone Mountain has been removed.");
        glowstoneEvent.remove();

    }
}
