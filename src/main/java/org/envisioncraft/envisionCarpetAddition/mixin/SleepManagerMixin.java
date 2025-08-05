package org.envisioncraft.envisionCarpetAddition.mixin;

import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"))
    public boolean isSpectatorORMPFakeMixin(ServerPlayerEntity instance) {
        if (EnvisionCarpetAdditionSettings.ignoreFakePlayerSleep) return instance.interactionManager.getGameMode() == GameMode.SPECTATOR || instance instanceof EntityPlayerMPFake;
        return instance.interactionManager.getGameMode() == GameMode.SPECTATOR;
    }
}