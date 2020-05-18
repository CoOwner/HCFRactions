package us.centile.hcfactions.profile.options.command;

import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.bukkit.entity.Player;

public class ProfileOptionsCommand extends PluginCommand {
    @Command(name = "options", aliases = "settings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        player.openInventory(profile.getOptions().getInventory());
    }
}
