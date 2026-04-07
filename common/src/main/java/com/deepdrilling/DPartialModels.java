package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class DPartialModels {
    public static final Map<ResourceLocation, PartialModel> drillHeadModels = new HashMap<>();

    public static final PartialModel
            DRILL_CORE_SHAFT = block("drill_core/partial");

    public static PartialModel block(String path) {
        return PartialModel.of(DrillMod.id("block/" + path));
    }

    public static PartialModel getDrillHeadModel(ResourceLocation blockID) {
        if (!drillHeadModels.containsKey(blockID)) {
            drillHeadModels.put(blockID, PartialModel.of(
                    new ResourceLocation(blockID.getNamespace(), "block/" + blockID.getPath())));
        }
        return drillHeadModels.get(blockID);
    }
    public static PartialModel getDrillHeadModel(BlockState state) {
        return getDrillHeadModel(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
    }

    public static boolean init() {
        DrillMod.LOGGER.info("Loading partial models for {} drill heads", DDrillHeads.knownDrillHeads.size());
        DDrillHeads.knownDrillHeads.forEach(be -> getDrillHeadModel(be.getId()));
        return true;
    }
}
