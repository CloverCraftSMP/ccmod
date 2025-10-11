package com.thecolonel63.ccmod.mixin.fix;

import com.github.quiltservertools.ledger.callbacks.ItemInsertCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.github.quiltservertools.ledger.callbacks.ItemInsertCallback$DefaultImpls")
public class ItemInsertCallbackMixin {
    @Inject(method = "EVENT$lambda$1$lambda$0", at = @At("HEAD"), cancellable = true, remap = false)
    private static void preventNullItemInsertCrash(ItemInsertCallback[] $listeners, ItemStack stack, BlockPos pos, ServerWorld world, String source, ServerPlayerEntity player, CallbackInfo ci) {
        if (pos == null) {
            ci.cancel();
        }
    }
}
