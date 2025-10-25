package com.mars.laserbridges.blocks;

import com.mars.laserbridges.LasersConfig;
import com.mars.laserbridges.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.mars.laserbridges.Constants.*;

public class BridgeSourceBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);

    protected static final VoxelShape CEILING_X_SHAPE = Block.box(5, 14, 0, 11, 16, 16);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.box(0, 14, 5, 16, 16, 11);
    protected static final VoxelShape CEILING_X_POWERED_SHAPE = Shapes.or(CEILING_X_SHAPE, LaserBridgeBlock.CEILING_X_SHAPE);
    protected static final VoxelShape CEILING_Z_POWERED_SHAPE = Shapes.or(CEILING_Z_SHAPE, LaserBridgeBlock.CEILING_Z_SHAPE);

    protected static final VoxelShape FLOOR_X_SHAPE = Block.box(5, 0, 0, 11, 2, 16);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.box(0, 0, 5, 16, 2, 11);
    protected static final VoxelShape FLOOR_X_POWERED_SHAPE = Shapes.or(FLOOR_X_SHAPE, LaserBridgeBlock.FLOOR_X_SHAPE);
    protected static final VoxelShape FLOOR_Z_POWERED_SHAPE = Shapes.or(FLOOR_Z_SHAPE, LaserBridgeBlock.FLOOR_Z_SHAPE);

    protected static final VoxelShape NORTH_SHAPE = Block.box(0, 5, 14, 16, 11, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0, 5, 0, 16, 11, 2);
    protected static final VoxelShape WEST_SHAPE = Block.box(14, 5, 0, 16, 11, 16);
    protected static final VoxelShape EAST_SHAPE = Block.box(0, 5, 0, 2, 11, 16);

    protected static final VoxelShape NORTH_POWERED_SHAPE = Shapes.or(NORTH_SHAPE, LaserBridgeBlock.NORTH_SHAPE);
    protected static final VoxelShape SOUTH_POWERED_SHAPE = Shapes.or(SOUTH_SHAPE, LaserBridgeBlock.SOUTH_SHAPE);
    protected static final VoxelShape WEST_POWERED_SHAPE = Shapes.or(WEST_SHAPE, LaserBridgeBlock.WEST_SHAPE);
    protected static final VoxelShape EAST_POWERED_SHAPE = Shapes.or(EAST_SHAPE, LaserBridgeBlock.EAST_SHAPE);
    public BridgeSourceBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(COLOR, 0));
    }

    public void generateBridge(LevelAccessor world, int power, BlockPos pos, BlockState state){
        Direction dir = state.getValue(FACING);
        AttachFace face = state.getValue(FACE);
        int col = state.getValue(COLOR);
        boolean bridgeEnded = false;

        float min_length = (float) LasersConfig.max_length / 15;
        int length = (int) (min_length * power);

        world.playSound((Player)null, pos, Services.PLATFORM.getSoundEvent(power), SoundSource.BLOCKS, 0.3F, 0.5F);

        for (int i = 1; i < LasersConfig.max_length; i++) {
            BlockPos addPos = new BlockPos(0, 0, 0);
            BlockState bs = (Registry.BLOCK.get(new ResourceLocation(MOD_ID, LASER_BLOCK_NAME))).defaultBlockState().setValue(COLOR, col).setValue(FACING, dir).setValue(FACE, face);
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
            String nextBlockName = (Registry.BLOCK.getKey(nextState.getBlock())).toString();
            boolean isDestroyable = LasersConfig.blocks_cut_through_by_lasers.contains(nextBlockName);

            if(!(nextState.equals(bs) || isDestroyable))
                return;
            if(isDestroyable)
                world.destroyBlock(nextPos, true);
            if(!bridgeEnded && i < length)
                world.setBlock(nextPos, bs, 3);
            else{
                bridgeEnded = true;
                world.setBlock(nextPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if(item instanceof DyeItem) {
            int col = ((DyeItem) item).getDyeColor().getId();
            if(player == null || !player.getAbilities().instabuild)
                stack.shrink(1);
            player.awardStat(Stats.ITEM_USED.get(item));
            lvl.setBlock(pos, state.setValue(COLOR, col), 2);
            return InteractionResult.sidedSuccess(lvl.isClientSide);
        }
        return super.use(state, lvl, pos, player, hand, hit);
    }

    public void neighborChanged(BlockState state, Level lvl, BlockPos pos, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        if (lvl.isClientSide) {
            return;
        }

        int redstonePower = lvl.getBestNeighborSignal(pos);
        boolean flag = state.getValue(POWERED);

        if(flag != lvl.hasNeighborSignal(pos)){
            if(flag){
                lvl.scheduleTick(pos, this, 0);
            }
            else {
                lvl.setBlock(pos, state.cycle(POWERED), 2);
                generateBridge(lvl, redstonePower, pos, state);
            }
        }
        else if(flag){
            lvl.scheduleTick(pos, this, 0);
        }
    }

    public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource p_221940_) {
        if (lvl.isClientSide) {
            return;
        }

        if(state.getValue(POWERED)){
            if(!lvl.hasNeighborSignal(pos)){
                lvl.setBlock(pos, state.cycle(POWERED), 2);
                generateBridge(lvl, 0, pos, state);
            }
            else{
                int redstonePower = lvl.getBestNeighborSignal(pos);
                generateBridge(lvl, redstonePower, pos, state);
            }
        }
    }

    public void onPlace(BlockState state, Level lvl, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
        if (lvl.isClientSide) {
            return;
        }

        boolean bl = state.getValue(POWERED);

        if(bl != lvl.hasNeighborSignal(pos)){
            if(bl){
                lvl.scheduleTick(pos, this, 0);
            }
            else{
                lvl.setBlock(pos, state.cycle(POWERED), 2);
                int redstone = lvl.getBestNeighborSignal(pos);
                generateBridge(lvl, redstone, pos, state);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (!lvl.isClientSide) {
            lvl.playSound((Player)null, pos, SoundEvents.METAL_BREAK, SoundSource.BLOCKS, 0.3F, 0.5F);
            generateBridge(lvl, 0, pos, state);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction p_53191_, BlockState statea, LevelAccessor lvl, BlockPos pos, BlockPos p_53195_) {
        if(!state.canSurvive(lvl, pos)){
            generateBridge(lvl, 0, pos, state);
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean powered = state.getValue(POWERED);
        switch (state.getValue(FACE)) {
            case FLOOR -> {
                if (dir.getAxis() == Direction.Axis.X) {
                    return powered ? FLOOR_X_POWERED_SHAPE : FLOOR_X_SHAPE;
                }
                return powered ? FLOOR_Z_POWERED_SHAPE : FLOOR_Z_SHAPE;
            }
            case WALL -> {
                switch (dir) {
                    case EAST -> {
                        return powered ? EAST_POWERED_SHAPE : EAST_SHAPE;
                    }
                    case WEST -> {
                        return powered ? WEST_POWERED_SHAPE : WEST_SHAPE;
                    }
                    case SOUTH -> {
                        return powered ? SOUTH_POWERED_SHAPE : SOUTH_SHAPE;
                    }
                }
                return powered ? NORTH_POWERED_SHAPE : NORTH_SHAPE;
            }
        }
        if (dir.getAxis() == Direction.Axis.X) {
            return powered ? CEILING_X_POWERED_SHAPE : CEILING_X_SHAPE;
        }
        return powered ? CEILING_Z_POWERED_SHAPE : CEILING_Z_SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(POWERED);
        builder.add(FACE);
        builder.add(COLOR);
    }
}
