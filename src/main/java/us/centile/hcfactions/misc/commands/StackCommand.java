package us.centile.hcfactions.misc.commands;

import org.bukkit.entity.Player;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class StackCommand extends PluginCommand {

    @Command(name = "stack", aliases = {"more"}, inGameOnly = true, permission = "command.stack")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.getInventory().getItemInHand().setAmount(64);
    }


}
