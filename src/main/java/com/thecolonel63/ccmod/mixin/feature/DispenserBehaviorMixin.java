package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$2")
public class DispenserBehaviorMixin {
    @WrapOperation(method = "dispenseSilently", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void addInfiniTNT(ItemStack instance, int amount, Operation<Void> original) {
        boolean preventDecrement = instance.isOf(Items.TNT) && Optional.ofNullable(instance.get(DataComponentTypes.CUSTOM_DATA))
                .map(e -> e.copyNbt().getBoolean("ccmod:infinite_tnt"))
                .orElse(false);

        if (!preventDecrement) original.call(instance, amount);
    }
}
