package com.deepdrilling.blockentities.overclock;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;

public class OverclockModuleVisual extends SingleAxisRotatingVisual<OverclockModuleBE> {
    public OverclockModuleVisual(VisualizationContext context, OverclockModuleBE blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.COGWHEEL));
    }
}
