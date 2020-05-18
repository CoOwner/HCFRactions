package us.centile.hcfactions.tab;

import net.minecraft.util.com.mojang.authlib.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import net.minecraft.util.com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;

public final class TabUtils
{
    private static Map<String, GameProfile> cache;
    
    public static boolean is18(final Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() > 20;
    }
    
    public static GameProfile getOrCreateProfile(final String name, final UUID id) {
        GameProfile player = TabUtils.cache.get(name);
        if (player == null) {
            player = new GameProfile(id, name);
            player.getProperties().putAll((Multimap)TabHandler.getDefaultPropertyMap());
            TabUtils.cache.put(name, player);
        }
        return player;
    }
    
    public static GameProfile getOrCreateProfile(final String name) {
        return getOrCreateProfile(name, new UUID(new Random().nextLong(), new Random().nextLong()));
    }
    
    static {
        TabUtils.cache = new ConcurrentHashMap<String, GameProfile>();
    }
}
