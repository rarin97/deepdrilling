package com.deepdrilling.datagen;

import com.deepdrilling.DeepDrilling;
import com.deepdrilling.DFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class MixingRecipes extends MixingRecipeGen {

    GeneratedRecipe
    SLUDGE_NETHERRACK = create(DeepDrilling.asResource("sludge_netherrack"), b -> b
            .require(DFluids.SLUDGE.get(), 250)
            .require(AllItems.CINDER_FLOUR)
            .output(Blocks.NETHERRACK)
            .requiresHeat(HeatCondition.HEATED));

    public MixingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, DeepDrilling.ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
