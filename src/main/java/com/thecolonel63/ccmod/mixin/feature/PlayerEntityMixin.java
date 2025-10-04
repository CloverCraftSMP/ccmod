package com.thecolonel63.ccmod.mixin.feature;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "dropInventory", at = @At("TAIL"))
    private void afterDropInventory(CallbackInfo ci) {
        PlayerEntity entity = (PlayerEntity) (Object) this;
        PlayerInventory inventory = entity.getInventory();
        if (!entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) return;

        for (int i = 36; i < 40; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)) {
                entity.dropItem(stack, true, false);
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }
    }
}
