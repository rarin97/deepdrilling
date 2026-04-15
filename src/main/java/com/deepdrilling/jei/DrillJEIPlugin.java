package com.deepdrilling.jei;

import com.deepdrilling.DeepDrilling;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.nodes.LootParser;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class DrillJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return DeepDrilling.asResource("ore_node");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new OreNodeCategory());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(FakeOreNodeRecipe.RECIPE_TYPE,
                LootParser.knownTables.entrySet().stream()
                        .map(entry -> new FakeOreNodeRecipe(entry.getKey(), entry.getValue()))
                        .toList()
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        DDrillHeads.knownDrillHeads.forEach(drillBlockEntry ->
                registration.addRecipeCatalyst(drillBlockEntry.asStack(), FakeOreNodeRecipe.RECIPE_TYPE)
        );
    }
}
