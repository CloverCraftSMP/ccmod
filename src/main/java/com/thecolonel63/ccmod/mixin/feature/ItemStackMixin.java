package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean isOf(Item item);

    @WrapMethod(method = "takesDamageFrom")
    private boolean preventCactusDamageCactus(DamageSource source, Operation<Boolean> original) {
        boolean isCactus = this.isOf(Items.CACTUS);
        boolean isCactusSource = source.getTypeRegistryEntry().getKey().map(e -> e == DamageTypes.CACTUS).orElse(false);
        return !(isCactus && isCactusSource) && original.call(source);
    }
}
