package com.thecolonel63.ccmod.mixin.fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.xujiayao.discord_mc_chat.Config;
import com.xujiayao.discord_mc_chat.minecraft.MinecraftEventListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(MinecraftEventListener.class)
public class MinecraftEventListenerMixin {
    @WrapOperation(method = "lambda$init$5", at = @At(value = "FIELD", target = "Lcom/xujiayao/discord_mc_chat/Config$Generic;announceDeathMessages:Z"), remap = false)
    private static boolean addDeathMessageCheck(Config.Generic instance, Operation<Boolean> original, @Local(argsOnly = true) ServerPlayerEntity player) {
        return original.call(instance) && player.getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES);
    }
}
