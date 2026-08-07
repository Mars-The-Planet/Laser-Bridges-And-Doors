package com.mars.laserbridges;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.ModRegistry.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

@Mod(Constants.MOD_ID)
public class LaserBridges {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public LaserBridges() {
        CommonClass.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS_REG.forEach(BLOCKS::register);
        BLOCK_ITEMS_REG.forEach(ITEMS::register);
        SOUND_EVENTS_REG.forEach(SOUND_EVENTS::register);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
            BLOCKS_REG.forEach((s, blockSupplier) -> event.register((state, level, pos, tintIndex) -> (((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[0] * 255)) << 16) | (((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[1] * 255)) << 8) | (int) ((DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[2] * 255)), blockSupplier.get()));
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
