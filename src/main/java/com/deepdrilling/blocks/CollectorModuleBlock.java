package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.CollectorModuleBE;
import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CollectorModuleBlock extends ModuleBlock<CollectorModuleBE> {
    public CollectorModuleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (AllItems.WRENCH.isIn(player.getItemInHand(hand)))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        withBlockEntityDo(level, pos, collector -> collector.givePlayerItems(player, level));

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public Class<CollectorModuleBE> getBlockEntityClass() {
        return CollectorModuleBE.class;
    }

    @Override
    public BlockEntityType<? extends CollectorModuleBE> getBlockEntityType() {
        return DBlockEntities.COLLECTOR.get();
    }
}
