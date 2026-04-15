package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class DDPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        
        HELPER.forComponents(DDrillHeads.ANDESITE, DDrillHeads.BRASS, DDrillHeads.COPPER)
                .addStoryBoard("drill/drilling", DrillingScenes::drillBasics);

        HELPER.forComponents(DDrillHeads.ANDESITE, DDrillHeads.BRASS, DDrillHeads.COPPER, DBlocks.DRILL, DBlocks.BLANK_MODULE, DBlocks.COLLECTOR, DBlocks.DRILL_OVERCLOCK, DBlocks.SLUDGE_PUMP)
                .addStoryBoard("drill/modules", DrillingScenes::drillModules);

        HELPER.forComponents(DBlocks.ASURINE_NODE, DBlocks.CRIMSITE_NODE, DBlocks.OCHRUM_NODE, DBlocks.VERIDIUM_NODE)
                .addStoryBoard("node/biomes", DrillingScenes::nodeBiomes);
    }
}
