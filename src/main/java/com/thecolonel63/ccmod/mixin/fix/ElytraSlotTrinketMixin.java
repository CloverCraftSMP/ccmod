package com.thecolonel63.ccmod.mixin.fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Pseudo
@Mixin(targets = "com.illusivesoulworks.elytraslot.ElytraSlotFabricMod$1")
public class ElytraSlotTrinketMixin {
    @WrapOperation(
            method = "tick(Lnet/minecraft/item/ItemStack;Ldev/emi/trinkets/api/SlotReference;Lnet/minecraft/entity/LivingEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V"
            )
    )
    private void skipDamageIfOneLeft(
            ItemStack stack, int amount, ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity,
            Consumer<Item> breakCallback, Operation<Void> original
    ) {
        int durabilityLeft = stack.getMaxDamage() - stack.getDamage();
        if (durabilityLeft > 1) {
            original.call(stack, amount, serverWorld, serverPlayerEntity, breakCallback);
        }
    }
}