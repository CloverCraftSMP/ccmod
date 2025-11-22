package com.thecolonel63.ccmod.mixin.fix;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SuppressWarnings("MixinAnnotationTarget")
@Mixin(value = ZombieHorseEntity.class, priority = 1500)
public class ZombieHorseMixin extends AbstractHorseEntity {
    protected ZombieHorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author thecolonel63
     * @reason Supplementaries broke this.
     * The original is implemented in such a way that I can't reach it with mixins *or* mixinsquared.
     * We aren't using the feature supplementaries adds, nor does anything else we use inject specifically into this method of zombie horse entity.
     * We're using this method until a better fix comes along later.
     *
     * I already made a pull request, and it already got accepted, so this is in place until the next version with the fix comes out for this version.
     */
    @Overwrite
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }
}
