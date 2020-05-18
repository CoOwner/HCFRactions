package us.centile.hcfactions.mode.command.subcommand;

import us.centile.hcfactions.mode.ModeType;
import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class ModeCreateCommand extends PluginCommand {
    @Command(name = "mode.create", permission = "mode.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /mode create <sotw/eotw>");
            return;
        }

        String name = StringUtils.join(args).toLowerCase();

        if(name.equalsIgnoreCase("sotw") || name.equalsIgnoreCase("eotw")) {
            Mode mode = new Mode(name, false, 0L, ModeType.valueOf(name.toUpperCase()));
            player.sendMessage(ChatColor.RED + "Mode named '" + mode.getName() + "' successfully created.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /mode start <sotw/eotw>");
        }
    }
}
