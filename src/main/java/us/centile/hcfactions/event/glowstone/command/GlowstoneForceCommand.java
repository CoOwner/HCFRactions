package us.centile.hcfactions.event.glowstone.command;

import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.glowstone.GlowstoneEvent;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class GlowstoneForceCommand extends PluginCommand {

    @Command(name = "glowstone.force", permission = "glowstone.force")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/glowstone force <zone>");
            return;
        }

        Event event = EventManager.getInstance().getByName(args[0]);

        if (event == null || (!(event instanceof GlowstoneEvent))) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid Glowstone Mountain.");
            return;
        }

        GlowstoneEvent glowstoneEvent = (GlowstoneEvent) event;
        glowstoneEvent.start();


    }
}
