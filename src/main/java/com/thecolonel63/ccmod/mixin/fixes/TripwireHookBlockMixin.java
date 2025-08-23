package com.thecolonel63.ccmod.mixin.fixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TripwireHookBlock.class)
public class TripwireHookBlockMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 2))
    private static boolean preventStringDuping(World instance, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        return (instance.getBlockState(pos).isOf(Blocks.TRIPWIRE) || Ccmod.ALLOW_STRING_DUPING) && original.call(instance, pos, state, flags);
    }
}
