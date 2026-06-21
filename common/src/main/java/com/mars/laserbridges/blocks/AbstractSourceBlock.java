package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractSourceBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);

    protected AbstractSourceBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(COLOR, 0));
    }

    public abstract void generateBridge(LevelAccessor world, int power, BlockPos pos, BlockState state);

    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(FACE);
        builder.add(POWERED);
        builder.add(COLOR);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level lvl,
                                              BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        if (item instanceof DyeItem) {
            int col = ((DyeItem) item).getDyeColor().getId();
            stack.consume(1, player);
            player.awardStat(Stats.ITEM_USED.get(item));
            lvl.setBlock(pos, state.setValue(COLOR, col), 3);
            return ItemInteractionResult.sidedSuccess(lvl.isClientSide);
        }
        return super.useItemOn(stack, state, lvl, pos, player, hand, hit);
    }

    @Override
    public void neighborChanged(BlockState state, Level lvl, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (lvl.isClientSide)
            return;

        int redstonePower = lvl.getBestNeighborSignal(pos);
        boolean flag = state.getValue(POWERED);

        if (flag != lvl.hasNeighborSignal(pos)) {
            if (flag) {
                lvl.scheduleTick(pos, this, 0);
            }
            else {
                lvl.setBlock(pos, state.cycle(POWERED), 3);
                generateBridge(lvl, redstonePower, pos, state);
            }
        }
        else if (flag) {
            lvl.scheduleTick(pos, this, 0);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource random) {
        if (lvl.isClientSide) {
            return;
        }

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

    @Override
    public void onPlace(BlockState state, Level lvl, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (lvl.isClientSide) {
            return;
        }

        boolean bl = state.getValue(POWERED);

        if (bl != lvl.hasNeighborSignal(pos)) {
            if (bl) {
                lvl.scheduleTick(pos, this, 0);
            }
            else {
                lvl.setBlock(pos, state.cycle(POWERED), 2);
                int redstone = lvl.getBestNeighborSignal(pos);
                generateBridge(lvl, redstone, pos, state);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!lvl.isClientSide) {
            lvl.playSound((Player)null, pos, SoundEvents.METAL_BREAK, SoundSource.BLOCKS, 0.3F, 0.5F);
            generateBridge(lvl, 0, pos, state);
        }
    }
}