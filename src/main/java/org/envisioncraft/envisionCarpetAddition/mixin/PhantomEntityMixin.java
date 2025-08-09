package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.entity.LivingEntity;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal")
public abstract class PhantomEntityMixin {

    @Redirect(
            method = "shouldContinue",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isSpectator()Z"
            )
    )
    public boolean modifySpectatorCheck(LivingEntity instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNotAsPhantomGoal) return instance.isSpectator() || instance instanceof EntityPlayerMPFake;
        return instance.isSpectator();
    }
}