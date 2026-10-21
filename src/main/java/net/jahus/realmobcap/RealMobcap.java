package net.jahus.realmobcap;

import net.fabricmc.api.ModInitializer;
import net.jahus.realmobcap.config.CategoryPatcher;
import net.jahus.realmobcap.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealMobcap implements ModInitializer {
    public static final String MOD_ID = "realmobcap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfig.load();
        CategoryPatcher.apply();
        LOGGER.info("Real Mobcap loaded (enabled={})", ModConfig.isEnabled());
    }
}
