package com.thecolonel63.ccmod.registry;

import com.thecolonel63.ccmod.listener.PlayerAFKListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class CcmodEvents {
    public static final Event<PlayerAFKListener> PLAYER_AFK = EventFactory.createArrayBacked(PlayerAFKListener.class, listeners -> (player, isAFK) -> {
        for (PlayerAFKListener listener : listeners) {
            listener.onPlayerAFK(player, isAFK);
        }
    });
}
