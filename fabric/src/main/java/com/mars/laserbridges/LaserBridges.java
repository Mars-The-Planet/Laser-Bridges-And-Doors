package com.mars.laserbridges;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.ModRegistry.*;
import static com.mars.laserbridges.blocks.ISourceBlock.COLOR;

public class LaserBridges implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {

        CommonClass.init();

        BLOCKS_REG.forEach((s, blockSupplier) -> Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, s), blockSupplier.get()));
        BLOCK_ITEMS_REG.forEach((s, blockItemSupplier) -> Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, s), blockItemSupplier.get()));
        SOUND_EVENTS_REG.forEach((s, soundEventSupplier) -> Registry.register(BuiltInRegistries.SOUND_EVENT, Identifier.fromNamespaceAndPath(MOD_ID, s), soundEventSupplier.get()));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(BRIDGE_ATTACHED_SOURCE_BLOCK.get()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(FENCE_ATTACHED_SOURCE_BLOCK.get()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(BRIDGE_SOURCE_BLOCK.get()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(FENCE_SOURCE_BLOCK.get()));
    }

    @Override
    public void onInitializeClient() {
        BlockTintSource colorTintSource = new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }
        };

        List<BlockTintSource> tintSourceList = List.of(colorTintSource);

        BLOCKS_REG.forEach((name, blockSupplier) -> {
            BlockColorRegistry.register(tintSourceList, blockSupplier.get());
        });
    }
}
