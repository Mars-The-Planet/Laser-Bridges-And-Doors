package com.mars.laserbridges;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String BRIDGE_ATTACHED_SOURCE_BLOCK_NAME = "laser_source_block";
    public static final String FENCE_ATTACHED_SOURCE_BLOCK_NAME = "laser_fence_source_block";
    public static final String LASER_BRIDGE_BLOCK_NAME = "laser_block_powered";
    public static final String LASER_FENCE_BLOCK_NAME = "laser_fence_powered";
    public static final String ON_NAME = "on";
    public static final String OFF_NAME = "off";
    public static int LIGHT = 10;
    public static final String MOD_ID = "laserbridges";
    public static final String MOD_NAME = "Laser Bridges & Doors";

    public static final VoxelShape CEILING_X_SHAPE = Block.box(7.99999, 0, 0, 8, 14, 16);
    public static final VoxelShape CEILING_Z_SHAPE = Block.box(0, 0, 7.99999, 16, 14, 8);
    public static final VoxelShape FLOOR_X_SHAPE = Block.box(7.99999, 0, 0, 8, 16, 16);
    public static final VoxelShape FLOOR_Z_SHAPE = Block.box(0, 0, 7.99999, 16, 16, 8);
    public static final VoxelShape NORTH_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape WEST_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);
    public static final VoxelShape EAST_SHAPE = Block.box(0, 7.99999, 0, 16, 8, 16);

    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
}
