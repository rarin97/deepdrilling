package com.deepdrilling;

import com.deepdrilling.blockentities.BlankModuleBE;
import com.deepdrilling.blockentities.CollectorModuleBE;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.drillcore.DrillCoreRenderer;
import com.deepdrilling.blockentities.drillcore.DrillCoreVisual;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blockentities.overclock.OverclockModuleBE;
import com.deepdrilling.blockentities.overclock.OverclockModuleRenderer;
import com.deepdrilling.blockentities.overclock.OverclockModuleVisual;
import com.deepdrilling.blockentities.sludgepump.SludgePumpModuleBE;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class DBlockEntities {
    /**
     * for drill heads
     * @see DDrillHeads
     */
    public static final BlockEntityEntry<DrillCoreBE> DRILL_CORE = DeepDrilling.REGISTRATE
            .blockEntity("deep_drill", DrillCoreBE::new)
            .visual(() -> DrillCoreVisual::new, false)
            .validBlocks(DBlocks.DRILL)
            .renderer(() -> DrillCoreRenderer::new)
            .register();

    public static final BlockEntityEntry<BlankModuleBE> BLANK_MODULE = DeepDrilling.REGISTRATE
            .blockEntity("blank_module", BlankModuleBE::new)
            .visual(() -> ShaftVisual::new)
            .validBlocks(DBlocks.BLANK_MODULE)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<CollectorModuleBE> COLLECTOR = DeepDrilling.REGISTRATE
            .blockEntity("collection_filter", CollectorModuleBE::new)
            .visual(() -> ShaftVisual::new)
            .validBlocks(DBlocks.COLLECTOR)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<OverclockModuleBE> DRILL_OVERCLOCK = DeepDrilling.REGISTRATE
            .blockEntity("drill_overclock", OverclockModuleBE::new)
            .visual(() -> OverclockModuleVisual::new, false)
            .validBlocks(DBlocks.DRILL_OVERCLOCK)
            .renderer(() -> OverclockModuleRenderer::new)
            .register();

    public static final BlockEntityEntry<SludgePumpModuleBE> SLUDGE_PUMP = DeepDrilling.REGISTRATE
            .blockEntity("sludge_pump", SludgePumpModuleBE::new)
            .visual(() -> ShaftVisual::new)
            .validBlocks(DBlocks.SLUDGE_PUMP)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static void init() {
        DeepDrilling.LOGGER.info("Registering block entities for " + DeepDrilling.NAME);
        DDrillHeads.init();
    }
}
