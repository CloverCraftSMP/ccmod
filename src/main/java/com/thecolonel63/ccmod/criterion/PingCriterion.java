package com.thecolonel63.ccmod.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class PingCriterion extends AbstractCriterion<PingCriterion.Conditions> {
    public void trigger(ServerPlayerEntity player, int requiredPing) {
        trigger(player, conditions -> conditions.requirementsMet(requiredPing));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int requiredPing) implements AbstractCriterion.Conditions {
        public static Codec<PingCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                Codec.INT.fieldOf("requiredPing").forGetter(Conditions::requiredPing)
        ).apply(instance, Conditions::new));

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet(int ping) {
            return ping >= requiredPing;
        }
    }
}
