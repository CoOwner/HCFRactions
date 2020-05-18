package us.centile.hcfactions.factions.commands;

import us.centile.hcfactions.files.ConfigFile;
import us.centile.hcfactions.FactionsPlugin;

public class FactionCommand {

    public FactionsPlugin main = FactionsPlugin.getInstance();
    public ConfigFile langConfig = main.getLanguageConfig();
    public ConfigFile mainConfig = main.getMainConfig();

    public FactionCommand() {
        main.getFramework().registerCommands(this);
    }

}
