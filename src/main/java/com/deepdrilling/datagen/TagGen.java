package com.deepdrilling.datagen;

import com.deepdrilling.DeepDrilling;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.simibubi.create.foundation.data.TagGen.CreateTagsProvider;

public class TagGen {
    public static void addGenerators() {
        DeepDrilling.registrate().addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::genBlockTags);
        DeepDrilling.registrate().addDataGenerator(ProviderType.ITEM_TAGS, TagGen::genItemTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        CreateTagsProvider<Block> prov = new CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        CreateTagsProvider<Item> prov = new CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);
    }
}
