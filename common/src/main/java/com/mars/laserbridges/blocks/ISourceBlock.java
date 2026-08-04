package com.mars.laserbridges.blocks;

import com.mars.laserbridges.LaserBridgesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.mars.laserbridges.ModRegistry.SOUND_OFF;
import static com.mars.laserbridges.ModRegistry.SOUND_ON;

public interface ISourceBlock {
    EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    BooleanProperty POWERED = BlockStateProperties.POWERED;
    IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);

    Block LaserBlockType();

    default void generateBridge(LevelAccessor world, int power, BlockPos pos, BlockState state) {

        Direction dir = state.getValue(ISourceBlock.FACING);
        AttachFace face = state.getValue(ISourceBlock.FACE);
        int col = state.getValue(COLOR);
        boolean bridgeEnded = false;

        float min_length = (float) LaserBridgesConfig.max_length / 15;
        int length = (int) (min_length * power);

        world.playSound((Player)null, pos, (length == 0 ? SOUND_OFF.get() : SOUND_ON.get()), SoundSource.BLOCKS, 0.3F, 0.5F);

        for (int i = 1; i < LaserBridgesConfig.max_length; i++) {
            BlockPos addPos = new BlockPos(0, 0, 0);
            BlockState bs = LaserBlockType().defaultBlockState().setValue(COLOR, col).setValue(ISourceBlock.FACING, dir).setValue(ISourceBlock.FACE, face);
            switch (face) {
                case FLOOR -> addPos = new BlockPos(0, i, 0);
                case WALL -> {
                    switch (dir) {
                        case EAST -> addPos = new BlockPos(i, 0, 0);
                        case WEST -> addPos = new BlockPos(-i, 0, 0);
                        case SOUTH -> addPos = new BlockPos(0, 0, i);
                        case NORTH -> addPos = new BlockPos(0, 0, -i);
                    }
                }
                case CEILING -> addPos = new BlockPos(0, -i, 0);
            }

            BlockPos nextPos = pos.offset(addPos);
            BlockState nextState = world.getBlockState(nextPos);
            String nextBlockName = (BuiltInRegistries.BLOCK.getKey(nextState.getBlock())).toString();
            boolean isDestroyable = LaserBridgesConfig.blocks_cut_through_by_lasers.contains(nextBlockName);

            if (!(nextState.equals(bs) || isDestroyable))
                return;
            if (isDestroyable && power != 0)
                world.destroyBlock(nextPos, true);
            if (!bridgeEnded && i < length)
                world.setBlock(nextPos, bs, 3);
            else {
                bridgeEnded = true;
                if (world.getBlockState(nextPos).getBlock().equals(LaserBlockType()))
                    world.setBlock(nextPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    default VoxelShape handleGetShape(BlockState state) {
        Direction dir = state.getValue(ISourceBlock.FACING);
        boolean powered = state.getValue(POWERED);
        switch (state.getValue(ISourceBlock.FACE)) {
            case FLOOR -> {
                if (dir.getAxis() == Direction.Axis.X) {
                    return powered ? getPoweredFloorXShape() : getFloorXShape();
                }
                return powered ? getPoweredFloorZShape() : getFloorZShape();
            }
            case WALL -> {
                switch (dir) {
                    case EAST -> {
                        return powered ? getPoweredEastShape() : getEastShape();
                    }
                    case WEST -> {
                        return powered ? getPoweredWestShape() : getWestShape();
                    }
                    case SOUTH -> {
                        return powered ? getPoweredSouthShape() : getSouthShape();
                    }
                }
                return powered ? getPoweredNorthShape() : getNorthShape();
            }
        }
        if (dir.getAxis() == Direction.Axis.X) {
            return powered ? getPoweredCeilingXShape() : getCeilingXShape();
        }
        return powered ? getPoweredCeilingZShape() : getCeilingZShape();
    }

    default InteractionResult handleUseItemOn(ItemStack stack, BlockState state, Level lvl, BlockPos pos, Player player) {
        Item item = stack.getItem();
        if (item instanceof DyeItem) {
            int col = ((DyeItem) item).getDyeColor().getId();
            stack.consume(1, player);
            player.awardStat(Stats.ITEM_USED.get(item));
            lvl.setBlock(pos, state.setValue(COLOR, col), 3);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    default void handleNeighborChanged(Block blockInstance, BlockState state, Level lvl, BlockPos pos) {
        if (lvl.isClientSide)
            return;

        int redstonePower = lvl.getBestNeighborSignal(pos);
        boolean flag = state.getValue(POWERED);

        if (flag != lvl.hasNeighborSignal(pos)) {
            if (flag) {
                lvl.scheduleTick(pos, blockInstance, 0);
            }
            else {
                lvl.setBlock(pos, state.cycle(POWERED), 3);
                generateBridge(lvl, redstonePower, pos, state);
            }
        }
        else if (flag) {
            lvl.scheduleTick(pos, blockInstance, 0);
        }
    }

    default void handleTick(BlockState state, ServerLevel lvl, BlockPos pos) {
        if (lvl.isClientSide)
            return;

        if (state.getValue(POWERED)) {
            if (!lvl.hasNeighborSignal(pos)) {
                lvl.setBlock(pos, state.cycle(POWERED), 3);
                generateBridge(lvl, 0, pos, state);
            }
            else{
                int redstonePower = lvl.getBestNeighborSignal(pos);
                generateBridge(lvl, redstonePower, pos, state);
            }
        }
    }

    default void handleOnPlace(Block blockInstance, BlockState state, Level lvl, BlockPos pos) {
        if (lvl.isClientSide)
            return;

        boolean bl = state.getValue(POWERED);

        if (bl != lvl.hasNeighborSignal(pos)) {
            if (bl) {
                lvl.scheduleTick(pos, blockInstance, 0);
            }
            else {
                lvl.setBlock(pos, state.cycle(POWERED), 2);
                int redstone = lvl.getBestNeighborSignal(pos);
                generateBridge(lvl, redstone, pos, state);
            }
        }
    }

    default void handleOnRemove(BlockState state, Level lvl, BlockPos pos) {
        if (!lvl.isClientSide) {
            lvl.playSound((Player)null, pos, SoundEvents.METAL_BREAK, SoundSource.BLOCKS, 0.3F, 0.5F);
            generateBridge(lvl, 0, pos, state);
        }
    }

    VoxelShape getCeilingXShape();
    VoxelShape getCeilingZShape();
    VoxelShape getFloorXShape();
    VoxelShape getFloorZShape();
    VoxelShape getNorthShape();
    VoxelShape getSouthShape();
    VoxelShape getWestShape();
    VoxelShape getEastShape();

    VoxelShape getPoweredCeilingXShape();
    VoxelShape getPoweredCeilingZShape();
    VoxelShape getPoweredFloorXShape();
    VoxelShape getPoweredFloorZShape();
    VoxelShape getPoweredNorthShape();
    VoxelShape getPoweredSouthShape();
    VoxelShape getPoweredWestShape();
    VoxelShape getPoweredEastShape();
}
