package us.centile.hcfactions.tab;

import org.bukkit.entity.*;
import us.centile.hcfactions.FactionsPlugin;

public interface LayoutProvider
{
    TabLayout provide(final Player p0, FactionsPlugin plugin);
}
