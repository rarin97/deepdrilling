package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrillHeadBE extends KineticBlockEntity {
    private double damage = 0;
    private boolean unbreakable = false;
    private ItemEnchantments enchantments = ItemEnchantments.EMPTY;
    private int enchUnbreaking = 0;
    private int enchEfficiency = 0;
    private int enchFortune = 0;
    private boolean enchMending = false;
    private ItemStack storedItem = ItemStack.EMPTY;

    public DrillHeadBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setDamage(double amount) {
        damage = amount;
        if (damage >= getMaxDamage()) {
            if (!unbreakable && !enchMending) {
                level.destroyBlock(getBlockPos(), false);
                return;
            } else {
                damage = getMaxDamage();
            }
        }
        setChanged();
        sendData();
    }

    public void applyDamage(double amount) {
        if (!unbreakable) {
            setDamage(damage + amount);
        }
    }

    public double getMaxDamage() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.DRILL_DURABILITY.getOrDefault(name, 100.0);
    }

    public boolean isEnchanted() {
        return enchUnbreaking > 0 || enchEfficiency > 0 || enchFortune > 0 || enchMending;
    }


    private boolean isFunctional() {
        if (enchMending) {
            return damage < getMaxDamage();
        }
        return true;
    }
    private static final Modifier<Boolean, DrillHeadBE> MODIFIER_FUNCTION = ModifierTypes.CAN_FUNCTION.create(
            ((core, head, be, base, prev) -> prev && be.isFunctional()), 0
    );

    private double getSpeedModifier() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        double speedStat = DrillHeadStats.DRILL_SPEED_MODIFIERS.getOrDefault(name, 1.0);
        return speedStat / (1 + (double) enchEfficiency / 5);
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> prev * be.getSpeedModifier()), 1000
    );

    private double getDamageModifier() {
        return 1D / (1 + enchUnbreaking);
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            ((core, head, be, base, prev) -> prev * be.getDamageModifier()), -1000
    );

    private double getFortuneAmount() {
        return (double) enchFortune / 3;
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_FORTUNE = ModifierTypes.FORTUNE.create(
            ((core, head, be, base, prev) -> prev + be.getFortuneAmount()), 1000
    );

    private DrillHeadStats.WeightMultipliers getWeightMultipliers() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.LOOT_WEIGHT_MULTIPLIER.getOrDefault(name, DrillHeadStats.WeightMultipliers.ONE);
    }
    private static final Modifier<DrillHeadStats.WeightMultipliers, DrillHeadBE> MODIFIER_WEIGHTS = ModifierTypes.RESOURCE_WEIGHT.create(
            ((core, head, be, base, prev) -> prev.mul(be.getWeightMultipliers())), 1000
    );

    public List<Modifier> getModifiers() {
        return List.of(MODIFIER_FUNCTION, MODIFIER_SPEED, MODIFIER_DAMAGE, MODIFIER_FORTUNE, MODIFIER_WEIGHTS);
    }

    public ItemStack writeItemNBT(ItemStack item) {
        item.set(DataComponents.DAMAGE, (int) damage);

        if (unbreakable)
            item.set(DataComponents.UNBREAKABLE, new Unbreakable(true));

        item.set(DataComponents.ENCHANTMENTS, enchantments);
        storedItem = item.copy();
        return item;
    }

    public void readItemNBT(ItemStack item) {
        storedItem = item.copy();
        unbreakable = item.has(DataComponents.UNBREAKABLE);
        enchantments = item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        updateCachedEnchantments();
        setDamage(item.getOrDefault(DataComponents.DAMAGE, 0));
    }

    private void updateCachedEnchantments() {
        if (level == null) {
            enchUnbreaking = 0;
            enchEfficiency = 0;
            enchFortune = 0;
            enchMending = false;
            return;
        }

        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        enchUnbreaking = enchantments.getLevel(registry.getHolderOrThrow(Enchantments.UNBREAKING));
        enchEfficiency = enchantments.getLevel(registry.getHolderOrThrow(Enchantments.EFFICIENCY));
        enchFortune = enchantments.getLevel(registry.getHolderOrThrow(Enchantments.FORTUNE));
        enchMending = enchantments.getLevel(registry.getHolderOrThrow(Enchantments.MENDING)) > 0;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if (!unbreakable) {
            CreateLang.text("Head Durability:").style(ChatFormatting.GRAY).forGoggles(tooltip);
            double fractDamage = damage / getMaxDamage();
            ChatFormatting formatting;
            if (fractDamage < 0.25) {
                formatting = ChatFormatting.GREEN;
            } else if (fractDamage < 0.5) {
                formatting = ChatFormatting.YELLOW;
            } else if (fractDamage < 0.75) {
                formatting = ChatFormatting.GOLD;
            } else {
                formatting = ChatFormatting.RED;
            }
            CreateLang.builder().space()
                    .text(LangNumberFormat.format((int) (getMaxDamage() - damage)))
                    .style(formatting)
                    .add(CreateLang.text(String.format(" / %s", (int) getMaxDamage()))
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip);
        }
        return true;
    }

    @Nullable
    public DrillCoreBE getCore() {
        if (level.getBlockEntity(getBlockPos().relative(
                    getBlockState().getValue(DrillHeadBlock.FACING).getOpposite())
                ) instanceof DrillCoreBE drillCore) {
            return drillCore;
        }
        return null;
    }

    @Override
    public void remove() {
        DrillCoreBE core = getCore();
        if (core != null) {
            core.removeDrillHead();
        }
        super.remove();
    }

    public static String DAMAGE_KEY = "Damage";
    public static String UNBREAKABLE_KEY = "Unbreakable";
    public static String ITEM_KEY = "Item";

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        damage = compound.getDouble(DAMAGE_KEY);
        unbreakable = compound.getBoolean(UNBREAKABLE_KEY);
        storedItem = compound.contains(ITEM_KEY) ? ItemStack.parseOptional(registries, compound.getCompound(ITEM_KEY)) : ItemStack.EMPTY;
        enchantments = storedItem.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        updateCachedEnchantments();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putDouble(DAMAGE_KEY, damage);
        compound.putBoolean(UNBREAKABLE_KEY, unbreakable);

        if (!storedItem.isEmpty())
            compound.put(ITEM_KEY, storedItem.save(registries));
    }
}
