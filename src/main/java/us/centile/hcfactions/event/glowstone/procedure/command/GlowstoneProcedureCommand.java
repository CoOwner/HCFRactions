package us.centile.hcfactions.event.glowstone.procedure.command;

import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.glowstone.procedure.GlowstoneCreateProcedure;
import us.centile.hcfactions.event.glowstone.procedure.GlowstoneCreateProcedureStage;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.PluginCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

public class GlowstoneProcedureCommand extends PluginCommand {

    @Command(name = "glowstone.create", aliases = {"glowstone.new", "createglowstone", "newglowstone"}, permission = "glowstone.create")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        String[] args = command.getArgs();

        if (profile.getGlowstoneCreateProcedure() != null) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.RED + "You're already in the process of creating a Glowstone Mountain.");
            player.sendMessage(ChatColor.RED + "Type 'cancel' in chat to stop the procedure.");
            player.sendMessage(" ");
            return;
        }

        if (args.length == 0) {
            profile.setGlowstoneCreateProcedure(new GlowstoneCreateProcedure().stage(GlowstoneCreateProcedureStage.NAME_SELECTION));
            player.sendMessage(" ");
            player.sendMessage(ChatColor.YELLOW + "Please type a name for this Glowstone Mountain in chat.");
            player.sendMessage(" ");
        } else {

            if (EventManager.getInstance().getByName(StringUtils.join(args)) != null) {
                player.sendMessage(" ");
                player.sendMessage(ChatColor.RED + "An event with that name already exists.");
                player.sendMessage(" ");
                return;
            }

            profile.setGlowstoneCreateProcedure(new GlowstoneCreateProcedure().stage(GlowstoneCreateProcedureStage.ZONE_SELECTION).name(StringUtils.join(args)));
            player.sendMessage(" ");
            player.sendMessage(ChatColor.YELLOW + "You have received the zone wand.");
            player.sendMessage(" ");
            player.getInventory().removeItem(GlowstoneCreateProcedure.getWand());
            player.getInventory().addItem(GlowstoneCreateProcedure.getWand());
        }

    }
}
