package com.thecolonel63.ccmod.mixin.fix;

import de.maxhenkel.status.events.PlayerEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixinSleep {
    @Inject(method = "sleep", at = @At("TAIL"))
    private void onSleep(BlockPos pos, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof ServerPlayerEntity serverPlayer) {
            PlayerEvents.PLAYER_SLEEP.invoker().accept(serverPlayer);
        }
    }
}
