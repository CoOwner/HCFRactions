package us.centile.hcfactions.tab;

import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import java.util.*;
import net.minecraft.util.com.mojang.authlib.*;
import org.spigotmc.*;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.*;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.util.player.PlayerUtility;

import java.io.*;

final class Tab
{
    private FactionsPlugin plugin;
    private Player player;
    private Map<String, String> previousNames;
    private Map<String, Integer> previousPings;
    private String lastHeader;
    private String lastFooter;
    private Set<String> createdTeams;
    private TabLayout initialLayout;
    private boolean initiated;
    private final StringBuilder removeColorCodesBuilder;
    
    public Tab(FactionsPlugin plugin, final Player player) {
        this.previousNames = new HashMap<String, String>();
        this.previousPings = new HashMap<String, Integer>();
        this.lastHeader = "{\"translate\":\"\"}";
        this.lastFooter = "{\"translate\":\"\"}";
        this.createdTeams = new HashSet<String>();
        this.initiated = false;
        this.removeColorCodesBuilder = new StringBuilder();
        this.player = player;
        this.plugin = plugin;
    }
    
    private void createAndAddMember(final String name, final String member) {
        final TeamPacket scoreboardTeamAdd = new TeamPacket("$" + name, "", "", Collections.singletonList(member), 0);
        scoreboardTeamAdd.sendToPlayer(this.player);
    }
    
    private void init() {
        if (!this.initiated) {
            final TabLayout initialLayout = TabLayout.createEmpty(this.player);
            if (!initialLayout.is18()) {
                for (final Player n : PlayerUtility.getOnlinePlayers()) {
                    this.updateTabList(n.getName(), 0, ((CraftPlayer)n).getProfile(), 4);
                }
            }
            for (final String s : initialLayout.getTabNames()) {
                this.updateTabList(s, 0, 0);
                final String teamName = s.replaceAll("§", "");
                if (!this.createdTeams.contains(teamName)) {
                    this.createAndAddMember(teamName, s);
                    this.createdTeams.add(teamName);
                }
            }
            this.initialLayout = initialLayout;
            this.initiated = true;
        }
    }
    
    private void updateScore(final String score, final String prefix, final String suffix) {
        final TeamPacket scoreboardTeamModify = new TeamPacket(score, prefix, suffix, null, 2);
        scoreboardTeamModify.sendToPlayer(this.player);
    }
    
    private void updateTabList(final String name, final int ping, final int action) {
        this.updateTabList(name, ping, TabUtils.getOrCreateProfile(name), action);
    }
    
    private void updateTabList(final String name, final int ping, final GameProfile profile, final int action) {
        final PlayerInfoPacketMod playerInfoPacketMod = new PlayerInfoPacketMod("$" + name, ping, profile, action);
        playerInfoPacketMod.sendToPlayer(this.player);
    }
    
    private String[] splitString(final String line) {
        if (line.length() <= 16) {
            return new String[] { line, "" };
        }
        return new String[] { line.substring(0, 16), line.substring(16, line.length()) };
    }
    
    protected void update() {
        if (TabHandler.getLayoutProvider() != null) {
            final TabLayout tabLayout = TabHandler.getLayoutProvider().provide(this.player, this.plugin);
            if (tabLayout == null) {
                if (this.initiated) {
                    this.reset();
                }
                return;
            }
            this.init();
            for (int y = 0; y < TabLayout.HEIGHT; ++y) {
                for (int x = 0; x < TabLayout.WIDTH; ++x) {
                    final String entry = tabLayout.getStringAt(x, y);
                    final int ping = tabLayout.getPingAt(x, y);
                    final String entryName = this.initialLayout.getStringAt(x, y);
                    this.removeColorCodesBuilder.setLength(0);
                    this.removeColorCodesBuilder.append(entryName);
                    int j = 0;
                    for (int i = 0; i < this.removeColorCodesBuilder.length(); ++i) {
                        if ('§' != this.removeColorCodesBuilder.charAt(i)) {
                            this.removeColorCodesBuilder.setCharAt(j++, this.removeColorCodesBuilder.charAt(i));
                        }
                    }
                    this.removeColorCodesBuilder.delete(j, this.removeColorCodesBuilder.length());
                    final String teamName = "$" + this.removeColorCodesBuilder.toString();
                    if (this.previousNames.containsKey(entryName)) {
                        if (!this.previousNames.get(entryName).equals(entry)) {
                            this.update(entryName, teamName, entry, ping);
                        }
                        else if (this.previousPings.containsKey(entryName) && this.previousPings.get(entryName) != ping) {
                            this.updateTabList(entryName, ping, 2);
                            this.previousPings.put(entryName, ping);
                        }
                    }
                    else {
                        this.update(entryName, teamName, entry, ping);
                    }
                }
            }
            boolean sendHeader = false;
            boolean sendFooter = false;
            final String header = tabLayout.getHeader();
            final String footer = tabLayout.getFooter();
            if (!header.equals(this.lastHeader)) {
                sendHeader = true;
            }
            if (!footer.equals(this.lastFooter)) {
                sendFooter = true;
            }
            if (tabLayout.is18() && (sendHeader || sendFooter)) {
                final ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(ChatSerializer.a(header), ChatSerializer.a(footer));
                ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)packet);
                this.lastHeader = header;
                this.lastFooter = footer;
            }
        }
    }
    
    private void reset() {
        this.initiated = false;
        for (final String s : this.initialLayout.getTabNames()) {
            this.updateTabList(s, 0, 4);
        }
        EntityPlayer ePlayer = ((CraftPlayer)this.player).getHandle();
        this.updateTabList(this.player.getName(), ePlayer.ping, ePlayer.getProfile(), 0);
        int count = 1;
        for (final Player player : PlayerUtility.getOnlinePlayers()) {
            if (this.player == player) {
                continue;
            }
            if (count > this.initialLayout.getTabNames().length - 1) {
                break;
            }
            ePlayer = ((CraftPlayer)player).getHandle();
            this.updateTabList(player.getName(), ePlayer.ping, ePlayer.getProfile(), 0);
            ++count;
        }
    }
    
    private void update(final String entryName, final String teamName, final String entry, final int ping) {
        final String[] entryStrings = this.splitString(entry);
        String prefix = entryStrings[0];
        String suffix = entryStrings[1];
        if (!suffix.isEmpty()) {
            if (prefix.charAt(prefix.length() - 1) == '§') {
                prefix = prefix.substring(0, prefix.length() - 1);
                suffix = '§' + suffix;
            }
            String suffixPrefix = ChatColor.RESET.toString();
            if (!ChatColor.getLastColors(prefix).isEmpty()) {
                suffixPrefix = ChatColor.getLastColors(prefix);
            }
            if (suffix.length() <= 14) {
                suffix = suffixPrefix + suffix;
            }
            else {
                suffix = suffixPrefix + suffix.substring(0, 14);
            }
        }
        this.updateScore(teamName, prefix, suffix);
        this.updateTabList(entryName, ping, 2);
        this.previousNames.put(entryName, entry);
        this.previousPings.put(entryName, ping);
    }
}
