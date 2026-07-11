package com.mars.laserbridges.blocks.source;

import com.mars.laserbridges.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaserFenceBlock extends AbstractLaserBlock{

    public static final VoxelShape CEILING_X_SHAPE = Constants.CEILING_Z_SHAPE;
    public static final VoxelShape CEILING_Z_SHAPE = Constants.CEILING_X_SHAPE;
    public static final VoxelShape FLOOR_X_SHAPE = Constants.FLOOR_Z_SHAPE;
    public static final VoxelShape FLOOR_Z_SHAPE = Constants.FLOOR_X_SHAPE;
    public static final VoxelShape NORTH_SHAPE = Block.box(7.99999, 0, 0, 8, 16, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.box(7.99999, 0, 0, 8, 16, 16);
    public static final VoxelShape WEST_SHAPE = Block.box(0, 0, 7.99999, 16, 16, 8);
    public static final VoxelShape EAST_SHAPE = Block.box(0, 0, 7.99999, 16, 16, 8);

    public static final MapCodec<LaserFenceBlock> CODEC = simpleCodec(LaserFenceBlock::new);

    public LaserFenceBlock(Properties properties) {
        super(properties);
    }

    @Override protected VoxelShape getCeilingXShape() { return CEILING_X_SHAPE; }
    @Override protected VoxelShape getCeilingZShape() { return CEILING_Z_SHAPE; }
    @Override protected VoxelShape getFloorXShape() { return FLOOR_X_SHAPE; }
    @Override protected VoxelShape getFloorZShape() { return FLOOR_Z_SHAPE; }
    @Override protected VoxelShape getNorthShape() { return NORTH_SHAPE; }
    @Override protected VoxelShape getSouthShape() { return SOUTH_SHAPE; }
    @Override protected VoxelShape getWestShape() { return WEST_SHAPE; }
    @Override protected VoxelShape getEastShape() { return EAST_SHAPE; }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
