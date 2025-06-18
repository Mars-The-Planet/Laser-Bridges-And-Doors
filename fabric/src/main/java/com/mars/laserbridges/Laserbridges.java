package com.mars.laserbridges;

import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import com.mars.laserbridges.blocks.LaserBridgeBlock;
import com.mars.laserbridges.blocks.LaserFenceBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

public class Laserbridges implements ModInitializer, ClientModInitializer {
    static ResourceLocation laser_source_block_id = ResourceLocation.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME);
    static ResourceLocation laser_block_id = ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_BLOCK_NAME);
    static ResourceLocation laser_fence_source_block_id = ResourceLocation.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME);
    static ResourceLocation laser_fence_block_id = ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_FENCE_BLOCK_NAME);

    public static final Block BRIDGE_SOURCE_BLOCK = new BridgeSourceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_source_block_id)).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));
    public static final Block LASER_BLOCK = new LaserBridgeBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_block_id)).strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));
    public static final Block LASER_FENCE_SOURCE_BLOCK = new FenceSourceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_fence_source_block_id)).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));
    public static final Block LASER_FENCE_BLOCK = new LaserFenceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_fence_block_id)).strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));

    public static final SoundEvent ON = register(ON_NAME);
    public static final SoundEvent OFF = register(OFF_NAME);
    @Override
    public void onInitialize() {
        CommonClass.init();

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME), BRIDGE_SOURCE_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME), new BlockItem(BRIDGE_SOURCE_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, laser_source_block_id))));

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_BLOCK_NAME), LASER_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_BLOCK_NAME), new BlockItem(LASER_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, laser_block_id))));

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME), LASER_FENCE_SOURCE_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME), new BlockItem(LASER_FENCE_SOURCE_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, laser_fence_source_block_id))));

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_FENCE_BLOCK_NAME), LASER_FENCE_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, LASER_FENCE_BLOCK_NAME), new BlockItem(LASER_FENCE_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, laser_fence_block_id))));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(BRIDGE_SOURCE_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.accept(LASER_FENCE_SOURCE_BLOCK));
    }

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(Laserbridges.LASER_BLOCK, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Laserbridges.BRIDGE_SOURCE_BLOCK, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Laserbridges.LASER_FENCE_SOURCE_BLOCK, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Laserbridges.LASER_FENCE_BLOCK, ChunkSectionLayer.TRANSLUCENT);

        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(COLOR))).getTextureDiffuseColor(), BRIDGE_SOURCE_BLOCK, LASER_FENCE_SOURCE_BLOCK, LASER_BLOCK, LASER_FENCE_BLOCK);
    }

    public static SoundEvent register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createFixedRangeEvent(id, 75));
    }
}
