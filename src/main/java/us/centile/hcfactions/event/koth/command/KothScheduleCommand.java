package us.centile.hcfactions.event.koth.command;

import org.apache.commons.lang.time.FastDateFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.centile.hcfactions.event.schedule.Schedule;
import us.centile.hcfactions.event.schedule.ScheduleHandler;
import us.centile.hcfactions.util.PluginCommand;
import us.centile.hcfactions.util.command.Command;
import us.centile.hcfactions.util.command.CommandArgs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class KothScheduleCommand extends PluginCommand {

    @Command(name = "koth.schedule", inGameOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        FastDateFormat formatter = FastDateFormat.getInstance("EEEE, hh:mma", TimeZone.getTimeZone("Australia/Brisbane"), Locale.ENGLISH);

        for(Schedule schedule : ScheduleHandler.schedules) {
            player.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW  + schedule.getName() + ChatColor.GOLD + " can be captured at " + ChatColor.BLUE + schedule.getFormatDay() + ChatColor.GOLD + ".");
        }

        player.sendMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW  + "It is currently " + ChatColor.BLUE + formatter.format(System.currentTimeMillis()) + ChatColor.GOLD + ".");

    }
}
