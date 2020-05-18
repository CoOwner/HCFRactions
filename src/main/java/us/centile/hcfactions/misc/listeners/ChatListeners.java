package us.centile.hcfactions.misc.listeners;

import us.centile.hcfactions.files.ConfigFile;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;

public class ChatListeners implements Listener {

    private FactionsPlugin main = FactionsPlugin.getInstance();
    private ConfigFile mainConfig = main.getMainConfig();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if (mainConfig.getBoolean("FACTION_CHAT.ENABLED")) {

            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.getByPlayer(player);
            PlayerFaction playerFaction = profile.getFaction();

            event.setFormat("{FACTION}" + event.getFormat());

            String actualMessage = event.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage());
            Bukkit.getLogger().info(ChatColor.stripColor(actualMessage.replace("{FACTION}", "")));

            for (Player recipient : event.getRecipients()) {
                Profile recipientProfile = Profile.getByPlayer(recipient);

                if (playerFaction == null) {
                    recipient.sendMessage(event.getFormat().replace("{FACTION}", mainConfig.getString("FACTION_CHAT.NO_FACTION")).replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage()));
                    continue;
                }

                if (recipientProfile.getFaction() != null) {
                    if (recipientProfile.getFaction().getUuid().equals(playerFaction.getUuid())) {
                        recipient.sendMessage(event.getFormat().replace("{FACTION}", mainConfig.getString("FACTION_CHAT.FRIENDLY").replace("%TAG%", playerFaction.getName())).replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage()));
                        continue;
                    }

                    if (recipientProfile.getFaction().getAllies().contains(playerFaction)) {
                        recipient.sendMessage(event.getFormat().replace("{FACTION}", mainConfig.getString("FACTION_CHAT.ALLY").replace("%TAG%", playerFaction.getName())).replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage()));
                        continue;
                    }
                }

                recipient.sendMessage(event.getFormat().replace("{FACTION}", mainConfig.getString("FACTION_CHAT.ENEMY").replace("%TAG%", playerFaction.getName())).replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPublicPlayerChat(AsyncPlayerChatEvent event) {

        Iterator<Player> recipents = event.getRecipients().iterator();

        while (recipents.hasNext()) {

            Player player = recipents.next();

            Profile profile = Profile.getByPlayer(player);

            if(profile == null) {
                continue;
            }

            if(!profile.getOptions().isReceivePublicMessages()) {
                recipents.remove();
            }

        }

    }

}
