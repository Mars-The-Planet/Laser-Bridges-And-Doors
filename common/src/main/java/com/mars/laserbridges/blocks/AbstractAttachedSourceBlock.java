package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAttachedSourceBlock extends FaceAttachedHorizontalDirectionalBlock implements ISourceBlock {

    protected AbstractAttachedSourceBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(COLOR, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ISourceBlock.FACING, ISourceBlock.FACE, ISourceBlock.POWERED, ISourceBlock.COLOR);
    }

    public abstract Block LaserBlockType();

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return handleGetShape(state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level lvl,
                                          BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleUseItemOn(stack, state, lvl, pos, player);
    }

    @Override
    public void neighborChanged(BlockState state, Level lvl, BlockPos pos, Block neighborBlock, @Nullable Orientation p_365159_, boolean p_60514_) {
        handleNeighborChanged(this, state, lvl, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource random) {
        handleTick(state, lvl, pos);
    }

    @Override
    public void onPlace(BlockState state, Level lvl, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        handleOnPlace(this, state, lvl, pos);
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel lvl, BlockPos pos, boolean movedByPiston) {
        handleOnRemove(state, lvl, pos);
    }
}
