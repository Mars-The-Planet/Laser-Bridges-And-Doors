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
                //System.out.println("EXECUTE DYE");
                ServerLevel serverLevel = blockSource.level();
                Direction direction = (Direction)blockSource.state().getValue(DispenserBlock.FACING);
                BlockPos blockPos = blockSource.pos().relative(direction);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                Block block = blockState.getBlock();
                String blockDesc = block.getDescriptionId();
                if (blockDesc.equals("block."+MOD_ID+"."+BRIDGE_SOURCE_BLOCK_NAME) || blockDesc.equals("block."+MOD_ID+"."+FENCE_SOURCE_BLOCK_NAME)) {
                    //System.out.println("IN");
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
                    //System.out.println("COLOUR: " + col);
                    return stack;
                }
                else{
                    //System.out.println("WTF: " + block + ", " + block.defaultBlockState() + ", " + block.getDescriptionId());
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
