package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.ponders.DDPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = DeepDrilling.ID, dist = Dist.CLIENT)
public class DeepDrillingClient {
    public DeepDrillingClient(IEventBus modEventBus) {
        DeepDrilling.LOGGER.info("Client initialization!");
        DDrillHeads.init();
        DPartialModels.init();
        modEventBus.addListener(DeepDrillingClient::init);
    }

    public static void init(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new DDPonderPlugin());
    }
}
