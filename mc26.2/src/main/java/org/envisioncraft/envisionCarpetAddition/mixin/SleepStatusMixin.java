package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.level.GameType;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Minecraft 26.2 port of the old {@code SleepManagerMixin}.
 * <p>
 * Yarn 1.21.7 {@code net.minecraft.server.world.SleepManager}
 * became Mojang {@code net.minecraft.server.players.SleepStatus};
 * {@code update} kept its name and signature.
 */
@Mixin(SleepStatus.class)
public abstract class SleepStatusMixin {

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"))
    public boolean isSpectatorORMPFakeMixin(ServerPlayer instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNoSleepCount) {
            return instance.gameMode.getGameModeForPlayer() == GameType.SPECTATOR || instance instanceof EntityPlayerMPFake;
        }
        return instance.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
    }
}
