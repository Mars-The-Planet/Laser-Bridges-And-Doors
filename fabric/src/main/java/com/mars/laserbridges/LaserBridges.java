package com.mars.laserbridges;

import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import com.mars.laserbridges.blocks.LaserBridgeBlock;
import com.mars.laserbridges.blocks.LaserFenceBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

public class LaserBridges implements ModInitializer, ClientModInitializer {
    public static final Block BRIDGE_SOURCE_BLOCK = new BridgeSourceBlock(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));
    public static final Block LASER_BLOCK = new LaserBridgeBlock(BlockBehaviour.Properties.of(Material.METAL).strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));
    public static final Block LASER_FENCE_SOURCE_BLOCK = new FenceSourceBlock(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));
    public static final Block LASER_FENCE_BLOCK = new LaserFenceBlock(BlockBehaviour.Properties.of(Material.METAL).strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));

    public static final SoundEvent ON = new SoundEvent(new ResourceLocation(MOD_ID, ON_NAME));
    public static final SoundEvent OFF = new SoundEvent(new ResourceLocation(MOD_ID, OFF_NAME));
    @Override
    public void onInitialize() {
        CommonClass.init();

        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME), BRIDGE_SOURCE_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME), new BlockItem(BRIDGE_SOURCE_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, LASER_BLOCK_NAME), LASER_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, LASER_BLOCK_NAME), new BlockItem(LASER_BLOCK, new Item.Properties()));

        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, FENCE_SOURCE_BLOCK_NAME), LASER_FENCE_SOURCE_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, FENCE_SOURCE_BLOCK_NAME), new BlockItem(LASER_FENCE_SOURCE_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, LASER_FENCE_BLOCK_NAME), LASER_FENCE_BLOCK);
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, LASER_FENCE_BLOCK_NAME), new BlockItem(LASER_FENCE_BLOCK, new Item.Properties()));
    }

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(LaserBridges.LASER_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(LaserBridges.BRIDGE_SOURCE_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(LaserBridges.LASER_FENCE_SOURCE_BLOCK, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(LaserBridges.LASER_FENCE_BLOCK, RenderType.translucent());

        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) ->
                        ((((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[0] * 255)) << 16) | (((int)(DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[1] * 255)) << 8) | (int) ((DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColors()[2] * 255))),
                BRIDGE_SOURCE_BLOCK, LASER_FENCE_SOURCE_BLOCK, LASER_BLOCK, LASER_FENCE_BLOCK);
    }
}
