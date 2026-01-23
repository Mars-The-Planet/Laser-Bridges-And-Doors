package com.mars.laserbridges.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaserBridgeBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final IntegerProperty COLOR = BridgeSourceBlock.COLOR;
    public static final MapCodec<LaserBridgeBlock> CODEC = simpleCodec(LaserBridgeBlock::new);

    public static final VoxelShape CEILING_X_SHAPE = Block.box(7.99999, 0, 0, 8, 14, 16);
    public static final VoxelShape CEILING_Z_SHAPE = Block.box(0, 0, 7.99999, 16, 14, 8);
    public static final VoxelShape FLOOR_X_SHAPE = Block.box(7.99999, 0, 0, 8, 16, 16);
    public static final VoxelShape FLOOR_Z_SHAPE = Block.box(0, 0, 7.99999, 16, 16, 8);
    public static final VoxelShape NORTH_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape WEST_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape EAST_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);

    public LaserBridgeBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        switch (state.getValue(FACE)) {
            case FLOOR -> {
                if (dir.getAxis() == Direction.Axis.X) {
                    return FLOOR_X_SHAPE;
                }
                return FLOOR_Z_SHAPE;
            }
            case WALL -> {
                switch (dir) {
                    case EAST -> {
                        return EAST_SHAPE;
                    }
                    case WEST -> {
                        return WEST_SHAPE;
                    }
                    case SOUTH -> {
                        return SOUTH_SHAPE;
                    }
                }
                return NORTH_SHAPE;
            }
        }
        if (dir.getAxis() == Direction.Axis.X) {
            return CEILING_X_SHAPE;
        }
        return CEILING_Z_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(FACE);
        builder.add(COLOR);
    }

    @Override
    protected boolean isPathfindable(BlockState p_57535_, PathComputationType p_57538_) {
        return false;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
