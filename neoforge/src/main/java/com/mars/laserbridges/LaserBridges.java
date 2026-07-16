package com.mars.laserbridges;

import com.mars.laserbridges.blocks.ILaserGeneratingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.ModRegistry.*;

@Mod(Constants.MOD_ID)
public class LaserBridges {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public LaserBridges(IEventBus eventBus) {
        CommonClass.init();

        BLOCKS_REG.forEach(BLOCKS::register);
        BLOCK_ITEMS_REG.forEach(ITEMS::register);
        SOUND_EVENTS_REG.forEach(SOUND_EVENTS::register);

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        SOUND_EVENTS.register(eventBus);

        eventBus.addListener(this::addCreative);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
            BLOCKS_REG.forEach((s, blockSupplier) -> event.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(ILaserGeneratingBlock.COLOR))).getTextureDiffuseColor(), blockSupplier.get()));
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS){
            event.accept(BRIDGE_ATTACHED_SOURCE_BLOCK_ITEM.get());
            event.accept(FENCE_ATTACHED_SOURCE_BLOCK_ITEM.get());
            event.accept(BRIDGE_SOURCE_BLOCK.get());
            event.accept(FENCE_SOURCE_BLOCK.get());
        }
    }
}
