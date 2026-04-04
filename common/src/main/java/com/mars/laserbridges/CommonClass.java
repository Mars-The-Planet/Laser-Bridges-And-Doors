package com.mars.laserbridges;

import com.mars.deimos.config.DeimosConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
                ServerLevel serverLevel = blockSource.level();
                Direction direction = (Direction)blockSource.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = blockSource.pos().relative(direction);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if(block.equals(BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME))) ||
                        block.equals(BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME)))){
                    //Item item = stack.getItem();
                    int col = stack.get(DataComponents.DYE).getId();
                    //int col = ((DyeItem) item).getDyeColor().getId();
                    serverLevel.setBlock(blockPos, blockState.setValue(COLOR, col), 2);
                    stack.shrink(1);
                    serverLevel.gameEvent((Entity)null, GameEvent.BLOCK_CHANGE, blockPos);
                    this.setSuccess(true);
                    return stack;
                }
                return super.execute(blockSource, stack);
            }
        };

        DispenserBlock.registerBehavior(Items.WHITE_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.ORANGE_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.MAGENTA_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.LIGHT_BLUE_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.YELLOW_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.LIME_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.PINK_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.GRAY_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.LIGHT_GRAY_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.CYAN_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.PURPLE_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.BLUE_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.BROWN_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.GREEN_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.RED_DYE, dyeBehavior);
        DispenserBlock.registerBehavior(Items.BLACK_DYE, dyeBehavior);
    }
}
