package com.zygzag.stewsup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue SHULKER_STEW_CAPACITY = BUILDER
            .comment("Number of stews that a shulker shell can hold")
            .defineInRange("magicNumber", 16, 1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int shulkerStewCapacity;

    @SubscribeEvent
    @SuppressWarnings("unused")
    static void onLoad(final ModConfigEvent event) {
        shulkerStewCapacity = SHULKER_STEW_CAPACITY.get();
    }
}
