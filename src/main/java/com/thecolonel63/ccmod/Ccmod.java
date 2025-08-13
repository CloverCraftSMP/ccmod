package com.thecolonel63.ccmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class Ccmod implements ModInitializer {
    public static RegistryEntry<Enchantment> MENDING;

    @Override
    public void onInitialize() {
    }

    public static boolean hasMending(ItemStack stack) {
        return MENDING != null &&
                Optional.ofNullable(stack.get(DataComponentTypes.STORED_ENCHANTMENTS))
                .map(e -> e.getEnchantments().contains(MENDING))
                .orElse(false);
    }
}
