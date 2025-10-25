package com.mars.laserbridges.platform;

import com.mars.laserbridges.platform.services.IPlatformHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import static com.mars.laserbridges.LaserBridges.OFF;
import static com.mars.laserbridges.LaserBridges.ON;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public SoundEvent getSoundEvent(int power) {
        if (power == 0)
            return OFF.get();
        return ON.get();
    }

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
