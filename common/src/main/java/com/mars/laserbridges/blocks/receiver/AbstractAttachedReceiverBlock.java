package com.mars.laserbridges.blocks.receiver;

import com.mars.laserbridges.blocks.ILaserGeneratingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractAttachedReceiverBlock extends FaceAttachedHorizontalDirectionalBlock implements IReceiverBlock {

    protected AbstractAttachedReceiverBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(COLOR, 0));
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock())) {
            if ((Boolean)state.getValue(POWERED)) {
                level.updateNeighborsAt(pos, this);
                level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        updateState(this, state, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ILaserGeneratingBlock.FACING, ILaserGeneratingBlock.FACE, ILaserGeneratingBlock.POWERED, ILaserGeneratingBlock.COLOR);
    }

    @Override
    protected void neighborChanged(BlockState state, Level lvl, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        updateState(this, state, lvl, pos);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return handleIsSignalSource();
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return handleGetSignal(state, level, pos, direction);
    }

    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) && getConnectedDirection(state) == direction ? 15 : 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level lvl,
                                              BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleUseItemOn(stack, state, lvl, pos, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return handleGetShape(state);
    }
}
