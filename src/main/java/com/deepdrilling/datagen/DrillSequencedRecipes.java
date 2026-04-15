package com.deepdrilling.datagen;

import com.deepdrilling.DItems;
import com.deepdrilling.DeepDrilling;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class DrillSequencedRecipes extends SequencedAssemblyRecipeGen {
    GeneratedRecipe

    COPPER_DRILL_HEAD = create("copper_drill_head", b -> b.require(AllBlocks.INDUSTRIAL_IRON_BLOCK)
            .transitionTo(DItems.INCOMPLETE_COPPER_DRILL_HEAD)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.COPPER_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)
            .loops(3)
            .addOutput(DDrillHeads.COPPER, 100)),

    BRASS_DRILL_HEAD = create("brass_drill_head", b -> b.require(AllBlocks.INDUSTRIAL_IRON_BLOCK)
            .transitionTo(DItems.INCOMPLETE_BRASS_DRILL_HEAD)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.BRASS_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.STURDY_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)
            .loops(5)
            .addOutput(DDrillHeads.BRASS, 100));

    public DrillSequencedRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, DeepDrilling.ID);
    }
}
