package com.mars.laserbridges.platform.services;

import net.minecraft.sounds.SoundEvent;

public interface IPlatformHelper {
    SoundEvent getSoundEvent(int power);
    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
