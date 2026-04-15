package com.deepdrilling.nodes;

import com.deepdrilling.DeepDrilling;
import com.deepdrilling.mixin.LootTableAccessor;
import com.deepdrilling.mixin.loottable.CompositeEntryBaseMixin;
import com.deepdrilling.mixin.loottable.LootItemAccessor;
import com.deepdrilling.mixin.loottable.LootPoolAccessor;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.*;

public class LootParser {
    public static Map<Block, LootEntry> knownTables = ImmutableMap.of();
    public static boolean hasLoaded = false;

    public static final ResourceLocation PACKET_ID = DeepDrilling.asResource("node_loot");

    public static void invalidate() {
        knownTables = ImmutableMap.of();
        hasLoaded = false;
    }

    public static void parseOreNodes(ReloadableServerRegistries.Holder manager, Map<Block, OreNode> nodes) {
        ImmutableMap.Builder<Block, LootEntry> builder = ImmutableMap.builder();
        for (Map.Entry<Block, OreNode> entry : nodes.entrySet()) {
            builder.put(entry.getKey(), parseOreNode(manager, entry.getValue()));
        }
        knownTables = builder.build();
        hasLoaded = true;
    }

    private static LootEntry parseOreNode(ReloadableServerRegistries.Holder manager, OreNode node) {
        return new LootEntry(
                getItems(manager, node.earthTable),
                getItems(manager, node.commonTable),
                getItems(manager, node.rareTable)
        );
    }

    private static Set<Item> getItems(ReloadableServerRegistries.Holder manager, String tableName) {
        Set<Item> items = new HashSet<>();
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, OreNodes.parseId(tableName));
        LootTable table = manager.getLootTable(key);

        if (table != LootTable.EMPTY) {
            parseTable(table, items);
        } else {
            DeepDrilling.LOGGER.warn("Couldn't find loot table {}", tableName);
        }
        return items;
    }

    private static void parseTable(LootTable table, Set<Item> items) {
        for (LootPool pool : getPools(table)) {
            for (LootPoolEntryContainer entry : getEntries(pool)) {
                parseEntry(entry, items);
            }
        }
    }

    public static Iterable<LootPool> getPools(LootTable table) {
        return ((LootTableAccessor)table).getPools();
    }

    private static void parseEntry(LootPoolEntryContainer entry, Set<Item> items) {
        if (entry instanceof LootItem lootItem) {
            parseLootItem(lootItem, items);
        } else if (entry instanceof CompositeEntryBase compositeEntry) {
            parseCompositeEntry(compositeEntry, items);
        }
    }

    private static void parseLootItem(LootItem lootItem, Set<Item> items) {
        items.add(getItem(lootItem));
    }

    private static void parseCompositeEntry(CompositeEntryBase compositeEntry, Set<Item> items) {
        for (LootPoolEntryContainer child : getChildren(compositeEntry)) {
            parseEntry(child, items);
        }
    }

    public static void sendToPlayer(ServerPlayer player) {
        if (!hasLoaded) {
            DeepDrilling.LOGGER.info("Tried to send unloaded node loot to player! Generating now");
            parseOreNodes(player.server.reloadableRegistries(), OreNodes.getNodeMap());
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(knownTables.size());
        for (Map.Entry<Block, LootEntry> entry : knownTables.entrySet()) {
            buf.writeInt(BuiltInRegistries.BLOCK.getId(entry.getKey()));
            writeItems(buf, entry.getValue().earth);
            writeItems(buf, entry.getValue().common);
            writeItems(buf, entry.getValue().rare);
        }
        FriendlyByteBuf copy = new FriendlyByteBuf(buf.copy());
        player.connection.send(new NodeLootPayload(copy));
    }

    private static void writeItems(FriendlyByteBuf buf, Set<Item> items) {
        buf.writeInt(items.size());
        items.stream().map(Item::getId).sorted().forEach(buf::writeInt);
    }

    public static void receiveFromServer(FriendlyByteBuf buf) {
        invalidate();
        int nodeCount = buf.readInt();
        ImmutableMap.Builder<Block, LootEntry> builder = ImmutableMap.builder();
        for (int i = 0; i < nodeCount; i++) {
            readNode(buf, builder);
        }
        knownTables = builder.build();
        hasLoaded = true;
    }

    private static void readNode(FriendlyByteBuf buf, ImmutableMap.Builder<Block, LootEntry> builder) {
        Block block = BuiltInRegistries.BLOCK.byId(buf.readInt());
        LootEntry entry = new LootEntry(
                readItems(buf),
                readItems(buf),
                readItems(buf)
        );
        builder.put(block, entry);
    }

    private static Set<Item> readItems(FriendlyByteBuf buf) {
        Set<Item> items = new HashSet<>();
        int itemCount = buf.readInt();
        for (int i = 0; i < itemCount; i++) {
            items.add(Item.byId(buf.readInt()));
        }
        return items;
    }

    public static Iterable<LootPoolEntryContainer> getEntries(LootPool pool) {
        return ((LootPoolAccessor) pool).getEntries();
    }

    public static Item getItem(LootItem item) {
        return ((LootItemAccessor)item).getItem().value();
    }

    public static Iterable<LootPoolEntryContainer> getChildren(CompositeEntryBase compositeEntry) {
        return ((CompositeEntryBaseMixin) compositeEntry).getChildren();
    }

    public record LootEntry(Set<Item> earth, Set<Item> common, Set<Item> rare) {
    }
}
