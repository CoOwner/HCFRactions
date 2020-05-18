package us.centile.hcfactions.tab;

import com.comphenix.protocol.*;
import org.bukkit.plugin.*;
import com.comphenix.protocol.events.*;
import us.centile.hcfactions.FactionsPlugin;

public class TabAdapter extends PacketAdapter
{
    public TabAdapter(FactionsPlugin plugin) {
        super(plugin, new PacketType[] { PacketType.Play.Server.PLAYER_INFO });
    }
    
    public void onPacketSending(final PacketEvent event) {
        if (TabHandler.getLayoutProvider() != null) {
            final PacketContainer packetContainer = event.getPacket();
            final String name = (String)packetContainer.getStrings().read(0);
            final boolean isOurs = ((String)packetContainer.getStrings().read(0)).startsWith("$");
            final int action = (int)packetContainer.getIntegers().read(1);
            if (!isOurs) {
                if (!TabUtils.is18(event.getPlayer())) {
                    if (action != 4) {
                        event.setCancelled(true);
                    }
                }
            }
            else {
                packetContainer.getStrings().write(0, name.replace("$", ""));
            }
        }
    }
}
