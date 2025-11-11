package com.thecolonel63.ccmod.mixin.fix;

import com.bawnorton.mixinsquared.TargetHandler;
import com.illusivesoulworks.elytraslot.platform.Services;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.EnderscapeEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 1500)
public abstract class LivingEntityMixinRebound extends Entity {
    public LivingEntityMixinRebound(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract float getJumpVelocity();

    @Unique
    private boolean wasOutOfFluid = false;

    @Unique
    private int onGroundTicks = -1;

    @TargetHandler(mixin = "net.bunten.enderscape.mixin.LivingEntityMixin", name = "Enderscape$canStandOnFluid")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/bunten/enderscape/registry/EnderscapeEnchantments;hasRebound(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean wrapReboundForFluid(World registry, ItemStack enchantment, Operation<Boolean> original) {
        return original.call(registry, enchantment) || hasReboundTrinket();
    }

    @TargetHandler(mixin = "net.bunten.enderscape.mixin.LivingEntityMixin", name = "Enderscape$travel")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/bunten/enderscape/registry/EnderscapeEnchantments;hasRebound(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean wrapReboundForTravel(World registry, ItemStack enchantment, Operation<Boolean> original) {
        return original.call(registry, enchantment) || hasReboundTrinket();
    }

    @TargetHandler(mixin = "net.bunten.enderscape.mixin.LivingEntityMixin", name = "Lnet/bunten/enderscape/mixin/LivingEntityMixin;Enderscape$updateFallFlying(Lnet/minecraft/entity/LivingEntity;)Z")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/bunten/enderscape/registry/EnderscapeEnchantments;hasRebound(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean wrapReboundForFallFlying(World registry, ItemStack enchantment, Operation<Boolean> original) {
        return original.call(registry, enchantment) || hasReboundTrinket();
    }

    @TargetHandler(mixin = "net.bunten.enderscape.mixin.LivingEntityMixin", name = "Enderscape$travelShiftAfter")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/bunten/enderscape/registry/EnderscapeEnchantments;hasRebound(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean wrapReboundForTravelShiftAfter(World registry, ItemStack enchantment, Operation<Boolean> original) {
        return original.call(registry, enchantment) || hasReboundTrinket();
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void fixWaterBounding(CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof ServerPlayerEntity player
                && player.isFallFlying()
                && !EnderscapeEnchantments.hasRebound(player.getWorld(), player.getEquippedStack(EquipmentSlot.CHEST))
                && hasReboundTrinket()
        ) {
            if (player.isInFluid() && wasOutOfFluid && player.getVelocity().getY() > -0.9) {
                float f = this.getJumpVelocity();
                if (f > 1.0E-5F) {
                    Vec3d vec3d = player.getVelocity();
                    player.setVelocity(vec3d.x, f, vec3d.z);
                    if (player.isSprinting()) {
                        float g = player.getYaw() * (float) (Math.PI / 180.0);
                        player.addVelocityInternal(new Vec3d(-MathHelper.sin(g) * 0.2, 0.0, MathHelper.cos(g) * 0.2));
                    }
                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                }
            } else if (onGroundTicks == 0) {
                player.stopFallFlying();
            }
        }

        wasOutOfFluid = !this.isInFluid();
        if (this.isOnGround()) {
            if (onGroundTicks > 0) onGroundTicks--;
        } else {
            onGroundTicks = 10;
        }
    }

    @Unique
    private boolean hasReboundTrinket() {
        LivingEntity entity = (LivingEntity) (Object) this;

        RegistryEntry<Enchantment> enchantment = entity.getRegistryManager()
                .getOptionalWrapper(RegistryKeys.ENCHANTMENT)
                .flatMap(enchantmentRegistryLookup -> enchantmentRegistryLookup.getOptional(RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("enderscape:rebound"))))
                .orElse(null);

        return EnchantmentHelper.getLevel(enchantment, Services.ELYTRA.getEquipped(entity)) > 0;
    }
}
