package com.mars.laserbridges.platform;

import com.mars.laserbridges.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sounds.SoundEvent;

import static com.mars.laserbridges.LaserBridges.OFF;
import static com.mars.laserbridges.LaserBridges.ON;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public SoundEvent getSoundEvent(int power) {
        if (power == 0)
            return OFF;
        return ON;
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
