package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @WrapOperation(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult addPreventPlacementTag(ItemStack instance, ItemUsageContext context, Operation<ActionResult> original) {
        boolean preventPlacement = Optional.ofNullable(instance.get(DataComponentTypes.CUSTOM_DATA))
                .map(e -> e.copyNbt().getBoolean("ccmod:prevent_placement"))
                .orElse(false);

        if (context.getPlayer() != null && preventPlacement) context.getPlayer().currentScreenHandler.syncState();

        return preventPlacement ? ActionResult.FAIL : original.call(instance, context);
    }
}
