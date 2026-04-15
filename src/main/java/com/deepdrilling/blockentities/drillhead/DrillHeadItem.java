package com.deepdrilling.blockentities.drillhead;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DrillHeadItem extends BlockItem {
    public static final List<ResourceKey<Enchantment>> ENCHANTMENTS = List.of(
            Enchantments.UNBREAKING,
            Enchantments.EFFICIENCY,
            Enchantments.FORTUNE,
            Enchantments.MENDING
    );

    public DrillHeadItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return super.getBarWidth(stack);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.unwrapKey()
                .map(key -> key.equals(Enchantments.UNBREAKING) ||
                        key.equals(Enchantments.EFFICIENCY) ||
                        key.equals(Enchantments.FORTUNE) ||
                        key.equals(Enchantments.MENDING))
                .orElse(false);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        ItemEnchantments bookEnchantments = book.getTagEnchantments();

        return bookEnchantments.isEmpty() &&
                bookEnchantments.keySet().stream()
                        .map(Holder::unwrapKey)
                        .allMatch(opt -> opt.map(ENCHANTMENTS::contains).orElse(false));
    }
}
