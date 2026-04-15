package com.deepdrilling.blockentities.drillcore;

import com.deepdrilling.DPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class DrillCoreVisual extends KineticBlockEntityVisual<DrillCoreBE> {

    protected final RotatingInstance shaft;
    final Direction direction;
    private final Direction opposite;

    public DrillCoreVisual(VisualizationContext context, DrillCoreBE blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);
        opposite = direction.getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING,  Models.partial(DPartialModels.DRILL_CORE_SHAFT))
                .createInstance();

        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.NORTH, opposite)
                .setChanged();
    }

    @Override
    public void update(float pt) {
        shaft.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(shaft);
    }

    @Override
    protected void _delete() {
        shaft.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(shaft);
    }
}
