package com.deepdrilling.jei;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.Color;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

public class OreNodeCategory implements IRecipeCategory<FakeOreNodeRecipe> {
    private static final Component TITLE = Component.translatable("deepdrilling.recipe.ore_node");

    AnimatedDrill drill = new AnimatedDrill();
    IDrawable background;
    IDrawable icon;

    public OreNodeCategory() {
        this.background = new EmptyBackground(177, 100);
        this.icon = new ItemIcon(DDrillHeads.ANDESITE::asStack);
    }

    @Override
    public RecipeType<FakeOreNodeRecipe> getRecipeType() {
        return FakeOreNodeRecipe.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FakeOreNodeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 44)
                .setBackground(getRenderedSlot(0), -1, -1)
                .addItemStack(recipe.nodeBlock);

        int x = 70;
        for (Item item : recipe.lootEntry.earth()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 14)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }

        x = 70;
        for (Item item : recipe.lootEntry.common()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 44)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }

        x = 70;
        for (Item item : recipe.lootEntry.rare()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 74)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }
    }

    @Override
    public void draw(FakeOreNodeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.nodeBlock.getItem() instanceof BlockItem blockItem) {
            drill.draw(guiGraphics, 30, 60, blockItem.getBlock().defaultBlockState());
        } else {
            drill.draw(guiGraphics, 30, 60);
        }

        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.earth"), 70, 4, Color.WHITE.getRGB());
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.common"), 70, 34, Color.WHITE.getRGB());
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.rare"), 70, 64, Color.WHITE.getRGB());
    }

    @Override
    public ResourceLocation getRegistryName(FakeOreNodeRecipe recipe) {
        return recipe.getId();
    }
}
