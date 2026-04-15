package com.deepdrilling;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import static com.deepdrilling.DeepDrilling.REGISTRATE;

public class DFluids {

    static {
        REGISTRATE.setCreativeTab(DCreativeTabs.MAIN);
    }

    public static final FluidEntry<BaseFlowingFluid.Flowing> SLUDGE = DeepDrilling.REGISTRATE
            .fluid("sludge",
                    DeepDrilling.asResource("block/sludge_still"),
                    DeepDrilling.asResource("block/sludge_flow"),
                    BaseFlowingFluid.Flowing::new)
            .lang("Sludge")
            .tag(FluidTags.WATER)
            .properties(b -> b.viscosity(2000).density(2000))
            .fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(30).explosionResistance(100))
            .source(BaseFlowingFluid.Source::new)
            .bucket(BucketItem::new)
            .build()
            .register();

    public static Fluid getSludge() {
        return SLUDGE.get();
    }

    public static void init() {}
}
