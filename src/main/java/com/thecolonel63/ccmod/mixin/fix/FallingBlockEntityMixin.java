package com.thecolonel63.ccmod.mixin.fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {
    @WrapOperation(method = "teleportTo", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/FallingBlockEntity;shouldDupe:Z"))
    private void preventDupe(FallingBlockEntity instance, boolean value, Operation<Void> original) {
        original.call(instance, Ccmod.ALLOW_FALLING_BLOCK_DUPING && value);
    }
}
