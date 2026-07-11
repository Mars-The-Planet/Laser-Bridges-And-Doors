package com.mars.laserbridges.blocks.receiver;

import com.mars.laserbridges.blocks.ILaserGeneratingBlock;
import com.mars.laserbridges.blocks.source.AbstractLaserBlock;
import com.mars.laserbridges.blocks.source.ISourceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public interface IReceiverBlock extends ILaserGeneratingBlock {

    default void updateState(Block blockInstance, BlockState state, Level lvl, BlockPos pos) {
        Direction facing = state.getValue(ILaserGeneratingBlock.FACING);
        AttachFace face = state.getValue(ILaserGeneratingBlock.FACE);
        Direction dir = switch (face) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> facing;
        };
        int color = state.getValue(ILaserGeneratingBlock.COLOR);
        boolean powered = state.getValue(ILaserGeneratingBlock.POWERED);

        BlockPos checkPos = pos.relative(dir);
        BlockState checkState = lvl.getBlockState(checkPos);
        Block checkblock = checkState.getBlock();

        boolean shouldPowered = false;

        if (checkblock instanceof AbstractLaserBlock || (checkblock instanceof ISourceBlock && checkState.getValue(ILaserGeneratingBlock.POWERED))) {
            Direction laserFacing = state.getValue(ILaserGeneratingBlock.FACING);
            AttachFace laserFace = state.getValue(ILaserGeneratingBlock.FACE);
            Direction laserDir = switch (laserFace) {
                case CEILING -> Direction.DOWN;
                case FLOOR -> Direction.UP;
                default -> laserFacing;
            };
            int laserColor = checkState.getValue(ILaserGeneratingBlock.COLOR);

            // is laser looking directly into receiver
            if (!(laserDir == dir.getOpposite())) {
                shouldPowered = true;
                color = laserColor;
            }
        }

        if (powered != shouldPowered) {
            lvl.setBlock(pos, state.setValue(POWERED, shouldPowered).setValue(COLOR, color), Block.UPDATE_ALL);

            lvl.updateNeighborsAt(pos, blockInstance);
            lvl.updateNeighborsAt(pos.relative(dir.getOpposite()), blockInstance);
        }
    }

    default ItemInteractionResult handleUseItemOn(ItemStack stack, BlockState state, Level lvl, BlockPos pos, Player player) {
        Item item = stack.getItem();
        if (item instanceof DyeItem) {
            int col = ((DyeItem) item).getDyeColor().getId();
            stack.consume(1, player);
            player.awardStat(Stats.ITEM_USED.get(item));
            lvl.setBlock(pos, state.setValue(COLOR, col), 3);
            return ItemInteractionResult.sidedSuccess(lvl.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default boolean handleIsSignalSource() {
        return true;
    }

    default int handleGetSignal(BlockState state, BlockGetter lvl, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? 15 : 0;
    }
}
