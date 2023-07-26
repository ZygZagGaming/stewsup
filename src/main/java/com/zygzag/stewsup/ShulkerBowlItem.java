package com.zygzag.stewsup;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ShulkerBowlItem extends BowlFoodItem {
    public ShulkerBowlItem() {
        super(new Properties().food(new FoodProperties.Builder().alwaysEat().build()));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        float n = getNumberOfStews(stack.getOrCreateTag());
        return Math.round((n / getMaximumNumberOfStews()) * MAX_BAR_WIDTH);
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return 0xde8c83;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        CompoundTag tag = stack.getOrCreateTag();
        if (entity instanceof Player player && getNumberOfStews(tag) != 0) {
            ListTag effectsTag = tag.getList("Effects", 10);
            for (int i = 0; i < effectsTag.size(); i++) {
                CompoundTag compound = effectsTag.getCompound(i);
                float chance = compound.getFloat("Chance");
                MobEffectInstance inst = MobEffectInstance.load(compound);
                if (inst != null) {
                    //System.out.println(inst);
                    int r = getNumberOfStews(tag);
                    MobEffectInstance effectToGiveToPlayer = new MobEffectInstance(inst.getEffect(), inst.getDuration() / r, inst.getAmplifier());
                    int n = compound.getInt("Duration");
                    compound.putInt("Duration",  n - (n / r));
                    if (Math.random() < chance) player.addEffect(effectToGiveToPlayer);
                }
            }
            Pair<Integer, Float> pair = getAdjustedHungerAndSaturation(tag);
            player.getFoodData().eat(pair.getFirst(), pair.getSecond());
        }
        return getNumberOfStews(tag) != 0 ? stack : new ItemStack(Items.SHULKER_SHELL);
    }

    public static Pair<Integer, Float> getAdjustedHungerAndSaturation(CompoundTag tag) {
        int h = getHunger(tag);
        float s = getSaturation(tag);
        int n = getNumberOfStews(tag);
        tag.putInt("Hunger", h - (h / n));
        tag.putFloat("Saturation", s - (s / n));
        tag.putInt("Stews", n - 1);
        return Pair.of(h, s);
    }

    public static int getHunger(CompoundTag tag) {
        return tag.getInt("Hunger");
    }

    public static float getSaturation(CompoundTag tag) {
        return tag.getFloat("Saturation");
    }

    public static int getNumberOfStews(CompoundTag tag) {
        return tag.getInt("Stews");
    }

    public static int getMaximumNumberOfStews() {
        return 16;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, world, text, flag);
        int n = getNumberOfStews(stack.getOrCreateTag());
        MutableComponent comp = Component.translatable("stewsup.contains").withStyle(ChatFormatting.GRAY);
        comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
        comp.append(Component.literal(n + " ").withStyle(ChatFormatting.GOLD));
        comp.append(Component.translatable(n == 1 ? "stewsup.stew" : "stewsup.stews").withStyle(ChatFormatting.GRAY));
        text.add(comp);
    }
}