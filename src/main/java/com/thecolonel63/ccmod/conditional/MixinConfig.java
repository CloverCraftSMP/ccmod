package com.thecolonel63.ccmod.conditional;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MixinConfig implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "com.thecolonel63.ccmod.mixin.feature.CannonBlockTileMixin", () -> FabricLoader.getInstance().isModLoaded("supplementaries"),
            "com.thecolonel63.ccmod.mixin.feature.PlayerStateManagerMixin", () -> FabricLoader.getInstance().isModLoaded("status"),
            "com.thecolonel63.ccmod.mixin.fix.ElytraSlotTrinketMixin", () -> FabricLoader.getInstance().isModLoaded("elytraslot"),
            "com.thecolonel63.ccmod.mixin.fix.ItemInsertCallbackMixin", () -> FabricLoader.getInstance().isModLoaded("ledger"),
            "com.thecolonel63.ccmod.mixin.fix.ItemRemoveCallbackMixin", () -> FabricLoader.getInstance().isModLoaded("ledger"),
            "com.thecolonel63.ccmod.mixin.fix.LivingEntityMixinRebound", () -> FabricLoader.getInstance().isModLoaded("elytraslot") && FabricLoader.getInstance().isModLoaded("enderscape"),
            "com.thecolonel63.ccmod.mixin.fix.LivingEntityMixinSleep", () -> FabricLoader.getInstance().isModLoaded("status"),
            "com.thecolonel63.ccmod.mixin.fix.MinecraftEventListenerMixin", () -> FabricLoader.getInstance().isModLoaded("discord-mc-chat-1_21_1")
    );

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
