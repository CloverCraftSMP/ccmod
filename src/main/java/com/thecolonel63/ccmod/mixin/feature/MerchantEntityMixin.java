package com.thecolonel63.ccmod.mixin.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.thecolonel63.ccmod.Ccmod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MerchantEntity.class)
public class MerchantEntityMixin {
    @WrapOperation(method = "fillRecipesFromPool", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/TradeOffers$Factory;create(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/village/TradeOffer;"))
    private TradeOffer rerollMendingTrades(TradeOffers.Factory instance, Entity entity, Random random, Operation<TradeOffer> original) {
        TradeOffer offer = original.call(instance, entity, random);
        return Ccmod.hasMending(offer.getSellItem()) ? null : offer;
    }
}
