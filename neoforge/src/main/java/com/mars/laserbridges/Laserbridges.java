package com.mars.laserbridges;


import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import com.mars.laserbridges.blocks.LaserBridgeBlock;
import com.mars.laserbridges.blocks.LaserFenceBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

@Mod(MOD_ID)
public class Laserbridges {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static final DeferredBlock<Block> BRIDGE_SOURCE_BLOCK = BLOCKS.registerBlock(BRIDGE_SOURCE_BLOCK_NAME, BridgeSourceBlock::new,
            BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));
    public static final DeferredItem<BlockItem> BRIDGE_SOURCE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BRIDGE_SOURCE_BLOCK_NAME, BRIDGE_SOURCE_BLOCK);
    public static final DeferredBlock<Block> LASER_BLOCK = BLOCKS.registerBlock(LASER_BLOCK_NAME, LaserBridgeBlock::new,
            BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));

    public static final DeferredBlock<Block> LASER_FENCE_SOURCE_BLOCK = BLOCKS.registerBlock(FENCE_SOURCE_BLOCK_NAME, FenceSourceBlock::new,
            BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0));

    public static final DeferredItem<BlockItem> LASER_FENCE_SOURCE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(FENCE_SOURCE_BLOCK_NAME, LASER_FENCE_SOURCE_BLOCK);

    public static final DeferredBlock<Block> LASER_FENCE_BLOCK = BLOCKS.registerBlock(LASER_FENCE_BLOCK_NAME, LaserFenceBlock::new,
            BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT));

    public static final DeferredHolder<SoundEvent, SoundEvent> ON = registerSoundEvent(ON_NAME);
    public static final DeferredHolder<SoundEvent, SoundEvent> OFF = registerSoundEvent(OFF_NAME);
    public Laserbridges(IEventBus modEventBus) {
        CommonClass.init();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::clientSetup);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(COLOR))).getTextureDiffuseColor(), BRIDGE_SOURCE_BLOCK.value(), LASER_FENCE_SOURCE_BLOCK.value(), LASER_BLOCK.value(), LASER_FENCE_BLOCK.value());
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(LASER_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(BRIDGE_SOURCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(LASER_FENCE_SOURCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(LASER_FENCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS){
            event.accept(BRIDGE_SOURCE_BLOCK_ITEM);
            event.accept(LASER_FENCE_SOURCE_BLOCK_ITEM);
        }
    }

    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, name), 75f));
    }
}
