package com.mars.laserbridges;

import com.google.common.collect.Lists;
import com.mars.deimos.config.DeimosConfig;
import com.mars.deimos.datagen.DeimosRecipeGenerator;
import com.mars.laserbridges.blocks.BridgeSourceBlock;
import com.mars.laserbridges.blocks.FenceSourceBlock;
import com.mars.laserbridges.blocks.ISourceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import static com.mars.laserbridges.Constants.*;
import static com.mars.laserbridges.blocks.ISourceBlock.COLOR;

public class CommonClass {

    public static void init() {

        ModRegistry.RegisterSetup();

        DeimosConfig.init(MOD_ID, LaserBridgesConfig.class);

        // Dispensers dying laser sources
        DefaultDispenseItemBehavior dyeBehavior = new OptionalDispenseItemBehavior() {
            protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                ServerLevel serverLevel = blockSource.level();
                Direction direction = (Direction)blockSource.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = blockSource.pos().relative(direction);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                Block block = blockState.getBlock();
                String blockDesc = block.getDescriptionId();
                if (blockDesc.equals("block."+MOD_ID+"."+BRIDGE_SOURCE_BLOCK_NAME) || blockDesc.equals("block."+MOD_ID+"."+FENCE_SOURCE_BLOCK_NAME)) {
                    int col = stack.get(DataComponents.DYE).getId();
                    BlockState newBlockState = blockState.setValue(BridgeSourceBlock.COLOR, col);
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
                    return stack;
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

        // Recipe generation
        String attachedBridgeID = Identifier.fromNamespaceAndPath(MOD_ID, BRIDGE_ATTACHED_SOURCE_BLOCK_NAME).toString();
        String attachedFenceID = Identifier.fromNamespaceAndPath(MOD_ID, FENCE_ATTACHED_SOURCE_BLOCK_NAME).toString();
        String bridgeID = Identifier.fromNamespaceAndPath(MOD_ID, BRIDGE_SOURCE_BLOCK_NAME).toString();
        String fenceID = Identifier.fromNamespaceAndPath(MOD_ID, FENCE_SOURCE_BLOCK_NAME).toString();

        DeimosRecipeGenerator.createShapedRecipeJson(
                Lists.newArrayList("minecraft:iron_ingot", "minecraft:glass", "minecraft:end_crystal"),
                Lists.newArrayList("IGI", "IEI", "III"), attachedBridgeID);

        DeimosRecipeGenerator.createShapedRecipeJson(
                Lists.newArrayList("minecraft:iron_ingot", "minecraft:end_crystal", "minecraft:glass"),
                Lists.newArrayList("III", "IEG", "III"), attachedFenceID);

        DeimosRecipeGenerator.createItemConvertorJson(attachedBridgeID, attachedFenceID, 1);
        DeimosRecipeGenerator.createItemConvertorJson(attachedFenceID, attachedBridgeID, 1);

        DeimosRecipeGenerator.createShapedRecipeJson(
                Lists.newArrayList(attachedBridgeID, "minecraft:iron_ingot"),
                Lists.newArrayList(" B ", "III", "III"), bridgeID);

        DeimosRecipeGenerator.createShapedRecipeJson(
                Lists.newArrayList("minecraft:iron_ingot", attachedFenceID),
                Lists.newArrayList("II ", "IIF", "II "), fenceID);

        DeimosRecipeGenerator.createItemConvertorJson(bridgeID, fenceID, 1);
        DeimosRecipeGenerator.createItemConvertorJson(fenceID, bridgeID, 1);
    }
}
