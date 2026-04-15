package com.deepdrilling.blocks;

import com.simibubi.create.api.contraption.ContraptionMovementSetting;
import net.minecraft.world.level.block.Block;

public class OreNodeBlock extends Block implements ContraptionMovementSetting.MovementSettingProvider {
    public OreNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ContraptionMovementSetting getContraptionMovementSetting() {
        return ContraptionMovementSetting.UNMOVABLE;
    }
}
