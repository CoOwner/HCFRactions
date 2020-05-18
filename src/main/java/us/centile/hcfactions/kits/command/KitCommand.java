package us.centile.hcfactions.kits.command;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.kits.Kit;
import us.centile.hcfactions.kits.command.subcommand.*;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.DateUtil;
import us.centile.hcfactions.util.ItemBuilder;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

import java.text.DecimalFormat;
import java.util.Arrays;

public class KitCommand extends PluginCommand {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    public KitCommand() {
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitListCommand();
        new KitUpdateCommand();
    }

    @Command(name = "kit", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(FactionsPlugin.getInstance().isKitmapMode()) {
            player.sendMessage(ChatColor.RED + "This command is not available for KitMap.");
            return;
        }

        if(args.length == 0) {
            openKitsInventory(player);
        } else if(args.length == 1) {

            Kit kit = Kit.getByName(StringUtils.join(args));

            if(kit == null) {
                player.sendMessage(net.md_5.bungee.api.ChatColor.RED + "A kit named '" + args[0] + "' does not exist.");
                return;
            }

            if (!player.hasPermission("class.use." + kit.getName().toLowerCase())) {
                player.sendMessage(ChatColor.YELLOW + "Purchase HCF Kits @ store.mineau.cc");
                return;
            }

            Profile profile = Profile.getByUuid(player.getUniqueId());

            if (profile == null) {
                return;
            }

            if (KitCommand.isCooldownActive(profile, kit) && !player.isOp()) {
                player.sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("KIT_COMMAND.DELAY").replace("%TIME%", KitCommand.getTimeLeft(profile, kit)));
                return;
            }


            kit.loadKit(player);
            profile.getKitDelay().put(kit, System.currentTimeMillis() + 172800 * 1000L);

            player.sendMessage(FactionsPlugin.getInstance().getLanguageConfig().getString("KIT_COMMAND.SUCCESS").replace("%KIT%", kit.getName()));
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /kit <name>");
        }
    }

    public static void openKitsInventory(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 27, "HCF Kits");

        ItemStack spacer = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build();

        int[] slots = new int[] {0,1,2,3,4,5,6,7,8,9,11,13,15,17,18,19,20,21,22,23,24,25,26};

        for(int i : slots) {
            inventory.setItem(i, spacer);
        }

        int count = 1;

        for (Kit kit : Kit.getKits()) {
            String cooldown = Profile.getByPlayer(player) != null && isCooldownActive(Profile.getByPlayer(player), kit) ? ChatColor.DARK_GREEN + "Cooldown: " + ChatColor.GRAY + getTimeLeft(Profile.getByPlayer(player), kit) : ChatColor.DARK_GREEN + "Cooldown: " + ChatColor.GRAY + "None";
            inventory.setItem(inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE).enchantment(Enchantment.DURABILITY).durability(count).name(ChatColor.RED + "Kit: " + ChatColor.GRAY + kit.getName()).lore(cooldown).lore(ChatColor.GRAY + ChatColor.BOLD.toString() + "* " + ChatColor.GOLD + "Right Click to preview this Kit").lore(ChatColor.GRAY + ChatColor.BOLD.toString() + "* " + ChatColor.GOLD + "Left Click to use this Kit").build());
            count++;
        }

        player.openInventory(inventory);
    }

    public static void openPreviewInventory(Player player, Kit kit) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, kit.getName() + " Preview");

        int count = 0;

        for(ItemStack itemStack : kit.getItems()) {
            inventory.setItem(count, itemStack);
            count++;
        }

        inventory.setItem(53, new ItemBuilder(Material.ARROW).name(ChatColor.RED + "⏎ Go Back").build());

        player.openInventory(inventory);
    }

    public static void openUpdateInventory(Player player, Kit kit) {

        Inventory inventory = Bukkit.createInventory(player, 9 * 6, ChatColor.RED + "Kits - 1/1");

        inventory.setItem(0, new ItemBuilder(Material.CARPET).durability(7).name(ChatColor.RED.toString()).build());
        inventory.setItem(8, new ItemBuilder(Material.CARPET).durability(7).name(ChatColor.RED.toString()).build());
        inventory.setItem(4, new ItemBuilder(Material.PAPER).name(ChatColor.RED + "Page 1/1").lore(Arrays.asList(ChatColor.YELLOW + "Kit: " + ChatColor.RED + kit.getName())).build());

        for (int i = 0; i < kit.getItems().size(); i++) {
            inventory.setItem(9 + i, kit.getItems().get(i));
        }

        player.openInventory(inventory);
    }

    public static String getTimeLeft(Profile profile, Kit kit) {

        if (!profile.getKitDelay().containsKey(kit)) {
            return "None";
        }

        long delay = profile.getKitDelay().get(kit);

        return DateUtil.convertTime((delay - System.currentTimeMillis()) / 1000L);
    }

    public static boolean isCooldownActive(Profile profile, Kit kit) {

        if(!profile.getKitDelay().containsKey(kit)) {
            return false;
        }

        long delay = profile.getKitDelay().get(kit);

        return delay > System.currentTimeMillis();
    }
}
