package us.centile.hcfactions.util;

import us.centile.hcfactions.FactionsPlugin;
import us.centile.hcfactions.files.ConfigFile;
import us.centile.hcfactions.util.command.CommandArgs;

public abstract class PluginCommand {

    public FactionsPlugin main = FactionsPlugin.getInstance();
    public ConfigFile configFile = main.getMainConfig();
    public ConfigFile langFile = main.getLanguageConfig();
    public ConfigFile scoreboardFile = main.getScoreboardConfig();

    public PluginCommand() {
        main.getFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command);

}
