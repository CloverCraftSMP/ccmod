package com.thecolonel63.ccmod.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.srrapero720.waterframes.common.commands.WaterFramesCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(WaterFramesCommand.class)
public class WaterFramesCommandMixin {
    @WrapMethod(method = "hasPermissions")
    private static boolean wrapPermissions(ServerCommandSource sourceStack, Operation<Boolean> original) {
        return original.call(sourceStack) || Permissions.check(sourceStack, "waterframes.root");
    }
}
