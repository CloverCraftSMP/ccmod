package com.thecolonel63.ccmod.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {
    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void addCustomPotions(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        Item soulStar = Registries.ITEM.get(Identifier.of("bosses_of_mass_destruction", "soul_star"));
        builder.registerRecipes(soulStar, Potions.LUCK);
    }
}
