package com.thecolonel63.ccmod.mixin;

import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract DynamicRegistryManager.Immutable getRegistryManager();

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;createMetadata()Lnet/minecraft/server/ServerMetadata;"))
    private void setMendingValue(CallbackInfo ci) {
        Ccmod.MENDING = this.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry((Enchantments.MENDING)).orElseThrow();
    }
}
