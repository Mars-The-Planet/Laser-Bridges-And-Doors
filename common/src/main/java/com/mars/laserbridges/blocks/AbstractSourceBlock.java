package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public abstract class AbstractSourceBlock extends HorizontalDirectionalBlock implements ISourceBlock{

    protected AbstractSourceBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(COLOR, 0));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(ISourceBlock.FACING, context.getHorizontalDirection());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(ISourceBlock.FACING, direction.getOpposite());
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level lvl,
                                              BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleUseItemOn(stack, state, lvl, pos, player);
    }

    @Override
    public void neighborChanged(BlockState state, Level lvl, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
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
    public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState newState, boolean movedByPiston) {
        handleOnRemove(state, lvl, pos);
    }
}