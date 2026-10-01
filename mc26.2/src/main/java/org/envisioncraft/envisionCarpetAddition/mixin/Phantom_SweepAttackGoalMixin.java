package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.world.entity.LivingEntity;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Minecraft 26.2 port of the old {@code PhantomEntity$SwoopMovementGoal} mixin.
 * <p>
 * Yarn 1.21.7 {@code net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal}
 * became Mojang {@code net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal},
 * and its {@code shouldContinue()} method became {@code canContinueToUse()}.
 */
@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public abstract class Phantom_SweepAttackGoalMixin {

    @Redirect(
            method = "canContinueToUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
            )
    )
    public boolean modifySpectatorCheck(LivingEntity instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNotAsPhantomGoal) {
            return instance.isSpectator() || instance instanceof EntityPlayerMPFake;
        }
        return instance.isSpectator();
    }
}
