package com.thecolonel63.ccmod.mixin.fixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0))
    private static boolean patchPistonDuping(World instance, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        return original.call(instance, pos, state, Ccmod.ALLOW_PISTON_DUPING ? flags : flags | Block.NOTIFY_LISTENERS);
    }
}
