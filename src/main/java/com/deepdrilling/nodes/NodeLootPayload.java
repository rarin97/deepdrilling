package com.deepdrilling.nodes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import static com.deepdrilling.DeepDrilling.ID;

public record NodeLootPayload(FriendlyByteBuf buf) implements CustomPacketPayload {

    public static final Type<NodeLootPayload> TYPE =
            new Type<>(LootParser.PACKET_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, NodeLootPayload> CODEC =
            StreamCodec.of(
                    (buffer, payload) -> buffer.writeBytes(payload.buf.copy()),
                    buffer -> new NodeLootPayload(new FriendlyByteBuf(buffer.readBytes(buffer.readableBytes())))
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar(ID)
                .playToClient(NodeLootPayload.TYPE, NodeLootPayload.CODEC, (payload, context) ->
                        context.enqueueWork(() -> LootParser.receiveFromServer(payload.buf()))
                );
    }
}
