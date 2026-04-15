package com.deepdrilling.jei;

import com.deepdrilling.DeepDrilling;
import com.deepdrilling.nodes.LootParser;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FakeOreNodeRecipe {

    public final ItemStack nodeBlock;
    public final LootParser.LootEntry lootEntry;
    private final ResourceLocation id;

    public FakeOreNodeRecipe(Block nodeBlock, LootParser.LootEntry lootEntry) {
        this.nodeBlock = nodeBlock.asItem().getDefaultInstance();
        this.lootEntry = lootEntry;
        this.id = DeepDrilling.asResource(
                "ore_node/" + nodeBlock.builtInRegistryHolder().key().location().getPath()
        );
    }

    public static final RecipeType<FakeOreNodeRecipe> RECIPE_TYPE =
            RecipeType.create(DeepDrilling.ID, "fake_ore_node_recipe", FakeOreNodeRecipe.class);

    public ResourceLocation getId() {
        return id;
    }
}
