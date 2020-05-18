package us.centile.hcfactions.potionlimiter;

import us.centile.hcfactions.FactionsPlugin;

public class PotionLimiter {

    private static final PotionLimiter instance = new PotionLimiter();
    private static FactionsPlugin main = FactionsPlugin.getInstance();

    public boolean isBlocked(int data) {
        return main.getMainConfig().getStringList("BLOCKED_POTIONS").contains(data + "");
    }

    public static PotionLimiter getInstance() {
        return instance;
    }

}
