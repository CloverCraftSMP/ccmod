package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$FindTargetGoal")
public class PhantomEntityMixin {
    @WrapOperation(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;isTarget(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private boolean addInsomniaCheck(PhantomEntity instance, LivingEntity livingEntity, TargetPredicate targetPredicate, Operation<Boolean> original) {
        boolean canStart = original.call(instance, livingEntity, targetPredicate);

        if (instance.getWorld().getDimensionEntry().matchesKey(DimensionTypes.OVERWORLD) && livingEntity instanceof ServerPlayerEntity serverPlayer) {
            int sleepTicks = MathHelper.clamp(serverPlayer.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
            return (sleepTicks >= 72000 && canStart);
        }

        return canStart;
    }
}
