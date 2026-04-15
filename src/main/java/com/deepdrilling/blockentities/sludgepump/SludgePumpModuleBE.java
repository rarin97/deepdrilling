package com.deepdrilling.blockentities.sludgepump;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.DeepDrilling;
import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import com.deepdrilling.blockentities.module.ModuleBE;
import com.deepdrilling.DFluids;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Set;

public class SludgePumpModuleBE extends ModuleBE {
    public SludgePumpModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public Set<ResourceLocation> getMutuallyExclusiveNames() {
        return Set.of(DeepDrilling.asResource("sludge_pump"));
    }

    private static final Modifier<Boolean, SludgePumpModuleBE> MODIFIER_FUNCTION = ModifierTypes.CAN_FUNCTION.create(
            ((core, head, be, base, prev) -> prev && be.canRun()), 0
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> (base + prev) / 8), -100
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            ((core, head, be, base, prev) -> prev * 0.5), -100
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_FORTUNE = ModifierTypes.FORTUNE.create(
            ((core, head, be, base, prev) -> prev * 0.55), -100
    );
    private static final Modifier<DrillHeadStats.WeightMultipliers, SludgePumpModuleBE> MODIFIER_WEIGHTS = ModifierTypes.RESOURCE_WEIGHT.create(
            ((core, head, be, base, prev) -> prev.mul(new DrillHeadStats.WeightMultipliers(1.5, 0.2, 0.2))), -100
    );

    private static final List<Modifier> MODIFIERS = List.of(MODIFIER_FUNCTION, MODIFIER_SPEED, MODIFIER_DAMAGE, MODIFIER_FORTUNE, MODIFIER_WEIGHTS);

    @Override
    public List<Modifier> getModifiers() {
        return MODIFIERS;
    }

    SmartFluidTankBehaviour tank;

    private boolean canRun() {
        return tank.getPrimaryHandler().getFluidAmount() < tank.getPrimaryHandler().getCapacity();
    }

    @Override
    public void blockBroken(DrillCoreBE drill) {
        Fluid sludge = DFluids.SLUDGE.getSource();
        tank.getPrimaryHandler().fill(new FluidStack(sludge, drill.getLevel().random.nextInt(100, 200)),
                IFluidHandler.FluidAction.EXECUTE);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                DBlockEntities.SLUDGE_PUMP.get(),
                (be, context) -> be.tank.getCapability()
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean addToGoggleTooltip = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (addToGoggleTooltip) {
//            if (!canRun()) {
//                tooltip.add(Component.translatable("deepdrilling.goggle.sludge_pump.backed_up").withStyle(ChatFormatting.RED));
//            return true;
//            }
            return containedFluidTooltip(tooltip, isPlayerSneaking,
                    level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null));
        }
        return true;


    }
}
