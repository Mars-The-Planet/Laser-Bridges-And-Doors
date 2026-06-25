package com.mars.laserbridges;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mars.laserbridges.blocks.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.Constants.OFF_NAME;

public class ModRegistry {
    public static final HashMap<String, Supplier<Block>> BLOCKS_REG = new HashMap<>();
    public static final HashMap<String, Supplier<BlockItem>> BLOCK_ITEMS_REG = new HashMap<>();
    public static final HashMap<String, Supplier<SoundEvent>> SOUND_EVENTS_REG = new HashMap<>();

    // BLocks
    public static final Supplier<Block> BRIDGE_ATTACHED_SOURCE_BLOCK = Suppliers.memoize(() ->
            new BridgeAttachedSourceBlock(BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0)));

    public static final Supplier<Block> FENCE_ATTACHED_SOURCE_BLOCK = Suppliers.memoize(() ->
            new FenceAttachedSourceBlock(BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.POWERED) ? LIGHT : 0)));

    public static final Supplier<Block> BRIDGE_SOURCE_BLOCK = Suppliers.memoize(() ->
            new BridgeSourceBlock(BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> LIGHT)));

    public static final Supplier<Block> FENCE_SOURCE_BLOCK = Suppliers.memoize(() ->
            new FenceSourceBlock(BlockBehaviour.Properties.of().strength(0.8f).noOcclusion().lightLevel((state) -> LIGHT)));

    public static final Supplier<Block> LASER_BRIDGE_BLOCK = Suppliers.memoize(() ->
            new LaserBridgeBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((state) -> LIGHT)));

    public static final Supplier<Block> LASER_FENCE_BLOCK = Suppliers.memoize(() ->
            new LaserFenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noOcclusion().lightLevel((state) -> LIGHT)));

    // Items
    public static final Supplier<BlockItem> BRIDGE_ATTACHED_SOURCE_BLOCK_ITEM = Suppliers.memoize(() ->
            new BlockItem(BRIDGE_ATTACHED_SOURCE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<BlockItem> FENCE_ATTACHED_SOURCE_BLOCK_ITEM = Suppliers.memoize(() ->
            new BlockItem(FENCE_ATTACHED_SOURCE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<BlockItem> BRIDGE_SOURCE_BLOCK_ITEM = Suppliers.memoize(() ->
            new BlockItem(BRIDGE_SOURCE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<BlockItem> FENCE_SOURCE_BLOCK_ITEM = Suppliers.memoize(() ->
            new BlockItem(FENCE_SOURCE_BLOCK.get(), new Item.Properties()));

    // Sound Events
    public static final Supplier<SoundEvent> SOUND_ON = Suppliers.memoize(() ->
            SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, ON_NAME), 75f));

    public static final Supplier<SoundEvent> SOUND_OFF = Suppliers.memoize(() ->
            SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, OFF_NAME), 75f));

    public static void RegisterSetup() {
        // Blocks
        BLOCKS_REG.put(BRIDGE_ATTACHED_SOURCE_BLOCK_NAME, BRIDGE_ATTACHED_SOURCE_BLOCK);
        BLOCKS_REG.put(LASER_BRIDGE_BLOCK_NAME, LASER_BRIDGE_BLOCK);
        BLOCKS_REG.put(BRIDGE_SOURCE_BLOCK_NAME, BRIDGE_SOURCE_BLOCK);
        BLOCKS_REG.put(FENCE_SOURCE_BLOCK_NAME, FENCE_SOURCE_BLOCK);

        BLOCKS_REG.put(FENCE_ATTACHED_SOURCE_BLOCK_NAME, FENCE_ATTACHED_SOURCE_BLOCK);
        BLOCKS_REG.put(LASER_FENCE_BLOCK_NAME, LASER_FENCE_BLOCK);

        // Items
        BLOCK_ITEMS_REG.put(BRIDGE_ATTACHED_SOURCE_BLOCK_NAME, BRIDGE_ATTACHED_SOURCE_BLOCK_ITEM);
        BLOCK_ITEMS_REG.put(FENCE_ATTACHED_SOURCE_BLOCK_NAME, FENCE_ATTACHED_SOURCE_BLOCK_ITEM);
        BLOCK_ITEMS_REG.put(BRIDGE_SOURCE_BLOCK_NAME, BRIDGE_SOURCE_BLOCK_ITEM);
        BLOCK_ITEMS_REG.put(FENCE_SOURCE_BLOCK_NAME, FENCE_SOURCE_BLOCK_ITEM);

        // Sound Events
        SOUND_EVENTS_REG.put(ON_NAME, SOUND_ON);
        SOUND_EVENTS_REG.put(OFF_NAME, SOUND_OFF);
    }
}
