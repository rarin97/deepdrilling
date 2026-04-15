package com.deepdrilling;

import com.deepdrilling.blocks.DrillHeadBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BlockstateHelper {
    public static <T extends Block> void existingFilePillar(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
        ModelFile model = p.models().getExistingFile(p.modLoc(location));
        p.getVariantBuilder(c.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(model).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(model).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
    }

    public static <T extends Block> void existingFileFacing(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
        p.directionalBlock(c.get(), p.models().getExistingFile(p.modLoc(location)));
    }

    public static void makeDrillModelThatDoesWeirdStuff(DataGenContext<Block, DrillHeadBlock> ctx,
                                                        RegistrateBlockstateProvider prov,
                                                        String blockID, String headTexture) {
        // empty model file with just particles, for the block rendering
        prov.getVariantBuilder(ctx.get()).forAllStates(
                state -> ConfiguredModel.builder()
                        .modelFile(prov.models().getBuilder("block/" + blockID + "_block")
                                .texture("particle", headTexture)).build()
        );
        // model file with actuall stuff, for the partial model
        ConfiguredModel.builder()
                .modelFile(prov.models()
                        .withExistingParent(blockID, prov.modLoc("block/base_drill_head"))
                        .texture("0", headTexture));
    }
}
