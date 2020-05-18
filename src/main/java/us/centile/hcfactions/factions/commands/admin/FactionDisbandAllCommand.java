package us.centile.hcfactions.factions.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.claims.Claim;
import us.centile.hcfactions.factions.commands.FactionCommand;
import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import us.centile.hcfactions.factions.type.PlayerFaction;

import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class FactionDisbandAllCommand extends FactionCommand {

    @Command(name = "f.disbandall", permission = "hcf.disbandall", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (command.getArgs().length == 0 && sender instanceof ConsoleCommandSender) {
            for (Faction faction : Faction.getFactions()) {
                if (faction instanceof PlayerFaction) {
                    Set<Claim> claims = new HashSet<>(faction.getClaims());

                    for (Claim claim : claims) {
                        claim.remove();
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            main.getFactionsDatabase().getDatabase().getCollection("playerFactions").deleteOne(eq("uuid", faction.getUuid().toString()));
                        }
                    });

                    Faction.getFactions().remove(faction);
                }
            }

            sender.sendMessage(langConfig.getString("ADMIN.DISBAND_ALL"));
        }
    }
}
