package com.mars.laserbridges;

import com.mars.deimos.config.DeimosConfig;
import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.blocks.BridgeSourceBlock.COLOR;

public class CommonClass {
    public static void init() {
        DeimosConfig.init(MOD_ID, LasersConfig.class);

        DefaultDispenseItemBehavior dyeBehavior = new OptionalDispenseItemBehavior() {
            protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                System.out.println("EXECUTE DYE");
                ServerLevel serverLevel = blockSource.level();
                Direction direction = (Direction)blockSource.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = blockSource.pos().relative(direction);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                Block block = blockState.getBlock();
                String blockDesc = block.getDescriptionId();
                if (blockDesc.equals("block."+MOD_ID+"."+BRIDGE_SOURCE_BLOCK_NAME) || blockDesc.equals("block."+MOD_ID+"."+FENCE_SOURCE_BLOCK_NAME)) {
                    System.out.println("IN");
                    int col = stack.get(DataComponents.DYE).getId();
                    BlockState newBlockState = blockState.setValue(COLOR, col);
                    serverLevel.setBlock(blockPos, newBlockState, 3);
                    stack.shrink(1);
                    serverLevel.gameEvent((Entity)null, GameEvent.BLOCK_CHANGE, blockPos);
                    this.setSuccess(true);

                    int redstonePower = serverLevel.getBestNeighborSignal(blockPos);
                    if (blockDesc.equals("block."+MOD_ID+"."+BRIDGE_SOURCE_BLOCK_NAME)) {
                        ((BridgeSourceBlock)block).generateBridge(serverLevel, redstonePower, blockPos, newBlockState);
                    }
                    else {
                        ((FenceSourceBlock)block).generateBridge(serverLevel, redstonePower, blockPos, newBlockState);
                    }
                    System.out.println("COLOUR: " + col);
                    return stack;
                }
                else{
                    System.out.println("WTF: " + block + ", " + block.defaultBlockState() + ", " + block.getDescriptionId());
                }
                return super.execute(blockSource, stack);
            }
        };

        DispenserBlock.registerBehavior(Items.DYE.white(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.orange(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.magenta(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.lightBlue(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.yellow(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.lime(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.pink(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.gray(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.lightGray(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.cyan(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.purple(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.blue(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.brown(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.green(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.red(), dyeBehavior);
        DispenserBlock.registerBehavior(Items.DYE.black(), dyeBehavior);
    }
}
