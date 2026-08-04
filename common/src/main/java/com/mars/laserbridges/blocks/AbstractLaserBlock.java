package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractLaserBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final IntegerProperty COLOR = ISourceBlock.COLOR;

    protected abstract VoxelShape getCeilingXShape();
    protected abstract VoxelShape getCeilingZShape();
    protected abstract VoxelShape getFloorXShape();
    protected abstract VoxelShape getFloorZShape();
    protected abstract VoxelShape getNorthShape();
    protected abstract VoxelShape getSouthShape();
    protected abstract VoxelShape getWestShape();
    protected abstract VoxelShape getEastShape();

    protected AbstractLaserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        switch (state.getValue(FACE)) {
            case FLOOR -> {
                if (dir.getAxis() == Direction.Axis.X) {
                    return getFloorXShape();
                }
                return getFloorZShape();
            }
            case WALL -> {
                switch (dir) {
                    case EAST -> {
                        return getEastShape();
                    }
                    case WEST -> {
                        return getWestShape();
                    }
                    case SOUTH -> {
                        return getSouthShape();
                    }
                }
                return getNorthShape();
            }
        }
        if (dir.getAxis() == Direction.Axis.X) {
            return getCeilingXShape();
        }
        return getCeilingZShape();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, COLOR);
    }
}
