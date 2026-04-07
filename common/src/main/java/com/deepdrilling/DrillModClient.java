package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.ponders.DDPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;

public class DrillModClient {
    public static void init() {
        DrillMod.LOGGER.info("Client initialization!");
        PonderIndex.addPlugin(new DDPonderPlugin());
        DDrillHeads.init();
        DPartialModels.init();
    }
}
