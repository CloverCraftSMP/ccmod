package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.thecolonel63.ccmod.duck.AFKDuck;
import com.thecolonel63.ccmod.registry.CcmodEvents;
import de.maxhenkel.status.net.PlayerStatePacket;
import de.maxhenkel.status.playerstate.Availability;
import de.maxhenkel.status.playerstate.PlayerState;
import de.maxhenkel.status.playerstate.PlayerStateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Pseudo
@Mixin(PlayerStateManager.class)
public abstract class PlayerStateManagerMixin {
    @Shadow(remap = false) @Final private ConcurrentHashMap<UUID, PlayerState> states;

    @Shadow(remap = false) protected abstract void broadcastState(MinecraftServer server, PlayerState state);

    @Inject(method = "<init>()V", at = @At("TAIL"), remap = false)
    private void addAFKEventCallback(CallbackInfo ci) {
        CcmodEvents.PLAYER_AFK.register((player, isAFK) -> {
            UUID uuid = player.getUuid();
            PlayerState state = this.states.getOrDefault(uuid, new PlayerState(uuid));
            if (state.getAvailability() == Availability.DO_NOT_DISTURB) return;
            state.setAvailability(isAFK ? Availability.NONE : Availability.OPEN);
            this.states.put(uuid, state);
            this.broadcastState(player.server, state);
        });
    }

    @WrapOperation(method = "lambda$new$0", at = @At(value = "INVOKE", target = "Lde/maxhenkel/status/net/PlayerStatePacket;getPlayerState()Lde/maxhenkel/status/playerstate/PlayerState;"))
    private PlayerState addAfkStatusToPacket(PlayerStatePacket instance, Operation<PlayerState> original, @Local ServerPlayerEntity player) {
        PlayerState state = original.call(instance);
        if (state.getAvailability() != Availability.DO_NOT_DISTURB)
            state.setAvailability(((AFKDuck) player).ccmod$isAfk() ? Availability.NONE : Availability.OPEN);
        return state;
    }

}
