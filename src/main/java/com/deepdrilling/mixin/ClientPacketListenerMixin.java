//package com.deepdrilling.mixin;
//
//import com.deepdrilling.nodes.LootParser;
//import net.minecraft.client.multiplayer.ClientPacketListener;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(ClientPacketListener.class)
//public class ClientPacketListenerMixin {
//
//    @Inject(
//            method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void customPayload(CustomPacketPayload payload, CallbackInfo ci) {
//
//        ResourceLocation id = payload.type().id();
//
//        if (id.equals(LootParser.PACKET_ID)) {
//
//            FriendlyByteBuf buf = new FriendlyByteBuf(payload.data());
//
//            LootParser.receiveFromServer(buf);
//
//            ci.cancel();
//        }
//    }
//}
