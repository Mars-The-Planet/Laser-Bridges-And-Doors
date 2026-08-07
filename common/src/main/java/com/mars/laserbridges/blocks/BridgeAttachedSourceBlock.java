package com.mars.laserbridges.blocks;

import com.mars.laserbridges.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.mars.laserbridges.ModRegistry.LASER_BRIDGE_BLOCK;

public class BridgeAttachedSourceBlock extends AbstractAttachedSourceBlock{

    protected static final VoxelShape CEILING_X_SHAPE = Block.box(5, 14, 0, 11, 16, 16);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.box(0, 14, 5, 16, 16, 11);
    protected static final VoxelShape CEILING_X_POWERED_SHAPE = Shapes.or(CEILING_X_SHAPE, Constants.CEILING_X_SHAPE);
    protected static final VoxelShape CEILING_Z_POWERED_SHAPE = Shapes.or(CEILING_Z_SHAPE, Constants.CEILING_Z_SHAPE);

    protected static final VoxelShape FLOOR_X_SHAPE = Block.box(5, 0, 0, 11, 2, 16);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.box(0, 0, 5, 16, 2, 11);
    protected static final VoxelShape FLOOR_X_POWERED_SHAPE = Shapes.or(FLOOR_X_SHAPE, Constants.FLOOR_X_SHAPE);
    protected static final VoxelShape FLOOR_Z_POWERED_SHAPE = Shapes.or(FLOOR_Z_SHAPE, Constants.FLOOR_Z_SHAPE);

    protected static final VoxelShape NORTH_SHAPE = Block.box(0, 5, 14, 16, 11, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0, 5, 0, 16, 11, 2);
    protected static final VoxelShape WEST_SHAPE = Block.box(14, 5, 0, 16, 11, 16);
    protected static final VoxelShape EAST_SHAPE = Block.box(0, 5, 0, 2, 11, 16);

    protected static final VoxelShape NORTH_POWERED_SHAPE = Shapes.or(NORTH_SHAPE, Constants.NORTH_SHAPE);
    protected static final VoxelShape SOUTH_POWERED_SHAPE = Shapes.or(SOUTH_SHAPE, Constants.SOUTH_SHAPE);
    protected static final VoxelShape WEST_POWERED_SHAPE = Shapes.or(WEST_SHAPE, Constants.WEST_SHAPE);
    protected static final VoxelShape EAST_POWERED_SHAPE = Shapes.or(EAST_SHAPE, Constants.EAST_SHAPE);

    public BridgeAttachedSourceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Block LaserBlockType() {
        return LASER_BRIDGE_BLOCK.get();
    }

    @Override
    public VoxelShape getCeilingXShape() {
        return CEILING_X_SHAPE;
    }

    @Override
    public VoxelShape getCeilingZShape() {
        return CEILING_Z_SHAPE;
    }

    @Override
    public VoxelShape getFloorXShape() {
        return FLOOR_X_SHAPE;
    }

    @Override
    public VoxelShape getFloorZShape() {
        return FLOOR_Z_SHAPE;
    }

    @Override
    public VoxelShape getNorthShape() {
        return NORTH_SHAPE;
    }

    @Override
    public VoxelShape getSouthShape() {
        return SOUTH_SHAPE;
    }

    @Override
    public VoxelShape getWestShape() {
        return WEST_SHAPE;
    }

    @Override
    public VoxelShape getEastShape() {
        return EAST_SHAPE;
    }

    @Override
    public VoxelShape getPoweredCeilingXShape() {
        return CEILING_X_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredCeilingZShape() {
        return CEILING_Z_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredFloorXShape() {
        return FLOOR_X_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredFloorZShape() {
        return FLOOR_Z_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredNorthShape() {
        return NORTH_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredSouthShape() {
        return SOUTH_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredWestShape() {
        return WEST_POWERED_SHAPE;
    }

    @Override
    public VoxelShape getPoweredEastShape() {
        return EAST_POWERED_SHAPE;
    }
}
