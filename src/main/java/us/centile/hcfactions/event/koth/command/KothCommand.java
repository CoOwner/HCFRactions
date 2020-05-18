package us.centile.hcfactions.event.koth.command;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.centile.hcfactions.event.Event;
import us.centile.hcfactions.event.EventManager;
import us.centile.hcfactions.event.koth.KothEvent;
import us.centile.hcfactions.event.koth.procedure.KothCreateProcedure;
import us.centile.hcfactions.event.koth.procedure.KothCreateProcedureStage;
import us.centile.hcfactions.event.schedule.Schedule;
import us.centile.hcfactions.event.schedule.ScheduleHandler;
import us.centile.hcfactions.profile.Profile;
import us.centile.hcfactions.util.DateUtil;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class KothCommand extends PluginCommand {

    @Command(name = "koth", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();


        for (Event possibleEvent : EventManager.getInstance().getEvents()) {

            if (possibleEvent instanceof KothEvent && possibleEvent.isActive()) {
                player.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE  + possibleEvent.getName() + ChatColor.GOLD + " can be contested now.");
                return;
            }
        }

        Schedule kothEvent = ScheduleHandler.getNextEvent();
        FastDateFormat formatter = FastDateFormat.getInstance("EEEE, hh:mma", TimeZone.getTimeZone("Australia/Brisbane"), Locale.ENGLISH);

        player.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW  + kothEvent.getName() + ChatColor.GOLD + " can be captured at " + ChatColor.BLUE + kothEvent.getFormatDay() + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW  + "It is currently " + ChatColor.BLUE + formatter.format(System.currentTimeMillis()) + ChatColor.GOLD + ".");
        player.sendMessage(ChatColor.YELLOW + "Type '/koth schedule' to see more upcoming KOTHs.");

    }
}
