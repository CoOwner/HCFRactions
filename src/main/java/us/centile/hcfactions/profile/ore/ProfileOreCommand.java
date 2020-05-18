package us.centile.hcfactions.profile.ore;

import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileOreCommand extends PluginCommand {
    @Command(name = "ores", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        String[] args = command.getArgs();
        CommandSender sender = command.getSender();

        Profile profile;
        if (args.length == 0) {
            if (sender instanceof Player) {
                profile = Profile.getByPlayer((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "You're console dumbass.");
                return;
            }
        } else {
            Player player = Bukkit.getPlayer(StringUtils.join(args));
            if (player != null) {
                profile = Profile.getByPlayer(player);
            } else {
                profile = Profile.getByName(StringUtils.join(args));
            }
        }

        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "No player with name '" + StringUtils.join(args) + "' found.");
            return;
        }

        for (String message : langFile.getStringList("ORES.VIEW")) {
            message = message.replace("%PLAYER%", profile.getName());
            for (ProfileOreType type : ProfileOreType.values()) {
                message = message.replace("%" + type.name() + "%", profile.getOres().get(type) + "");
            }
            sender.sendMessage(message);
        }

    }
}
