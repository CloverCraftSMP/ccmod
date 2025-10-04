package com.thecolonel63.ccmod.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Pseudo
@Mixin(CannonBlockTile.class)
public class CannonBlockTileMixin {
    @WrapOperation(method = "fire", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V", ordinal = 1))
    private void checkDecrementStack(ItemStack instance, int amount, Operation<Void> original) {
        boolean preventDecrement = instance.isOf(Items.TNT) && Optional.ofNullable(instance.get(DataComponentTypes.CUSTOM_DATA))
                .map(e -> e.copyNbt().getBoolean("ccmod:infinite_tnt"))
                .orElse(false);

        if (!preventDecrement) original.call(instance, amount);
    }
}
