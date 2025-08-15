package com.thecolonel63.ccmod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {
    @WrapOperation(method = "shouldHatchProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getSkyAngle(F)F"))
    private float wrapSkyAngle(World instance, float v, Operation<Float> original) {
        float angle = original.call(instance, v);
        return angle > 0.7845 && angle < 0.8202 ? 0.67f : 0.0f;
    }
}
