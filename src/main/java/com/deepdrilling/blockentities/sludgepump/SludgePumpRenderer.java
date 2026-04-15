package com.deepdrilling.blockentities.sludgepump;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.fluids.FluidStack;

public class SludgePumpRenderer extends SafeBlockEntityRenderer<SludgePumpModuleBE> {

    public SludgePumpRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(SludgePumpModuleBE be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        SmartFluidTankBehaviour tank = be.tank;
        if (tank == null)
            return;

        SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
        FluidStack fluidStack = primaryTank.getRenderedFluid();
        float level = primaryTank.getFluidLevel()
                .getValue(partialTicks);

        if (!fluidStack.isEmpty() && level != 0) {
            boolean top = fluidStack.getFluid()
                    .getFluidType()
                    .isLighterThanAir();

            level = Math.max(level, 0.175f);
            float min = 2.5f / 16f;
            float max = min + (11 / 16f);
            float yOffset = (11 / 16f) * level;

            ms.pushPose();
            if (!top) ms.translate(0, yOffset, 0);
            else ms.translate(0, max - min, 0);

            NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, min, min - yOffset, min, max, min,
                    max, buffer, ms, light, false, true);

            ms.popPose();
        }
    }
}
