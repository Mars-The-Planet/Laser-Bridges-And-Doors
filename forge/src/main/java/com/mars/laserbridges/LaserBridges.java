package com.mars.laserbridges;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
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
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public LaserBridges() {
        CommonClass.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS_REG.forEach(BLOCKS::register);
        BLOCK_ITEMS_REG.forEach(ITEMS::register);
        SOUND_EVENTS_REG.forEach(SOUND_EVENTS::register);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(this::clientSetup);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerBlockColorHandlers(ColorHandlerEvent.Block event) {
            BLOCKS_REG.forEach((s, blockSupplier) -> event.getBlockColors().register((state, level, pos, tintIndex) -> (((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[0] * 255)) << 16) | (((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[1] * 255)) << 8) | (int) ((DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[2] * 255)), blockSupplier.get()));
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        BLOCKS_REG.forEach((s, blockSupplier) -> ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), RenderType.translucent()));
    }
}
