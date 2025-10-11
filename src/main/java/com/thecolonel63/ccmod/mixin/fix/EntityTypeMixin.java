package com.thecolonel63.ccmod.mixin.fix;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {
    @Unique
    private final Identifier RED_MERCHANT = Identifier.of("supplementaries", "red_merchant");

    @WrapMethod(method = "create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;")
    private @Nullable T preventRedMerchant(World world, Operation<T> original) {
        T entity = original.call(world);
        Identifier entityId = Registries.ENTITY_TYPE.getId(entity.getType());
        return entityId.equals(RED_MERCHANT) ? null : entity;
    }
}
