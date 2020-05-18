package us.centile.hcfactions.mode.command.subcommand;

import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

import static com.mongodb.client.model.Filters.eq;

public class ModeDeleteCommand extends PluginCommand {
    @Command(name = "mode.delete", permission = "mode.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /mode delete <sotw/eotw>");
            return;
        }
        String name = StringUtils.join(args).toLowerCase();

        if(name.equalsIgnoreCase("sotw") || name.equalsIgnoreCase("eotw")) {
            Mode mode = Mode.getByName(name);

            if (mode == null) {
                player.sendMessage(ChatColor.RED + "A mode named '" + name + "' does not exist.");
                return;
            }


            main.getFactionsDatabase().getModes().deleteOne(eq("name", mode.getName()));
            Mode.getModes().remove(mode);
            player.sendMessage(ChatColor.RED + "Mode named '" + name + "' successfully deleted.");

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /mode delete <sotw/eotw>");
        }
    }
}
