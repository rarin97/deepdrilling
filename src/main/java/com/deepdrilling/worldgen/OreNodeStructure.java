package com.deepdrilling.worldgen;

import com.deepdrilling.DeepDrilling;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class OreNodeStructure extends Structure {
    public static final MapCodec<OreNodeStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    settingsCodec(instance),
                    Data.CODEC.fieldOf("ore_node").forGetter(node -> node.data)
            ).apply(instance, OreNodeStructure::new)
    );

    public OreNodeStructure(StructureSettings settings, Data data) {
        super(settings);
        this.data = data;
    }

    private final Data data;

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos pos = context.chunkPos().getBlockAt(
                context.random().nextInt(16),
                context.heightAccessor().getMinBuildHeight(),
                context.random().nextInt(16)
        );
        OreNodePiece piece = new OreNodePiece(pos.getX(), pos.getY(), pos.getZ(), data);
        return Optional.of(new GenerationStub(
                pos,
                builder -> builder.addPiece(piece)
        ));
    }

    @Override
    public StructureType<?> type() {
        return getStructureType();
    }

    record Data(ResourceLocation node, List<ResourceLocation> ores, List<ResourceLocation> layers) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ResourceLocation.CODEC.fieldOf("nodeID").forGetter(Data::node),
                        Codec.list(ResourceLocation.CODEC).fieldOf("ores").forGetter(Data::ores),
                        Codec.list(ResourceLocation.CODEC).fieldOf("layers").forGetter(Data::layers)
                ).apply(instance, Data::new)
        );
    }

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, DeepDrilling.ID);
    private static final DeferredHolder<StructureType<?>, StructureType<OreNodeStructure>> STRUCTURE_TYPE =
            STRUCTURE_TYPE_REGISTRY.register("ore_node", () -> () -> OreNodeStructure.CODEC);

    private static final DeferredRegister<StructurePieceType> PIECE_TYPE_REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, DeepDrilling.ID);
    private static final DeferredHolder<StructurePieceType, StructurePieceType> PIECE_TYPE =
            PIECE_TYPE_REGISTRY.register("ore_node", () -> OreNodePiece::new);

    public static StructureType<OreNodeStructure> getStructureType() {
        return STRUCTURE_TYPE.get();
    }

    public static StructurePieceType getPieceType() {
        return PIECE_TYPE.get();
    }

    public static void init() {
        STRUCTURE_TYPE_REGISTRY.register(DeepDrilling.getBus());
        PIECE_TYPE_REGISTRY.register(DeepDrilling.getBus());
    }
}
