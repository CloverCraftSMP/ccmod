package com.thecolonel63.ccmod;

import com.thecolonel63.ccmod.mixin.accessor.ItemEntryAccessor;
import com.thecolonel63.ccmod.mixin.accessor.LeafEntryAccessor;
import com.thecolonel63.ccmod.mixin.accessor.LootTableAccessor;
import com.thecolonel63.ccmod.registry.CcmodCriteria;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class Ccmod implements ModInitializer {
    public static final boolean ALLOW_STRING_DUPING = true;
    public static final boolean ALLOW_FALLING_BLOCK_DUPING = true;
    public static final boolean ALLOW_PISTON_DUPING = true;
    public static final int AFK_TIME = 20 * 60 * 5;

    public static RegistryEntry<Enchantment> MENDING;

    private static final Identifier ANCIENT_CITY = Identifier.of("minecraft:chests/ancient_city");
    private static final Identifier END_VAULT = Identifier.of("enderscape:end_city/vault");
    private static final Identifier ELYTRA_VAULT = Identifier.of("enderscape:end_city/elytra_vault");
    private static final Identifier ANCIENT_CITY_BARREL = Identifier.of("ancient_cities:ancient_city_barrel");

    private static final Identifier MIRROR = Identifier.of("enderscape:mirror");

    @Override
    public void onInitialize() {
        CcmodCriteria.init();

        LootTableEvents.REPLACE.register((key, original, source, registries) -> {
            boolean modified = false;

            if (key.getValue().equals(END_VAULT)) {
                ((LootTableAccessor) original).getPools().forEach(pool -> pool.entries.forEach(entry -> {
                    if (entry instanceof ItemEntry itemEntry && Registries.ITEM.getId(((ItemEntryAccessor) itemEntry).getItem().value()).equals(MIRROR)) {
                        ((LeafEntryAccessor) itemEntry).setWeight(1);
                    }
                }));
                modified = true;
            }

            if (key.getValue().equals(ELYTRA_VAULT)) {
                ((LootTableAccessor) original).getPools().forEach(pool -> pool.entries.forEach(entry -> {
                    if (entry instanceof ItemEntry itemEntry && Registries.ITEM.getId(((ItemEntryAccessor) itemEntry).getItem().value()).equals(MIRROR)) {
                        ((LeafEntryAccessor) itemEntry).setWeight(3);
                    }
                }));
                modified = true;
            }

            return modified ? original : null;
        });

        LootTableEvents.MODIFY.register((registryKey, builder, source, wrapper) -> {
            if (MENDING == null)
                MENDING = wrapper.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MENDING);

            if (!source.isBuiltin()) return;

            if (registryKey.getValue().equals(ANCIENT_CITY)) addMendingBook(builder, 0.1f);
            if (registryKey.getValue().equals(END_VAULT)) addMendingBook(builder, 0.25f);

            if (registryKey.getValue().equals(ANCIENT_CITY_BARREL)) {
                builder.pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.DIAMOND))
                                .conditionally(RandomChanceLootCondition.builder(0.0075f))
                );

                builder.pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE))
                                .conditionally(RandomChanceLootCondition.builder(0.0075f))
                );

                builder.pool(LootPool.builder()
                        .with(ItemEntry.builder(Items.BOOK)
                                .apply(new EnchantRandomlyLootFunction.Builder()
                                        .option(
                                                wrapper
                                                        .getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
                                                        .getOrThrow(Enchantments.SWIFT_SNEAK)
                                        ))
                        )
                        .conditionally(RandomChanceLootCondition.builder(0.0035f)));

                addMendingBook(builder, 0.0025f);

                builder.pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE))
                                .conditionally(RandomChanceLootCondition.builder(0.0025f))
                );

                builder.pool(LootPool.builder()
                        .with(ItemEntry.builder(Items.BOOK)
                                .apply(EnchantRandomlyLootFunction.builder(wrapper))
                        )
                        .conditionally(RandomChanceLootCondition.builder(0.0025f)));
            }
        });
    }

    private static void addMendingBook(LootTable.Builder builder, float rarity) {
        builder.pool(LootPool.builder()
                .with(ItemEntry.builder(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsLootFunction.Builder().enchantment(MENDING, ConstantLootNumberProvider.create(1.0f)))
                )
                .conditionally(RandomChanceLootCondition.builder(rarity)));
    }

    public static boolean hasMending(ItemStack stack) {
        return MENDING != null &&
                Optional.ofNullable(stack.get(DataComponentTypes.STORED_ENCHANTMENTS))
                        .map(e -> e.getEnchantments().contains(MENDING))
                        .orElse(false);
    }
}
