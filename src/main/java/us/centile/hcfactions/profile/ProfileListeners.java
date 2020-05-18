package us.centile.hcfactions.profile;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.claims.Claim;
import us.centile.hcfactions.factions.events.player.PlayerCancelFactionTeleportEvent;
import us.centile.hcfactions.factions.events.player.PlayerDisbandFactionEvent;
import us.centile.hcfactions.factions.events.player.PlayerLeaveFactionEvent;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.factions.type.SystemFaction;
import us.centile.hcfactions.profile.deathban.ProfileDeathban;
import us.centile.hcfactions.profile.deathban.ProfileDeathbanListeners;
import us.centile.hcfactions.profile.deathmessage.ProfileDeathMessageListeners;
import us.centile.hcfactions.profile.kit.ProfileKitListeners;
import us.centile.hcfactions.profile.ore.ProfileOreListeners;
import us.centile.hcfactions.profile.achievement.ProfileAchievementListeners;
import us.centile.hcfactions.profile.fight.ProfileFightListeners;
import us.centile.hcfactions.profile.kit.ability.ProfileKitAbilityListeners;
import us.centile.hcfactions.profile.options.ProfileOptionsListeners;
import us.centile.hcfactions.profile.protection.ProfileProtectionListeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import us.centile.hcfactions.profile.protection.life.ProfileProtectionLifeType;
import us.centile.hcfactions.profile.teleport.ProfileTeleportType;
import us.centile.hcfactions.util.ItemBuilder;
import us.centile.hcfactions.util.LocationSerialization;
import us.centile.hcfactions.util.player.SimpleOfflinePlayer;

import java.math.BigDecimal;
import java.util.*;

public class ProfileListeners implements Listener {

    private static FactionsPlugin main = FactionsPlugin.getInstance();

    private HashMap<UUID, HashMap<ProfileProtectionLifeType, Long>> livesCooldown;

    public ProfileListeners(FactionsPlugin main) {

        this.livesCooldown = new HashMap<>();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ProfileDeathbanListeners(), main);
        pluginManager.registerEvents(new ProfileProtectionListeners(), main);
        pluginManager.registerEvents(new ProfileAchievementListeners(), main);
        pluginManager.registerEvents(new ProfileKitAbilityListeners(), main);
        pluginManager.registerEvents(new ProfileKitListeners(), main);
        pluginManager.registerEvents(new ProfileOreListeners(), main);
        pluginManager.registerEvents(new ProfileOptionsListeners(), main);
        pluginManager.registerEvents(new ProfileFightListeners(), main);
        pluginManager.registerEvents(new ProfileDeathMessageListeners(), main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            Profile profile = new Profile(event.getUniqueId());

            if (profile.getName() == null || !profile.getName().equals(event.getName())) {
                profile.setName(event.getName());
                profile.save();
            }

            if (!main.isKitmapMode()) {
                ProfileDeathban deathban = profile.getDeathban();

                if (profile.getDeathban() != null) {

                    if(this.livesCooldown.containsKey(profile.getUuid())) {

                        for(Map.Entry<ProfileProtectionLifeType, Long> entry : this.livesCooldown.get(profile.getUuid()).entrySet()) {

                            if(entry.getValue() > System.currentTimeMillis() && profile.getLives().get(entry.getKey()) > 0) {
                                profile.getLives().put(entry.getKey(), profile.getLives().get(entry.getKey()) - 1);
                                event.allow();
                                profile.setDeathban(null);
                                return;
                            }
                        }
                    }

                    if(profile.getLives().containsKey(ProfileProtectionLifeType.SOULBOUND) && profile.getLives().get(ProfileProtectionLifeType.SOULBOUND) > 0) {

                        HashMap<ProfileProtectionLifeType, Long> map = new HashMap<>(); map.put(ProfileProtectionLifeType.SOULBOUND, System.currentTimeMillis() + 30 * 1000L);
                        this.livesCooldown.put(profile.getUuid(), map);

                        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);

                        int amount = profile.getLives().get(ProfileProtectionLifeType.SOULBOUND);
                        event.setKickMessage(ProfileDeathban.LIFE_USE_MESSAGE.replace("%AMOUNT%",  + amount + "").replace("%LIFE_TYPE%", ProfileProtectionLifeType.SOULBOUND.name().replace("_", " ") + (amount > 1 ? " LIVES" : " LIFE")));
                        Profile.getProfilesMap().remove(profile.getUuid());
                    }

                    else if(profile.getLives().containsKey(ProfileProtectionLifeType.FRIEND) && profile.getLives().get(ProfileProtectionLifeType.FRIEND) > 0) {
                        HashMap<ProfileProtectionLifeType, Long> map = new HashMap<>(); map.put(ProfileProtectionLifeType.FRIEND, System.currentTimeMillis() + 30 * 1000L);
                        this.livesCooldown.put(profile.getUuid(), map);

                        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                        int amount = profile.getLives().get(ProfileProtectionLifeType.FRIEND);
                        event.setKickMessage(ProfileDeathban.LIFE_USE_MESSAGE.replace("%AMOUNT%",  + amount + "").replace("%LIFE_TYPE%", ProfileProtectionLifeType.FRIEND.name().replace("_", " ") + (amount > 1 ? " LIVES" : " LIFE")));
                        Profile.getProfilesMap().remove(profile.getUuid());
                    } else {
                        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                        event.setKickMessage(ProfileDeathban.KICK_MESSAGE.replace("%TIME%", deathban.getTimeLeft()));
                        Profile.getProfilesMap().remove(profile.getUuid());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile.sendPlayerTabUpdate(player);
        Profile profile = Profile.getByPlayer(player);
        profile.updateTab();
        profile.setLastInside(Claim.getProminentClaimAt(player.getLocation()));

        if(!player.hasPlayedBefore()) {
            profile.setBalance(100);

            if(!FactionsPlugin.getInstance().isKitmapMode()) {
                player.getInventory().addItem(new ItemBuilder(Material.FISHING_ROD).enchantment(Enchantment.DURABILITY, 3).name(ChatColor.GOLD.toString() + ChatColor.BOLD + "MineAU " + ChatColor.GRAY + "[" + ChatColor.RED + "Starter" + ChatColor.GRAY + "]").build());
                player.getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF).amount(16).build());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "crate givekey " + player.getName() + " Common 1");
            }
        }

        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(player.getUniqueId());

        if (!player.hasPlayedBefore()) {
            SystemFaction faction = SystemFaction.getByName("Spawn");

            if (faction != null && faction.getHome() != null) {
                player.teleport(LocationSerialization.deserializeLocation(faction.getHome()));
            }
        }

        if (offlinePlayer == null) {
            new SimpleOfflinePlayer(player);
        }
        else {
            if (!(offlinePlayer.getName().equals(player.getName()))) {
                offlinePlayer.setName(player.getName());
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Claim claim = Claim.getProminentClaimAt(event.getEntity().getLocation());

        if (claim != null && claim.getFaction() instanceof SystemFaction) {
            if (!((SystemFaction) claim.getFaction()).isDeathban()) {
                event.setFoodLevel(20);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END) {
            Faction faction = Faction.getByName("South Road");
            if(faction != null && faction instanceof SystemFaction) {

                if (faction.getHome() == null) {
                    event.getPlayer().sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("ERROR.HOME_NOT_SET"));
                    return;
                }

                Location location = LocationSerialization.deserializeLocation(faction.getHome());

                if(location != null) {
                    event.setTo(location);
                }
            }
        }

        else if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Profile profile = Profile.getByPlayer(event.getPlayer());
            profile.setLastInside(Claim.getProminentClaimAt(event.getTo()));
        }
    }

    @EventHandler
    public void onPortalEvent(PlayerPortalEvent event) {

        if(event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END && event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            Faction faction = Faction.getByName("South Road");
            if(faction != null && faction instanceof SystemFaction) {

                if (faction.getHome() == null) {
                    event.getPlayer().sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("ERROR.HOME_NOT_SET"));
                    return;
                }

                Location location = LocationSerialization.deserializeLocation(faction.getHome());

                if(location != null) {
                    event.useTravelAgent(false);
                    event.setTo(location);
                }
            }
        }

        else if(event.getFrom().getWorld().getEnvironment() == World.Environment.NETHER && event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            Faction faction = Faction.getByName("Spawn");
            if(faction != null && faction instanceof SystemFaction) {

                if (faction.getHome() == null) {
                    event.getPlayer().sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("ERROR.HOME_NOT_SET"));
                    return;
                }

                Location location = LocationSerialization.deserializeLocation(faction.getHome());

                if(location != null) {
                    event.useTravelAgent(false);
                    event.setTo(location);
                }
            }
        }

        else if(event.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

            Faction faction = Faction.getByName("End");
            if(faction != null && faction instanceof SystemFaction) {

                if (faction.getHome() == null) {
                    event.getPlayer().sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("ERROR.HOME_NOT_SET"));
                    return;
                }

                Location location = LocationSerialization.deserializeLocation(faction.getHome());

                if(location != null) {
                    event.setTo(location);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveFaction(PlayerLeaveFactionEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());

        if (profile.getClaimProfile() != null && profile.getClaimProfile().getFaction() == event.getFaction()) {
            profile.setClaimProfile(null);
        }
    }

    @EventHandler
    public void onFactionDisband(PlayerDisbandFactionEvent event) {
        for (Player player : event.getFaction().getOnlinePlayers()) {
            Profile profile = Profile.getByPlayer(player);

            if (profile.getClaimProfile() != null && profile.getClaimProfile().getFaction() == event.getFaction()) {
                profile.setClaimProfile(null);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            final Player player = event.getPlayer();
            Profile profile = Profile.getByPlayer(player);

            if (profile.getTeleportWarmup() != null) {
                if (profile.getTeleportWarmup().getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, null, ProfileTeleportType.HOME_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLanguageConfig().getString("ERROR.TELEPORT_CANCELLED"));
                }
                else {
                    if (player.getLocation().distance(profile.getTeleportWarmup().getEvent().getInitialLocation()) >= main.getMainConfig().getInt("TELEPORT_COUNTDOWN.STUCK.DISTANCE")) {
                        profile.getTeleportWarmup().getEvent().setCancelled(true);
                        Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, profile.getTeleportWarmup().getEvent().getFaction(), ProfileTeleportType.STUCK_TELEPORT));
                        profile.setTeleportWarmup(null);
                        player.sendMessage(main.getLanguageConfig().getString("ERROR.TELEPORT_CANCELLED"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            Profile profile = Profile.getByPlayer(player);

            if (profile.getTeleportWarmup() != null) {
                if (profile.getTeleportWarmup().getEvent().getTeleportType() == ProfileTeleportType.HOME_TELEPORT) {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, null, ProfileTeleportType.HOME_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLanguageConfig().getString("ERROR.TELEPORT_CANCELLED"));
                } else {
                    profile.getTeleportWarmup().getEvent().setCancelled(true);
                    Bukkit.getPluginManager().callEvent(new PlayerCancelFactionTeleportEvent(player, profile.getTeleportWarmup().getEvent().getFaction(), ProfileTeleportType.STUCK_TELEPORT));
                    profile.setTeleportWarmup(null);
                    player.sendMessage(main.getLanguageConfig().getString("ERROR.TELEPORT_CANCELLED"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        SystemFaction faction = SystemFaction.getByName("Spawn");

        if (faction != null && faction.getHome() != null) {
            event.setRespawnLocation(LocationSerialization.deserializeLocation(faction.getHome()));
        }

        profile.setLastInside(Claim.getProminentClaimAt(event.getRespawnLocation()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        player.getInventory().remove(Faction.getWand());

        Profile profile = Profile.getByPlayer(player);

        if(this.livesCooldown.containsKey(player.getUniqueId())) {
            this.livesCooldown.remove(player.getUniqueId());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Profile.getProfilesMap().remove(profile.getUuid());

                long playtime = System.currentTimeMillis() - (event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) * 50);
                profile.setPlayTime(playtime);

                if (profile.getTeleportWarmup() != null) {
                    profile.getTeleportWarmup().cancel();
                }

                profile.save();
            }
        }.runTaskAsynchronously(FactionsPlugin.getInstance());
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        event.getSource().remove(Faction.getWand());
        event.getDestination().remove(Faction.getWand());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            Player damager;

            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            }
            else if (e.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) e.getDamager();

                if (projectile.getShooter() instanceof Player) {
                    damager = (Player) projectile.getShooter();
                }
                else {
                    return;
                }
            }
            else {
                return;
            }

            if (damager == damaged) {
                return;
            }

            PlayerFaction damagedFaction = Profile.getByPlayer(damaged).getFaction();
            PlayerFaction damagerFaction = Profile.getByPlayer(damager).getFaction();

            if (damagedFaction == null || damagerFaction == null) {
                return;
            }

            if (damagedFaction == damagerFaction) {
                damager.sendMessage(main.getLanguageConfig().getString("FACTION_OTHER.CANNOT_DAMAGE_FRIENDLY").replace("%PLAYER%", damaged.getName()));
                e.setCancelled(true);
                return;
            }

            if (damagedFaction.getAllies().contains(damagerFaction) && !main.getMainConfig().getBoolean("ALLIES.DAMAGE_ALLIES")) {
                damager.sendMessage(main.getLanguageConfig().getString("FACTION_OTHER.CANNOT_DAMAGE_ALLY").replace("%PLAYER%", damaged.getName()));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(player.getUniqueId());

        if (offlinePlayer != null) {
            offlinePlayer.setDeaths(offlinePlayer.getDeaths() + 1);

            if(offlinePlayer.getKillStreak() > 0) {
                offlinePlayer.setKillStreak(0);
            }
        }

        if (player.getKiller() != null) {
            SimpleOfflinePlayer killerOfflinePlayer = SimpleOfflinePlayer.getByUuid(player.getKiller().getUniqueId());

            if (killerOfflinePlayer != null) {
                killerOfflinePlayer.setKills(killerOfflinePlayer.getKills() + 1);
                killerOfflinePlayer.setKillStreak( killerOfflinePlayer.getKillStreak() + 1 );
            }
        }

        PlayerFaction playerFaction = PlayerFaction.getByPlayer(player);

        if (playerFaction != null) {
            playerFaction.freeze(main.getMainConfig().getInt("FACTION_GENERAL.FREEZE_DURATION"));
            playerFaction.setDeathsTillRaidable(playerFaction.getDeathsTillRaidable().subtract(BigDecimal.ONE));
            playerFaction.sendMessage(main.getLanguageConfig().getString("ANNOUNCEMENTS.FACTION.PLAYER_DEATH").replace("%PLAYER%", player.getName()).replace("%DTR%", playerFaction.getDeathsTillRaidable() + "").replace("%MAX_DTR%", playerFaction.getMaxDeathsTillRaidable() + ""));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Claim claim = Claim.getProminentClaimAt(event.getEntity().getLocation());

            if (claim != null) {
                if (claim.getFaction() instanceof SystemFaction && !((SystemFaction) claim.getFaction()).isDeathban()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Profile profile = Profile.getByPlayer(event.getPlayer());
        Claim claim = Claim.getProminentClaimAt(event.getPlayer().getLocation());

        if(this.livesCooldown.containsKey(profile.getUuid())) {
            this.livesCooldown.remove(profile.getUuid());
        }

        if (claim != null) {
            profile.setLastInside(claim);
        }
    }

    @EventHandler
    public void onItemPickupEvent(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        Profile profile = Profile.getByPlayer(player);

        if(profile.isCobble() && event.getItem() != null && event.getItem().getItemStack().getType() == Material.COBBLESTONE) {
            event.setCancelled(true);
        }
    }

}
