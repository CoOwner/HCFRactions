package us.centile.hcfactions.misc.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.centile.hcfactions.util.player.PlayerUtility;
import us.centile.permissions.profile.Profile;

import java.util.StringJoiner;


public class HighRollerBroadcast extends BukkitRunnable {

    @Override
    public void run() {
        if (PlayerUtility.getOnlinePlayers() .size() < 1) return;

        StringJoiner onlineJoiner = new StringJoiner(ChatColor.GOLD + ", ");

        int count = 0;

        for (Player player : PlayerUtility.getOnlinePlayers() ) {

            String rankName = Profile.getByUuid(player.getUniqueId()) == null ? "DEFAULT" : Profile.getByUuid(player.getUniqueId()).getActiveGrant().getRank().getData().getName().toUpperCase();

            if(rankName.equalsIgnoreCase("HIGHROLLER")) {
                onlineJoiner.add(ChatColor.DARK_PURPLE + player.getName());
                count++;
            }
        }

        if (count == 0) return;

        Bukkit.broadcastMessage(ChatColor.GOLD + "MineAU HighRollers: " + onlineJoiner.toString());
    }

}
