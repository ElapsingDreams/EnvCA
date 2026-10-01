package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import net.minecraft.world.GameMode;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SleepManager.class)
//@Mixin(targets = "net.minecraft.server.world.SleepManager")
public abstract class SleepManagerMixin {
    //@Inject(method = "update",
    //        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z")
    //)
    //public void isSpectatorORMPFakeMixin(List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir) {
    //    if (players instanceof EntityPlayerMPFake)
    //}

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"))
    public boolean isSpectatorORMPFakeMixin(ServerPlayerEntity instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNoSleepCount) return instance.interactionManager.getGameMode() == GameMode.SPECTATOR || instance instanceof EntityPlayerMPFake;
        return instance.interactionManager.getGameMode() == GameMode.SPECTATOR;
    }
}