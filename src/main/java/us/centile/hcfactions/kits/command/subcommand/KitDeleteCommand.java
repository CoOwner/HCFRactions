package us.centile.hcfactions.kits.command.subcommand;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import us.centile.hcfactions.crate.Crate;
import us.centile.hcfactions.kits.Kit;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

import static com.mongodb.client.model.Filters.eq;

public class KitDeleteCommand extends PluginCommand {

    @Command(name = "kit.delete", permission = "kit.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /kit delete <name>");
            return;
        }

        String name = StringUtils.join(args);
        Kit kit = Kit.getByName(name);

        if (kit == null) {
            player.sendMessage(ChatColor.RED + "A kit named '" + name + "' does not exist.");
            return;
        }

        main.getFactionsDatabase().getCrates().deleteOne(eq("name", kit.getName()));
        Kit.getKits().remove(kit);
        player.sendMessage(ChatColor.RED + "Kit named '" + name + "' successfully deleted.");
    }
}
