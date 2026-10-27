package net.jahus.realmobcap;

import com.mojang.logging.LogUtils;
import net.jahus.realmobcap.command.MobcapCommand;
import net.jahus.realmobcap.config.CategoryPatcher;
import net.jahus.realmobcap.config.ModConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(RealMobcap.MOD_ID)
public class RealMobcap {
    public static final String MOD_ID = "realmobcap";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealMobcap(IEventBus modEventBus) {
        ModConfig.load();
        CategoryPatcher.apply();
        NeoForge.EVENT_BUS.addListener(RealMobcap::onRegisterCommands);
        LOGGER.info("Real Mobcap loaded (enabled={})", ModConfig.isEnabled());
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        MobcapCommand.register(event.getDispatcher());
    }
}
