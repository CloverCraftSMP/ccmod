package com.thecolonel63.ccmod.mixin.accessor;

import net.minecraft.loot.entry.LeafEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LeafEntry.class)
public interface LeafEntryAccessor {
    @Accessor("weight") @Mutable
    void setWeight(int weight);
}
