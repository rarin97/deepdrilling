package com.deepdrilling;

import com.deepdrilling.blocks.*;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.deepdrilling.DeepDrilling.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

/**
 * @see com.deepdrilling.blockentities.drillhead.DDrillHeads
 */
public class DBlocks {

	static {
		REGISTRATE.setCreativeTab(DCreativeTabs.MAIN);
	}

	public static final BlockEntry<DrillCore> DRILL = REGISTRATE
			.block("drill_core", DrillCore::new)
			.addLayer(() -> RenderType::cutout)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.lang("Drill Core")
			//.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(p.modLoc("block/drill_core/block"))))
			.blockstate((c, p) -> BlockstateHelper.existingFileFacing(c, p, "block/drill_core/block"))
			.onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 16))
			.item()
//			.recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
//					.pattern("IHI")
//					.pattern("HGH")
//					.pattern("IHI")
//					.define('I', AllItems.PRECISION_MECHANISM)
//					.define('H', AllItems.ELECTRON_TUBE)
//					.define('G', AllBlocks.FLYWHEEL)
//					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(AllItems.PRECISION_MECHANISM))
//					.save(p))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<BlankModuleBlock> BLANK_MODULE = REGISTRATE
			.block("blank_module", BlankModuleBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.lang("Blank Module")
			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/blank_module/block"))
			.item()
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
					.requires(AllBlocks.SHAFT)
					.requires(AllBlocks.ANDESITE_CASING)
					.requires(AllItems.IRON_SHEET)
					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(DRILL))
					.save(p))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<CollectorModuleBlock> COLLECTOR = REGISTRATE
			.block("collection_filter", CollectorModuleBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.lang("Collection Filter")
			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/collection_filter/block"))
			.onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 2))
			.item()
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
					.requires(BLANK_MODULE)
					.requires(AllBlocks.ANDESITE_FUNNEL)
					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
					.save(p))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<OverclockModuleBlock> DRILL_OVERCLOCK = REGISTRATE
			.block("drill_overclock", OverclockModuleBlock::new)
			.initialProperties(SharedProperties::stone)
			.addLayer(() -> RenderType::cutout)
			.lang("Drill Overclock")
			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/drill_overclock/block"))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(pickaxeOnly())
			.onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 8))
			.item()
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
					.requires(BLANK_MODULE)
					.requires(AllBlocks.COGWHEEL)
					.requires(AllBlocks.BRASS_CASING)
					.requires(AllItems.PRECISION_MECHANISM)
					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
					.save(p))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<SludgePumpModuleBlock> SLUDGE_PUMP = REGISTRATE
			.block("sludge_pump", SludgePumpModuleBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.lang("Sludge Pump")
			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/sludge_pump/block"))
			.transform(axeOrPickaxe())
			.onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 4))
			.item()
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
					.requires(BLANK_MODULE)
					.requires(AllBlocks.FLUID_TANK)
					.requires(AllBlocks.COPPER_CASING)
					.requires(AllBlocks.MECHANICAL_PUMP)
					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
					.save(p))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<OreNodeBlock>

	CRIMSITE_NODE = REGISTRATE
			.block("crimsite_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			//.loot((p, b) -> {})
			.item()
			.build()
			.register(),
	ASURINE_NODE = REGISTRATE
			.block("asurine_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			//.loot((p, b) -> {})
			.item()
			.build()
			.register(),
	OCHRUM_NODE = REGISTRATE
			.block("ochrum_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			//.loot((p, b) -> {})
			.item()
			.build()
			.register(),
	VERIDIUM_NODE = REGISTRATE
			.block("veridium_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			//.loot((p, b) -> {})
			.item()
			.build()
			.register();

	public static void init() {
		DeepDrilling.LOGGER.info("Registering blocks for " + DeepDrilling.NAME);
	}
}
