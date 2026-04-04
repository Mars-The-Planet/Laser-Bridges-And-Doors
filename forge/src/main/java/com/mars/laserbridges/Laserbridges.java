package com.mars.laserbridges;

import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import com.mars.laserbridges.blocks.LaserBridgeBlock;
import com.mars.laserbridges.blocks.LaserFenceBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

@Mod(Constants.MOD_ID)
public class Laserbridges {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
    static Identifier laser_source_block_id = Identifier.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME);
    static Identifier laser_block_id = Identifier.fromNamespaceAndPath(MOD_ID, LASER_BLOCK_NAME);
    static Identifier laser_fence_source_block_id = Identifier.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME);
    static Identifier laser_fence_block_id = Identifier.fromNamespaceAndPath(MOD_ID, LASER_FENCE_BLOCK_NAME);

    public static final RegistryObject<Block> BRIDGE_SOURCE_BLOCK = BLOCKS.register(BRIDGE_SOURCE_BLOCK_NAME, () ->
            new BridgeSourceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_source_block_id)).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0)));
    public static final RegistryObject<BlockItem> BRIDGE_SOURCE_BLOCK_ITEM = ITEMS.register(BRIDGE_SOURCE_BLOCK_NAME, () -> new BlockItem(BRIDGE_SOURCE_BLOCK.get(), new Item.Properties().useBlockDescriptionPrefix().setId(ITEMS.key(laser_source_block_id))));
    public static final RegistryObject<Block> LASER_BLOCK = BLOCKS.register(LASER_BLOCK_NAME, () -> new LaserBridgeBlock(BlockBehaviour.Properties
            .of().setId(ResourceKey.create(Registries.BLOCK, laser_block_id)).strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((p_220871_) -> LIGHT)));

    public static final RegistryObject<Block> LASER_FENCE_SOURCE_BLOCK = BLOCKS.register(FENCE_SOURCE_BLOCK_NAME, () ->
            new FenceSourceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, laser_fence_source_block_id)).strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0)));

    public static final RegistryObject<BlockItem> LASER_FENCE_SOURCE_BLOCK_ITEM = ITEMS.register(FENCE_SOURCE_BLOCK_NAME, () -> new BlockItem(LASER_FENCE_SOURCE_BLOCK.get(), new Item.Properties().useBlockDescriptionPrefix().setId(ITEMS.key(laser_fence_source_block_id))));

    public static final RegistryObject<Block> LASER_FENCE_BLOCK = BLOCKS.register(LASER_FENCE_BLOCK_NAME, () -> new LaserFenceBlock(BlockBehaviour.Properties
            .of().strength(-1.0F, 3600000.0F).setId(ResourceKey.create(Registries.BLOCK, laser_fence_block_id)).noOcclusion().lightLevel((p_220871_) -> LIGHT)));

    public static final RegistryObject<SoundEvent> ON = registerSoundEvent(ON_NAME);
    public static final RegistryObject<SoundEvent> OFF = registerSoundEvent(OFF_NAME);
    public Laserbridges() {
        CommonClass.init();

        var modEventBus = FMLJavaModLoadingContext.get().getModBusGroup();
        
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        RegisterColorHandlersEvent.Block.BUS.addListener(Laserbridges::registerBlockColorHandlers);
        //FMLClientSetupEvent.getBus(modEventBus).addListener(Laserbridges::clientSetup);
        BuildCreativeModeTabContentsEvent.BUS.addListener(Laserbridges::addCreative);
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return DyeColor.byId(state.getValue(COLOR)).getTextureDiffuseColor();
            }
        }), BRIDGE_SOURCE_BLOCK.get(), LASER_FENCE_SOURCE_BLOCK.get(), LASER_BLOCK.get(), LASER_FENCE_BLOCK.get());

        //event.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(COLOR))).getTextureDiffuseColor(), BRIDGE_SOURCE_BLOCK.get(), LASER_FENCE_SOURCE_BLOCK.get(), LASER_BLOCK.get(), LASER_FENCE_BLOCK.get());
    }

//    @SubscribeEvent
//    private static void clientSetup(FMLClientSetupEvent event) {
//        ItemBlockRenderTypes.setRenderLayer(LASER_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(BRIDGE_SOURCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(LASER_FENCE_SOURCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(LASER_FENCE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
//    }

    @SubscribeEvent
    private static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS){
            event.accept(BRIDGE_SOURCE_BLOCK_ITEM);
            event.accept(LASER_FENCE_SOURCE_BLOCK_ITEM);
        }
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Identifier.fromNamespaceAndPath(MOD_ID, name), 75f));
    }
}
