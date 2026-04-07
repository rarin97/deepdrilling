package com.deepdrilling.fabric.datagen;

import com.deepdrilling.DrillMod;
import com.deepdrilling.fluid.fabric.FluidsImpl;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.world.level.block.Blocks;

public class MixingRecipes extends MixingRecipeGen {
    // we love fabrics unique fluid api it is very cool
    GeneratedRecipe
            SLUDGE_NETHERRACK = create(DrillMod.id("sludge_netherrack"), b -> b
            .require(FluidsImpl.getSludge(), FluidConstants.BUCKET / 4)
            .require(AllItems.CINDER_FLOUR)
            .output(Blocks.NETHERRACK)
            .requiresHeat(HeatCondition.HEATED));

    public MixingRecipes(FabricDataOutput output) {
        super(output, DrillMod.MOD_ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
