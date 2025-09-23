package com.thecolonel63.ccmod.mixin.fixes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(targets = "com.illusivesoulworks.elytraslot.ElytraSlotFabricMod$1")
public class ElytraSlotTrinketMixin {
    @Redirect(
            method = "tick(Lnet/minecraft/item/ItemStack;Ldev/emi/trinkets/api/SlotReference;Lnet/minecraft/entity/LivingEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V"
            )
    )
    private void elytraslot$skipDamageIfOneLeft(
            ItemStack stack, int amount, ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, Consumer<Item> breakCallback
    ) {
        int durabilityLeft = stack.getMaxDamage() - stack.getDamage();
        if (durabilityLeft > 1) {
            stack.damage(amount, serverWorld, serverPlayerEntity, breakCallback);
        }
    }
}