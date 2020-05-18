package us.centile.hcfactions.tab;

import net.minecraft.util.com.mojang.authlib.*;
import java.lang.reflect.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import net.minecraft.server.v1_7_R4.*;

public class PlayerInfoPacketMod
{
    private PacketPlayOutPlayerInfo packet;
    
    public PlayerInfoPacketMod(final String name, final int ping, final GameProfile profile, final int action) {
        this.packet = new PacketPlayOutPlayerInfo();
        this.setField("username", name);
        this.setField("ping", ping);
        this.setField("action", action);
        this.setField("player", profile);
    }
    
    public void setField(final String field, final Object value) {
        try {
            final Field fieldObject = this.packet.getClass().getDeclaredField(field);
            fieldObject.setAccessible(true);
            fieldObject.set(this.packet, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendToPlayer(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)this.packet);
    }
}
