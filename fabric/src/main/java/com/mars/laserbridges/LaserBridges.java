package com.mars.laserbridges;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;

import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.ModRegistry.*;
import static com.mars.laserbridges.blocks.ISourceBlock.COLOR;

public class LaserBridges implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {

        CommonClass.init();

        BLOCKS_REG.forEach((s, blockSupplier) -> Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, s), blockSupplier.get()));
        BLOCK_ITEMS_REG.forEach((s, blockItemSupplier) -> Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, s), blockItemSupplier.get()));
        SOUND_EVENTS_REG.forEach((s, soundEventSupplier) -> Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(MOD_ID, s), soundEventSupplier.get()));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(BRIDGE_ATTACHED_SOURCE_BLOCK.get()));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(FENCE_ATTACHED_SOURCE_BLOCK.get()));
    }

    @Override
    public void onInitializeClient() {
        BLOCKS_REG.forEach((s, blockSupplier) -> {
            BlockRenderLayerMap.INSTANCE.putBlock(blockSupplier.get(), RenderType.translucent());
            ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(COLOR))).getTextureDiffuseColor(), blockSupplier.get());
        });

    }
}
