package com.deepdrilling;

import com.deepdrilling.blockentities.CollectorModuleBE;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blockentities.sludgepump.SludgePumpModuleBE;
import com.deepdrilling.nodes.NodeLootPayload;
import com.deepdrilling.worldgen.OreNodeStructure;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DeepDrilling.ID)
public class DeepDrilling {
    public static final String ID = "deepdrilling";
    public static final String NAME = "DeepDrilling";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static IEventBus modEventBus;

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                    .andThen(TooltipModifier.mapNull(DrillHeadTooltips.create(item))));


    public DeepDrilling(IEventBus eventBus, ModContainer modContainer) {
        modEventBus = eventBus;
        REGISTRATE.registerEventListeners(eventBus);

        DCreativeTabs.init(eventBus);
        DDrillHeads.registerBlockEntity();
        DDrillHeads.init();
        DBlocks.init();
        DBlockEntities.init();
        DItems.init();
        OreNodeStructure.init();
        DFluids.init();

        eventBus.addListener(CollectorModuleBE::registerCapabilities);
        eventBus.addListener(SludgePumpModuleBE::registerCapabilities);

        eventBus.addListener(NodeLootPayload::registerPayloadHandlers);
        NeoForge.EVENT_BUS.register(DrillEvents.class);

        eventBus.addListener(DeepDrillingDatagen::gatherDataHighPriority);
        eventBus.addListener(DeepDrillingDatagen::gatherData);
    }

    public static IEventBus getBus() {
        return modEventBus;
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
