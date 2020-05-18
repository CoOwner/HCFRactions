package us.centile.hcfactions.tab;

import java.util.concurrent.atomic.*;
import com.google.common.base.*;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import net.minecraft.util.com.mojang.authlib.properties.*;
import java.util.*;
import java.net.*;
import net.minecraft.util.com.mojang.authlib.yggdrasil.*;
import net.minecraft.util.com.mojang.authlib.*;
import net.minecraft.util.com.mojang.authlib.minecraft.*;
import us.centile.hcfactions.FactionsPlugin;

import java.util.concurrent.*;

public class TabHandler
{
    private static FactionsPlugin plugin;
    private static boolean initiated;
    private static final AtomicReference<Object> defaultPropertyMap;
    private static LayoutProvider layoutProvider;
    private static Map<String, Tab> tabs;
    
    public static void init(FactionsPlugin plugin) {
        TabHandler.plugin = plugin;
        Preconditions.checkState(!TabHandler.initiated);
        TabHandler.initiated = true;
        getDefaultPropertyMap();
        //Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new TabThread(), 1L, 1L);
        new TabThread().start();
        plugin.getServer().getPluginManager().registerEvents((Listener)new TabListener(plugin), plugin);
    }
    
    public static void setLayoutProvider(final LayoutProvider provider) {
        TabHandler.layoutProvider = provider;
    }
    
    protected static void addPlayer(final Player player) {
        TabHandler.tabs.put(player.getName(), new Tab(plugin, player));
    }
    
    protected static void updatePlayer(final Player player) {
        if (TabHandler.tabs.containsKey(player.getName())) {
            TabHandler.tabs.get(player.getName()).update();
        }
    }
    
    protected static void removePlayer(final Player player) {
        TabHandler.tabs.remove(player.getName());
    }
    
    private static PropertyMap fetchSkin() {
        final GameProfile profile = new GameProfile(UUID.fromString("6b22037d-c043-4271-94f2-adb00368bf16"), "bananasquad");
        final HttpAuthenticationService authenticationService = (HttpAuthenticationService)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final MinecraftSessionService sessionService = authenticationService.createMinecraftSessionService();
        final GameProfile profile2 = sessionService.fillProfileProperties(profile, true);
        return profile2.getProperties();
    }
    
    public static PropertyMap getDefaultPropertyMap() {
        Object value = TabHandler.defaultPropertyMap.get();
        if (value == null) {
            synchronized (TabHandler.defaultPropertyMap) {
                value = TabHandler.defaultPropertyMap.get();
                if (value == null) {
                    final PropertyMap actualValue = fetchSkin();
                    value = ((actualValue == null) ? TabHandler.defaultPropertyMap : actualValue);
                    TabHandler.defaultPropertyMap.set(value);
                }
            }
        }
        return (PropertyMap)((value == TabHandler.defaultPropertyMap) ? null : value);
    }
    
    public static LayoutProvider getLayoutProvider() {
        return TabHandler.layoutProvider;
    }
    
    static {
        TabHandler.initiated = false;
        defaultPropertyMap = new AtomicReference<Object>();
        TabHandler.tabs = new ConcurrentHashMap<String, Tab>();
    }
}
