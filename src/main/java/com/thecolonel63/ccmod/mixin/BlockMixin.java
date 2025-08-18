package com.thecolonel63.ccmod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onStacksDropped(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V"))
    private static void addXPToCrops(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo ci) {
        if (entity instanceof ServerPlayerEntity && tool.isIn(ItemTags.HOES) && state.getBlock() instanceof CropBlock cropBlock) {
            int maxAge = cropBlock.getMaxAge();
            if (cropBlock.getAge(state) == maxAge) {
                RegistryEntry.Reference<Enchantment> fortune = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
                int fortuneLevel = tool.getEnchantments().getLevel(fortune);
                int xpAmount = (world.random.nextInt(maxAge - 1) + 1) + fortuneLevel;
                ExperienceOrbEntity.spawn((ServerWorld) world, pos.toCenterPos().add(0.0, 0.5, 0.0), xpAmount);
            }
        }
    }
}
