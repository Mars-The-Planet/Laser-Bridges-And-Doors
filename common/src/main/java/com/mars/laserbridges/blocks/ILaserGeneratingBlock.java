package com.mars.laserbridges.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ILaserGeneratingBlock {
    DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    BooleanProperty POWERED = BlockStateProperties.POWERED;
    IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);

    default VoxelShape handleGetShape(BlockState state) {
        Direction dir = state.getValue(ILaserGeneratingBlock.FACING);
        boolean powered = state.getValue(POWERED);
        switch (state.getValue(ILaserGeneratingBlock.FACE)) {
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
