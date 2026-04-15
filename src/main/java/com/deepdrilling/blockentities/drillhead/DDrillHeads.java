package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.BlockstateHelper;
import com.deepdrilling.DCreativeTabs;
import com.deepdrilling.DeepDrilling;
import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;

import static com.deepdrilling.DeepDrilling.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class DDrillHeads {

    static {
        REGISTRATE.setCreativeTab(DCreativeTabs.MAIN);
    }

    public static final List<BlockEntry<DrillHeadBlock>> knownDrillHeads = new ArrayList<>();

    // only this stuff should need to be touched
    public static final BlockEntry<DrillHeadBlock> ANDESITE, BRASS, COPPER;
    static {
        ANDESITE = createDrillHead("andesite_drill_head", "minecraft:block/andesite", "Andesite Drill Head",
                100, 4, 1, 1.5, 1, 0.5);
        BRASS = createDrillHead("brass_drill_head", "create:block/brass_block", "Brass Drill Head",
                500, 8, 0.8, 0, 1.5, 1.5);
        COPPER = createDrillHead("copper_drill_head", "minecraft:block/copper_block", "Copper Drill Head",
                200, 4, 0.5, 2, 0, 0);
    }

    public static void createCopyHeadDataDrop(RegistrateBlockLootTables tables, Block block) {
        tables.add(block, LootTable.lootTable().withPool(
                (tables.applyExplosionCondition(block, LootPool.lootPool())).setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(block).apply(CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Damage", "Damage")
                                        .copy("Unbreakable", "Unbreakable")
                                        .copy("Enchantments", "Enchantments")
                        )))
        );
    }

    public static BlockEntityEntry<DrillHeadBE> DRILL_HEAD_BE;
    public static BlockEntry<DrillHeadBlock> createDrillHead(String blockID, String headTexture, String name,
                                                             double durability, double stress, double miningTimeModifier,
                                                             double earthWeight, double commonWeight, double rareWeight) {
        BlockEntry<DrillHeadBlock> block = DeepDrilling.REGISTRATE
                .block(blockID, DrillHeadBlock::new)
                .initialProperties(SharedProperties::stone)
                .addLayer(() -> RenderType::cutout)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(pickaxeOnly())
                .onRegister(BE -> BlockStressValues.IMPACTS.register(BE, () -> stress))
                .transform(DrillHeadStats.setDurability(durability))
                .transform(DrillHeadStats.setSpeedModifier(miningTimeModifier))
                .transform(DrillHeadStats.setLootWeightMultiplier(earthWeight, commonWeight, rareWeight))
                .loot(DDrillHeads::createCopyHeadDataDrop)
                .blockstate((c, p) -> BlockstateHelper.makeDrillModelThatDoesWeirdStuff(c, p, blockID, headTexture))
                .lang(name)
                .item(DrillHeadItem::new)
                .tag(ItemTags.DURABILITY_ENCHANTABLE)
                .tag(ItemTags.MINING_ENCHANTABLE)
                .tag(ItemTags.MINING_LOOT_ENCHANTABLE)
                .properties(p -> p.durability((int) durability))
                .build()
                .register();
        knownDrillHeads.add(block);
        return block;
    }

    public static void registerBlockEntity() {
        CreateBlockEntityBuilder<DrillHeadBE, CreateRegistrate> builder = DeepDrilling.REGISTRATE
                .blockEntity("drill_head", DrillHeadBE::new)
                .visual(() -> DrillHeadVisual::new, false);

        for (BlockEntry<DrillHeadBlock> drillHeadBlock : knownDrillHeads) {
            builder.validBlocks(drillHeadBlock);
        }

        DRILL_HEAD_BE = builder.renderer(() -> DrillHeadRenderer::new)
                .register();
    }

    public static void init() {
        DeepDrilling.LOGGER.info("Registred {} drill heads", knownDrillHeads.size());
    }
}
