package com.zygzag.stewsup;

import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "stewsup";

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<Item> GOLDEN_STEW = ITEMS.register("golden_stew", () -> new BowlFoodItem(
            new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL).rarity(Rarity.RARE).food(
                    new FoodProperties.Builder().alwaysEat().nutrition(6).saturationMod(0.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 2), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1f)
                            .build()
            )
    ));

    public static final RegistryObject<Item> ENCHANTED_GOLDEN_STEW = ITEMS.register("enchanted_golden_stew", () -> new EnchantedBowlFoodItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).craftRemainder(Items.BOWL).food(
                    new FoodProperties.Builder().alwaysEat().nutrition(12).saturationMod(1.6f)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 4), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 2), 1f)
                            .build()
            )
    ));
    @SuppressWarnings("unused")
    public static final RegistryObject<Item> SHULKER_BOWL = ITEMS.register("shulker_bowl", ShulkerBowlItem::new);
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ShulkerBowlRecipe>> SHULKER_BOWL_CRAFTING = RECIPE_SERIALIZERS.register("crafting_special_shulker_bowl", () -> new SimpleCraftingRecipeSerializer<>(ShulkerBowlRecipe::new));

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) { }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(GOLDEN_STEW);
            event.accept(ENCHANTED_GOLDEN_STEW);
        }
    }
}
