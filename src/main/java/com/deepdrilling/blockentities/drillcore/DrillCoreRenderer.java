package com.deepdrilling.blockentities.drillcore;

import com.deepdrilling.DPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class DrillCoreRenderer extends KineticBlockEntityRenderer<DrillCoreBE> {
    public DrillCoreRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrillCoreBE be, BlockState state) {
        return CachedBuffers.partialFacing(DPartialModels.DRILL_CORE_SHAFT, state);
    }
}