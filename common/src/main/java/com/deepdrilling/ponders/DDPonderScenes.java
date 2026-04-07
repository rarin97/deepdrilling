package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class DDPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        List<BlockEntry<? extends Block>> drills = new ArrayList<>(DDrillHeads.knownDrillHeads);
        drills.add(DBlocks.DRILL);
        HELPER.forComponents(drills)
                .addStoryBoard("drill/drilling", DrillingScenes::drillBasics);

        HELPER.forComponents(DBlocks.DRILL, DBlocks.BLANK_MODULE, DBlocks.COLLECTOR, DBlocks.DRILL_OVERCLOCK, DBlocks.SLUDGE_PUMP)
                .addStoryBoard("drill/modules", DrillingScenes::drillModules);
    }
}
