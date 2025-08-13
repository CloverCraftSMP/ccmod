package com.thecolonel63.ccmod.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffer.class)
public class TradeOfferMixin {
    @Shadow
    @Final
    private ItemStack sellItem;

    @WrapMethod(method = "isDisabled")
    private boolean preventSellMending(Operation<Boolean> original) {
        return Ccmod.hasMending(sellItem) || original.call();
    }
}
