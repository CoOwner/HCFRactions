package us.centile.hcfactions;

import com.bizarrealex.aether.Aether;
import com.bizarrealex.aether.AetherOptions;
import com.comphenix.protocol.ProtocolLibrary;
import lombok.Setter;
import us.centile.hcfactions.blockoperation.BlockOperationModifier;
import us.centile.hcfactions.combatlogger.CombatLogger;
import us.centile.hcfactions.combatlogger.CombatLoggerListeners;
import us.centile.hcfactions.combatlogger.commands.CombatLoggerCommand;
import us.centile.hcfactions.crate.Crate;
import us.centile.hcfactions.crate.command.CrateCommand;
import us.centile.hcfactions.crowbar.CrowbarListeners;
import us.centile.hcfactions.deathlookup.DeathLookupCommand;
import us.centile.hcfactions.deathlookup.DeathLookupListeners;
import us.centile.hcfactions.deathsign.DeathSignListeners;
import us.centile.hcfactions.elevator.ElevatorListeners;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.glowstone.command.GlowstoneForceCommand;
import us.centile.hcfactions.event.glowstone.procedure.command.GlowstoneRemoveCommand;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.event.koth.KothEventListeners;
import us.centile.hcfactions.event.koth.command.KothCommand;
import us.centile.hcfactions.event.koth.command.KothScheduleCommand;
import us.centile.hcfactions.event.koth.command.KothStopCommand;
import us.centile.hcfactions.event.koth.procedure.KothCreateProcedureListeners;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.claims.ClaimListeners;
import us.centile.hcfactions.factions.claims.ClaimPillar;
import us.centile.hcfactions.factions.commands.*;
import us.centile.hcfactions.factions.commands.admin.*;
import us.centile.hcfactions.factions.commands.leader.*;
import us.centile.hcfactions.factions.commands.officer.*;
import us.centile.hcfactions.factions.commands.system.*;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.files.ConfigFile;
import us.centile.hcfactions.inventory.command.CloneInventoryCommand;
import us.centile.hcfactions.inventory.command.GiveInventoryCommand;
import us.centile.hcfactions.itemdye.ItemDye;
import us.centile.hcfactions.itemdye.ItemDyeListeners;
import us.centile.hcfactions.kits.Kit;
import us.centile.hcfactions.kits.KitListeners;
import us.centile.hcfactions.kits.command.KitCommand;
import us.centile.hcfactions.misc.commands.*;
import us.centile.hcfactions.misc.commands.economy.PayCommand;
import us.centile.hcfactions.misc.commands.economy.SetBalanceCommand;
import us.centile.hcfactions.misc.listeners.*;
import us.centile.hcfactions.misc.runnable.HighRollerBroadcast;
import us.centile.hcfactions.mobstack.MobStack;
import us.centile.hcfactions.mobstack.MobStackListeners;
import us.centile.hcfactions.mode.ModeListeners;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.profile.ProfileAutoSaver;
import us.centile.hcfactions.profile.ProfileListeners;
import us.centile.hcfactions.profile.fight.command.KillStreakCommand;
import us.centile.hcfactions.profile.kit.ProfileKitActionListeners;
import us.centile.hcfactions.profile.kit.command.ProfileKitCommand;
import us.centile.hcfactions.profile.options.command.ProfileOptionsCommand;
import us.centile.hcfactions.profile.ore.ProfileOreCommand;
import us.centile.hcfactions.profile.protection.ProfileProtection;
import us.centile.hcfactions.statracker.StatTrackerListeners;
import us.centile.hcfactions.tab.TabAdapter;
import us.centile.hcfactions.util.command.CommandFramework;
import us.centile.hcfactions.util.database.FactionsDatabase;
import us.centile.hcfactions.event.koth.procedure.command.KothRemoveCommand;
import us.centile.hcfactions.blockoperation.BlockOperationModifierListeners;
import us.centile.hcfactions.claimwall.ClaimWallListeners;
import us.centile.hcfactions.crate.CrateListeners;
import us.centile.hcfactions.economysign.EconomySignListeners;
import us.centile.hcfactions.enchantmentlimiter.EnchantmentLimiterListeners;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.glowstone.GlowstoneEvent;
import us.centile.hcfactions.event.glowstone.GlowstoneEventListeners;
import us.centile.hcfactions.event.glowstone.procedure.GlowstoneCreateProcedureListeners;
import us.centile.hcfactions.event.glowstone.procedure.command.GlowstoneProcedureCommand;
import us.centile.hcfactions.event.koth.command.KothStartCommand;
import us.centile.hcfactions.event.koth.procedure.command.KothCreateProcedureCommand;
import us.centile.hcfactions.inventory.command.LastInventoryCommand;
import us.centile.hcfactions.misc.commands.economy.AddBalanceCommand;
import us.centile.hcfactions.misc.commands.economy.BalanceCommand;
import us.centile.hcfactions.mode.Mode;
import us.centile.hcfactions.mode.command.ModeCommand;
import us.centile.hcfactions.potionlimiter.PotionLimiterListeners;
import us.centile.hcfactions.profile.cooldown.ProfileCooldownListeners;
import us.centile.hcfactions.profile.protection.command.ProfileProtectionCommand;
import us.centile.hcfactions.subclaim.SubclaimListeners;
import us.centile.hcfactions.tab.TabHandler;
import us.centile.hcfactions.tab.layout.HCFTabLayout;
import us.centile.hcfactions.util.FactionsBoardAdapter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.centile.hcfactions.util.player.PlayerUtility;
import us.centile.hcfactions.util.player.SimpleOfflinePlayer;

import java.io.IOException;

@Getter
public class FactionsPlugin extends JavaPlugin {

    @Getter private static FactionsPlugin instance;

    private CommandFramework framework;
    private FactionsDatabase factionsDatabase;
    private ConfigFile mainConfig, scoreboardConfig, languageConfig, kothScheduleConfig;
    @Setter private boolean loaded;
    @Setter private boolean kitmapMode;

    public void onEnable() {
        instance = this;

        this.mainConfig = new ConfigFile(this, "config");
        this.languageConfig = new ConfigFile(this, "lang");
        this.scoreboardConfig = new ConfigFile(this, "scoreboard");
        this.kothScheduleConfig = new ConfigFile(this, "koth-schedule");
        this.factionsDatabase = new FactionsDatabase(this);
        this.kitmapMode = this.mainConfig.getBoolean("KITMAP_MODE");

        new Aether(this, new FactionsBoardAdapter(this), new AetherOptions().hook(true));

        for (Player player : PlayerUtility.getOnlinePlayers() ) {
            new Profile(player.getUniqueId());
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == CombatLogger.ENTITY_TYPE) {
                    if (entity instanceof LivingEntity) {
                        if (((LivingEntity) entity).getCustomName() != null) {
                            entity.remove();
                        }
                    }
                }
            }
        }

        this.framework = new CommandFramework(this);

        SimpleOfflinePlayer.load(this);
        Faction.load();
        Mode.load();
        MobStack.stack();
        KothEvent.load();
        GlowstoneEvent.load();
        Crate.load();
        Kit.load();
        BlockOperationModifier.run();
        ProfileProtection.run(this);

        registerRecipes();
        registerListeners();
        registerCommands();

        TabHandler.init(this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new TabAdapter(this));
        TabHandler.setLayoutProvider(new HCFTabLayout());

        PlayerFaction.runTasks();

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new HighRollerBroadcast(), 6000L, 6000L);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ProfileAutoSaver(this), 5000L, 5000L);
    }

    public void onDisable() {
        Faction.save();

        for (Player player : PlayerUtility.getOnlinePlayers() ) {
            Profile profile = Profile.getByPlayer(player);

            if (profile.getClaimProfile() != null) {
                profile.getClaimProfile().removePillars();
            }

            for (ClaimPillar claimPillar : profile.getMapPillars()) {
                claimPillar.remove();
            }

            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        try {
            SimpleOfflinePlayer.save(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for (Profile profile : Profile.getProfiles()) {
            profile.save();
        }

        for (Mode mode : Mode.getModes()) {
            mode.save();
        }

        for (CombatLogger logger : CombatLogger.getLoggers()) {
            logger.getEntity().remove();
        }

        for (Event event : EventManager.getInstance().getEvents()) {
            if (event instanceof KothEvent) {
                ((KothEvent) event).save();
            }
            else if (event instanceof GlowstoneEvent) {
                ((GlowstoneEvent) event).save();
            }
        }

        for (Crate crate : Crate.getCrates()) {
            crate.save();
        }

        for (Kit kit : Kit.getKits()) {
            kit.save();
        }

        MobStack.unStack();

        factionsDatabase.getClient().close();
    }

    private void registerRecipes() {
        for (Material material : Material.values()) {
            if (material.name().contains("CHESTPLATE") || material.name().contains("SWORD") || material.name().contains("LEGGINGS") || material.name().contains("BOOTS") || material.name().contains("HELMET") || material.name().contains("AXE") || material.name().contains("SPADE")) {
                for (ItemDye dye : ItemDye.values()) {
                    Bukkit.addRecipe(ItemDye.getRecipe(material, dye));
                }
            }
        }

        Bukkit.addRecipe(new ShapelessRecipe(new ItemStack(Material.EXP_BOTTLE)).addIngredient(1, Material.GLASS_BOTTLE));
    }

    private void registerCommands() {
        new ProfileProtectionCommand();
        new ProfileOreCommand();
        new CloneInventoryCommand();
        new LastInventoryCommand();
        new GiveInventoryCommand();
        new DeathLookupCommand();
        new ProfileKitCommand();
        new KillStreakCommand();
        new CombatLoggerCommand();

        new ModeCommand();

        new KothCommand();
        new KothScheduleCommand();
        new KothCreateProcedureCommand();
        new KothRemoveCommand();
        new KothStartCommand();
        new KothStopCommand();

        new GlowstoneProcedureCommand();
        new GlowstoneRemoveCommand();
        new GlowstoneForceCommand();

        new BalanceCommand();
        new ReclaimCommand();
        new ReclaimRemoveCommand();
        new StackCommand();
        new EnchantCommand();
        new CobbleCommand();
        new PayCommand();
        new SetBalanceCommand();
        new AddBalanceCommand();
        new HelpCommand();
        new SpawnCommand();
        new ProfileOptionsCommand();
        new PingCommand();
        new CrateCommand();
        new KitCommand();
        new MapKitCommand();
        new RenameCommand();
        new PlayTimeCommand();
        new TellLocationCommand();
        new FocusCommand();
        new MedicReviveCommand();

        new FactionHelpCommand();
        new FactionDisbandCommand();
        new FactionCreateCommand();
        new FactionDisbandAllCommand();
        new FactionInviteCommand();
        new FactionJoinCommand();
        new FactionRenameCommand();
        new FactionPromoteCommand();
        new FactionDemoteCommand();
        new FactionLeaderCommand();
        new FactionUninviteCommand();
        new FactionChatCommand();
        new FactionSetHomeCommand();
        new FactionMessageCommand();
        new FactionAnnouncementCommand();
        new FactionLeaveCommand();
        new FactionShowCommand();
        new FactionKickCommand();
        new FactionInvitesCommand();
        new FactionDepositCommand();
        new FactionWithdrawCommand();
        new FactionClaimCommand();
        new FactionMapCommand();
        new FactionUnclaimCommand();
        new FactionListCommand();
        new FactionHomeCommand();
        new FactionStuckCommand();
        new FactionCreateSystemCommand();
        new FactionToggleDeathbanCommand();
        new FactionColorCommand();
        new FactionFreezeCommand();
        new FactionThawCommand();
        new FactionSetDtrCommand();
        new FactionAdminCommand();

        if (this.mainConfig.getBoolean("FACTION_GENERAL.ALLIES.ENABLED")) {
            new FactionAllyCommand();
        }

        if (mainConfig.getBoolean("FACTION_GENERAL.ALLIES.ENABLED")) {
            new FactionEnemyCommand();
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ProfileListeners(this), this);
        pluginManager.registerEvents(new MobStackListeners(), this);
        pluginManager.registerEvents(new CrowbarListeners(), this);
        pluginManager.registerEvents(new EconomySignListeners(), this);
        pluginManager.registerEvents(new DeathSignListeners(), this);
        pluginManager.registerEvents(new StatTrackerListeners(), this);
        pluginManager.registerEvents(new ProfileCooldownListeners(), this);
        pluginManager.registerEvents(new ProfileKitActionListeners(), this);
        pluginManager.registerEvents(new ClaimWallListeners(this), this);
        pluginManager.registerEvents(new EnchantmentLimiterListeners(), this);
        pluginManager.registerEvents(new PotionLimiterListeners(), this);
        pluginManager.registerEvents(new DeathLookupListeners(), this);
        pluginManager.registerEvents(new CombatLoggerListeners(this), this);
        pluginManager.registerEvents(new BlockOperationModifierListeners(), this);
        pluginManager.registerEvents(new KothCreateProcedureListeners(), this);
        pluginManager.registerEvents(new GlowstoneCreateProcedureListeners(), this);
        pluginManager.registerEvents(new KothEventListeners(), this);
        pluginManager.registerEvents(new GlowstoneEventListeners(), this);
        pluginManager.registerEvents(new ElevatorListeners(), this);
        pluginManager.registerEvents(new SubclaimListeners(), this);
        pluginManager.registerEvents(new CrateListeners(), this);
        pluginManager.registerEvents(new ItemDyeListeners(), this);
        pluginManager.registerEvents(new GlitchListeners(), this);
        pluginManager.registerEvents(new ModeListeners(), this);
        pluginManager.registerEvents(new ScoreboardListeners(), this);
        pluginManager.registerEvents(new ChatListeners(), this);
        pluginManager.registerEvents(new ClaimListeners(), this);
        pluginManager.registerEvents(new KitListeners(), this);
        pluginManager.registerEvents(new BorderListener(), this);
    }

}
