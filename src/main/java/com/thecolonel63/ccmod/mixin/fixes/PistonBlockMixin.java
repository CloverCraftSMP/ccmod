package com.thecolonel63.ccmod.mixin.fixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(PistonBlock.class)
public class PistonBlockMixin<E> {
    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 1))
    private E addOldPos(List<E> instance, int i, Operation<E> original, @Share("oldPos") LocalRef<BlockPos> oldPos, @Share("allowDupe") LocalBooleanRef allowDupe) {
        E value = original.call(instance, i);
        oldPos.set((BlockPos) value);
        allowDupe.set(Ccmod.ALLOW_PISTON_DUPING);
        return value;
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 3))
    private BlockState wrapBlockState(World instance, BlockPos pos, Operation<BlockState> original, @Share("oldPos") LocalRef<BlockPos> oldPos, @Share("allowDupe") LocalBooleanRef allowDupe, @Local(ordinal = 1) Direction direction) {
        BlockState state = original.call(instance, pos);
        return allowDupe.get() ? state : null;
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 2, shift = At.Shift.AFTER))
    private void replaceMap(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) LocalRef<BlockState> blockState2, @Share("oldPos") LocalRef<BlockPos> oldPos, @Local Map<BlockPos, BlockState> map, @Share("allowDupe") LocalBooleanRef allowDupe) {
        if (!allowDupe.get()) {
            BlockPos oldBlockPos = oldPos.get();
            blockState2.set(world.getBlockState(oldBlockPos));
            map.replace(oldBlockPos, blockState2.get());
        }
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PistonExtensionBlock;createBlockEntityPiston(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;ZZ)Lnet/minecraft/block/entity/BlockEntity;", ordinal = 0))
    private BlockEntity wrapAddPiston(BlockPos pos, BlockState state, BlockState pushedBlock, Direction facing, boolean extending, boolean source, Operation<BlockEntity> original, @Local(ordinal = 0) LocalRef<BlockState> blockState2, @Share("oldPos") LocalRef<BlockPos> oldPos, @Local Map<BlockPos, BlockState> map, @Share("allowDupe") LocalBooleanRef allowDupe) {
        return original.call(pos, state, allowDupe.get() ? pushedBlock : blockState2.get(), facing, extending, source);
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void setBlock(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, @Share("oldPos") LocalRef<BlockPos> oldPos, @Share("allowDupe") LocalBooleanRef allowDupe) {
        if (!allowDupe.get()) {
            world.setBlockState(oldPos.get(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.MOVED | 1024);
        }
    }
}
