package us.centile.hcfactions.profile.protection.life.command;

import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.profile.protection.life.ProfileProtectionLifeType;
import us.centile.hcfactions.profile.protection.life.command.subcommand.ProfileProtectionLifeAddCommand;
import us.centile.hcfactions.profile.protection.life.command.subcommand.ProfileProtectionLifeRemoveCommand;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import us.centile.hcfactions.profile.protection.life.command.subcommand.ProfileProtectionLifeSetCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileProtectionLifeCommand extends PluginCommand {

    public ProfileProtectionLifeCommand() {
        new ProfileProtectionLifeAddCommand();
        new ProfileProtectionLifeRemoveCommand();
        new ProfileProtectionLifeSetCommand();
    }

    @Command(name = "lives", aliases = {"lifes"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Profile profile;
        if (sender instanceof Player) {
            profile = Profile.getByPlayer((Player) sender);
        } else {
            sender.sendMessage(ChatColor.RED + "You're console dumbass.");
            return;
        }

        for (String message : langFile.getStringList("LIVES.COMMAND.VIEW")) {
            message = message.replace("%PLAYER%", profile.getName());

            for (ProfileProtectionLifeType type : ProfileProtectionLifeType.values()) {
                message = message.replace("%" + type.name() + "_LIVES%", profile.getLives().get(type) + "");
            }

            sender.sendMessage(message);
        }

        if (Bukkit.getPlayer(profile.getUuid()) == null) {
            Profile.getProfilesMap().remove(profile.getUuid());
        }
    }
}
