package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrillHeadTooltips implements TooltipModifier {
    @Override
    public void modify(ItemTooltipEvent itemTooltipEvent) {
        List<Component> stats = DrillHeadTooltips.getDrillStats(BuiltInRegistries.ITEM.getKey(itemTooltipEvent.getItemStack().getItem()), itemTooltipEvent.getEntity());
        if (!stats.isEmpty()) {
            itemTooltipEvent.getToolTip().addAll(stats);
        }
    }

    public static DrillHeadTooltips create(Item item) {
        if (DDrillHeads.knownDrillHeads.stream().anyMatch(entry -> entry.get().asItem() == item)) {
            return new DrillHeadTooltips();
        }
        return null;
    }

    public static ChatFormatting mulColor(double factor) {
        if (factor <= 0)
            return ChatFormatting.DARK_RED;
        if (factor < 1)
            return ChatFormatting.RED;
        if (factor > 1)
            return ChatFormatting.GREEN;
        return ChatFormatting.YELLOW;
    }
    public static int barLengthProbability(double factor) {
        if (factor <= 0)
            return 0;
        if (factor < 1)
            return 1;
        if (factor > 1)
            return 3;
        return 2;
    }

    public static LangBuilder makeProbabilityMultiplier(double factor, boolean numbers, String suffix) {
        LangBuilder bar = CreateLang.builder().text(TooltipHelper.makeProgressBar(3, barLengthProbability(factor)));
        if (numbers)
            bar.text(LangNumberFormat.format(factor) + "x ");
        if (!Objects.equals(suffix, ""))
            bar.add(Component.translatable(suffix));
        return bar.style(DrillHeadTooltips.mulColor(factor));
    }

    public static ChatFormatting speedColor(double speed) {
        if (speed < 1)
            return ChatFormatting.DARK_RED;
        if (speed == 1)
            return ChatFormatting.RED;
        if (speed < 2)
            return ChatFormatting.YELLOW;
        return ChatFormatting.GREEN;
    }
    private static int barLengthSpeed(double speed) {
        if (speed < 1) {
            return 0;
        }
        if (speed == 1) {
            return 1;
        }
        if (speed < 2) {
            return 2;
        }
        return 3;
    }

    public static String makeSpeedBar(double factor, boolean numbers, String suffix) {
        String bar = TooltipHelper.makeProgressBar(3, barLengthSpeed(factor));
        if (numbers)
            bar += LangNumberFormat.format(factor) + "x";
        if (!Objects.equals(suffix, ""))
            bar += " " + suffix;
        return bar;
    }

    public static List<Component> getDrillStats(ResourceLocation drillID, Player player) {
        boolean hasGoggles = GogglesItem.isWearingGoggles(player);
        List<Component> list = new ArrayList<>();

        double speed = 1.0 / DrillHeadStats.getDrillSpeedModifier(drillID);

        String bar = makeSpeedBar(speed, hasGoggles, "");
        if (!hasGoggles) {
            if (speed < 1) {
                bar += "Slow";
            } else if (speed == 1) {
                bar += "Normal";
            } else if (speed < 2) {
                bar += "Fast";
            }
            else {
                bar += "Blazing";
            }
        }

        CreateLang.builder().add(CreateLang.text("Speed:").style(ChatFormatting.GRAY)).addTo(list);
        CreateLang.builder().add(CreateLang.text(bar).style(speedColor(speed))).addTo(list);

        CreateLang.builder().add(CreateLang.text("Resource Odds:").style(ChatFormatting.GRAY)).addTo(list);
        DrillHeadStats.getLootWeightMultiplier(drillID).addTooltip(list, hasGoggles, true);

        return list;
    }
}
