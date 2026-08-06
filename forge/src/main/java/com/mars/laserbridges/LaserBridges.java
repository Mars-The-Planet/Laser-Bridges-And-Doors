package com.mars.laserbridges;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.ModRegistry.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

@Mod(Constants.MOD_ID)
public class LaserBridges {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public LaserBridges(FMLJavaModLoadingContext context) {
        CommonClass.init();

        var modEventBus = context.getModBusGroup();

        BLOCKS_REG.forEach(BLOCKS::register);
        BLOCK_ITEMS_REG.forEach(ITEMS::register);
        SOUND_EVENTS_REG.forEach(SOUND_EVENTS::register);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        RegisterColorHandlersEvent.Block.BUS.addListener(LaserBridges::registerBlockColorHandlers);
        BuildCreativeModeTabContentsEvent.BUS.addListener(LaserBridges::addCreative);
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        BLOCKS_REG.forEach((s, blockSupplier) -> event.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }
        }), blockSupplier.get()));
    }

    @SubscribeEvent
    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(BRIDGE_ATTACHED_SOURCE_BLOCK_ITEM.get());
            event.accept(FENCE_ATTACHED_SOURCE_BLOCK_ITEM.get());
            event.accept(BRIDGE_SOURCE_BLOCK.get());
            event.accept(FENCE_SOURCE_BLOCK.get());
        }
    }
}
