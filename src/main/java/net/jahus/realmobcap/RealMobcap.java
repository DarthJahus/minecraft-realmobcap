package net.jahus.realmobcap;

import com.mojang.logging.LogUtils;
import net.jahus.realmobcap.command.MobcapCommand;
import net.jahus.realmobcap.config.CategoryPatcher;
import net.jahus.realmobcap.config.ModConfig;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RealMobcap.MOD_ID)
public class RealMobcap {
    public static final String MOD_ID = "realmobcap";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealMobcap(FMLJavaModLoadingContext context) {
        ModConfig.load();
        CategoryPatcher.apply();
        RegisterCommandsEvent.BUS.addListener(RealMobcap::onRegisterCommands);
        LOGGER.info("Real Mobcap loaded (enabled={})", ModConfig.isEnabled());
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        MobcapCommand.register(event.getDispatcher());
    }
}
