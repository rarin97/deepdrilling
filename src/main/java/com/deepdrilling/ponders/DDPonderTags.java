package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DeepDrilling;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class DDPonderTags {

    public static final ResourceLocation

            DRILLING = DeepDrilling.asResource("drilling");


    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        List<BlockEntry<? extends Block>> drills = new ArrayList<>(DDrillHeads.knownDrillHeads);
        drills.add(DBlocks.DRILL);

        HELPER.registerTag(DRILLING)
                .item(DDrillHeads.ANDESITE.get(), true, false)
                .title("Deep Drilling")
                .description("Components related to extracting resources from deep within the earth")
                .addToIndex()
                .register();

        HELPER.addToTag(DDPonderTags.DRILLING)
                .add(DDrillHeads.ANDESITE)
                .add(DDrillHeads.BRASS)
                .add(DDrillHeads.COPPER)
                .add(DBlocks.DRILL)
                .add(DBlocks.BLANK_MODULE)
                .add(DBlocks.COLLECTOR)
                .add(DBlocks.DRILL_OVERCLOCK)
                .add(DBlocks.SLUDGE_PUMP)
                .add(DBlocks.ASURINE_NODE)
                .add(DBlocks.CRIMSITE_NODE)
                .add(DBlocks.OCHRUM_NODE)
                .add(DBlocks.VERIDIUM_NODE);
    }
}
