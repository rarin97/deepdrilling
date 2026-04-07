package com.deepdrilling.blockentities.drillhead;

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

public class DrillHeadVisual extends KineticBlockEntityVisual<DrillHeadBE> {

    protected final RotatingInstance drill;
    final Direction direction;
    private final Direction opposite;

    public DrillHeadVisual(VisualizationContext context, DrillHeadBE blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        direction = blockState.getValue(FACING);
        opposite = direction.getOpposite();

        drill =  instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(DPartialModels.getDrillHeadModel(blockEntity.getBlockState())))
                .createInstance();

        drill.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.NORTH, opposite)
                .setChanged();
    }

    @Override
    public void update(float pt) {
        drill.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(drill);
    }

    @Override
    protected void _delete() {
        drill.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(drill);
    }
}

