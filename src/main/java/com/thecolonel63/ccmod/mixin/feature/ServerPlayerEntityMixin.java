package com.thecolonel63.ccmod.mixin.feature;

import com.thecolonel63.ccmod.Ccmod;
import com.thecolonel63.ccmod.duck.AFKDuck;
import com.thecolonel63.ccmod.event.PlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements AFKDuck {
    @Unique private Vec3d lastPos = Vec3d.ZERO;
    @Unique private float lastYaw = 0;
    @Unique private float lastPitch = 0;
    @Unique private int afkTimer = 0;
    @Unique private boolean wasAFK = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void updateAFK(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        float yaw = player.getHeadYaw();
        float pitch = player.getPitch();
        Vec3d pos = player.getPos();

        if (yaw != lastYaw || pitch != lastPitch || !pos.equals(lastPos)) afkTimer = 0;
        boolean isAFK = afkTimer == Ccmod.AFK_TIME;
        if (isAFK != wasAFK) PlayerEvents.PLAYER_AFK.invoker().onPlayerAFK(player, isAFK);

        wasAFK = isAFK;
        lastYaw = yaw;
        lastPitch = pitch;
        lastPos = pos;

        if (afkTimer < Ccmod.AFK_TIME) afkTimer++;
    }

    @Override
    public boolean ccmod$isAfk() {
        return wasAFK;
    }
}
