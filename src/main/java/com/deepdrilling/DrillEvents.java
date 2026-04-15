package com.deepdrilling;

import com.deepdrilling.nodes.LootParser;
import com.deepdrilling.nodes.NodeReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

public class DrillEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void reloadListener(AddReloadListenerEvent event) {
        DeepDrilling.LOGGER.info("Registering reload listener");
        event.addListener(new NodeReloadListener());
        LootParser.invalidate();
    }

    @SubscribeEvent
    public static void syncToPlayer(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            LootParser.sendToPlayer(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(LootParser::sendToPlayer);
        }
    }
}
