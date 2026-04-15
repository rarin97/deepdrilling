package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.CollectorModuleBE;
import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CollectorModuleBlock extends ModuleBlock<CollectorModuleBE> {
    public CollectorModuleBlock(Properties properties) {
        super(properties);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CollectorModuleBE) {
                Containers.dropContents(level, pos, (CollectorModuleBE)blockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (AllItems.WRENCH.isIn(player.getItemInHand(hand)))
            return super.use(state, level, pos, player, hand, hit);

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        withBlockEntityDo(level, pos, collector -> collector.givePlayerItems(player, level));

        return super.use(state, level, pos, player, hand, hit);
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
