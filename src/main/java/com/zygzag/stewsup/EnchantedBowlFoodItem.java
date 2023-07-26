package com.zygzag.stewsup;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EnchantedBowlFoodItem extends BowlFoodItem {
    public EnchantedBowlFoodItem(Properties prop) {
        super(prop);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}