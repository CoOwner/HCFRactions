package us.centile.hcfactions.tab;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import net.minecraft.server.v1_7_R4.*;
import java.lang.reflect.*;

public final class TeamPacket
{
    private PacketPlayOutScoreboardTeam packet;
    
    public TeamPacket(final String name, final String prefix, final String suffix, final Collection players, final int paramInt) {
        this.packet = new PacketPlayOutScoreboardTeam();
        this.setField("a", name);
        this.setField("f", paramInt);
        if (paramInt == 0 || paramInt == 2) {
            this.setField("b", name);
            this.setField("c", prefix);
            this.setField("d", suffix);
            this.setField("g", 3);
        }
        if (paramInt == 0) {
            this.addAll(players);
        }
    }
    
    public TeamPacket(final String name, Collection players, final int paramInt) {
        this.packet = new PacketPlayOutScoreboardTeam();
        if (players == null) {
            players = new ArrayList();
        }
        this.setField("g", 3);
        this.setField("a", name);
        this.setField("f", paramInt);
        this.addAll(players);
    }
    
    public void sendToPlayer(final Player bukkitPlayer) {
        ((CraftPlayer)bukkitPlayer).getHandle().playerConnection.sendPacket((Packet)this.packet);
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
    
    private void addAll(final Collection col) {
        try {
            final Field fieldObject = this.packet.getClass().getDeclaredField("e");
            fieldObject.setAccessible(true);
            ((Collection)fieldObject.get(this.packet)).addAll(col);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
