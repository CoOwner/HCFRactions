package us.centile.hcfactions.factions.commands.admin;

import org.bukkit.command.ConsoleCommandSender;
import us.centile.hcfactions.factions.commands.FactionCommand;
import us.centile.hcfactions.factions.Faction;
import us.centile.hcfactions.factions.type.PlayerFaction;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;

public class FactionSetDtrCommand extends FactionCommand {

    @Command(name = "f.setdtr", aliases = {"faction.setdtr", "factions.setdtr"}, permission = "hcf.setdtr", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (command.getArgs().length == 2) {
            String name = command.getArgs()[0];

            if(name.equalsIgnoreCase("*") && sender instanceof ConsoleCommandSender) {

                for(Faction faction : Faction.getFactions()) {

                    if(faction instanceof PlayerFaction) {
                        ((PlayerFaction) faction).setDeathsTillRaidable(BigDecimal.valueOf(Double.valueOf(command.getArgs()[1])));
                    }
                }

                sender.sendMessage(langConfig.getString("ADMIN.SET_DTR_ALL").replaceAll("%DTR%", command.getArgs()[1] + ""));
                return;
            }

            Faction faction = Faction.getByName(name);
            PlayerFaction playerFaction = null;

            if (faction instanceof PlayerFaction) {
                playerFaction = (PlayerFaction) faction;
            }

            if (faction == null || (!(faction instanceof PlayerFaction))) {
                playerFaction = PlayerFaction.getByPlayerName(name);

                if (playerFaction == null) {
                    sender.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                    return;
                }
            }

            playerFaction.setDeathsTillRaidable(BigDecimal.valueOf(Double.valueOf(command.getArgs()[1])));
            sender.sendMessage(langConfig.getString("ADMIN.SET_DTR").replaceAll("%FACTION%", playerFaction.getName()).replaceAll("%DTR%", playerFaction.getDeathsTillRaidable().doubleValue() + ""));
        } else {
            sender.sendMessage(langConfig.getString("TOO_FEW_ARGS.SET_DTR"));
        }
    }
}
