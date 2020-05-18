package us.centile.hcfactions.profile.fight.command;

import org.bukkit.entity.Player;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class KillStreakCommand extends PluginCommand {
    @Command(name = "killstreak", aliases = "ks")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getStringList("KILL_STREAK.HELP_MENU").toArray(new String[FactionsPlugin.getInstance().getLanguageConfig().getStringList("KILL_STREAK.HELP_MENU").size()]));
    }
}
