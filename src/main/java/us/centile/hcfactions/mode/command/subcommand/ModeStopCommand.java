package us.centile.hcfactions.mode.command.subcommand;

import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.mode.ModeType;
import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class ModeStopCommand extends PluginCommand {
    @Command(name = "mode.stop", permission = "mode.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /mode stop <sotw/eotw>");
            return;
        }
        String name = StringUtils.join(args).toLowerCase();

        if(name.equalsIgnoreCase("sotw") || name.equalsIgnoreCase("eotw")) {
            Mode mode = Mode.getByName(name);

            if (mode == null) {
                player.sendMessage(ChatColor.RED + "A mode named '" + name + "' does not exist.");
                return;
            }

            if(mode.getModeType() == ModeType.SOTW) {
                mode.setActive(false);
                mode.setStartingTime(0L);
            }
            else if(mode.getModeType() == ModeType.EOTW) {

                mode.setActive(false);
                mode.setStartingTime(0L);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "f setdtr * 0");

                Event event = EventManager.getInstance().getByName("EOTW");

                if(event != null && event instanceof KothEvent) {
                    KothEvent kothEvent = (KothEvent) event;
                    if(kothEvent.isActive()) {
                        kothEvent.stop(true);
                    }
                }
            }

            player.sendMessage(ChatColor.RED + "Mode named '" + name + "' successfully stoped.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /mode stop <sotw/eotw>");
        }
    }
}
