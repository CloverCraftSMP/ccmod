package com.thecolonel63.ccmod.listener;

import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PlayerAFKListener {
    void onPlayerAFK(ServerPlayerEntity player, boolean isAFK);
}
