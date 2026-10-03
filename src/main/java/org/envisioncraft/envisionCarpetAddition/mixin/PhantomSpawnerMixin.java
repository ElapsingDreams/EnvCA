package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Minecraft 26.2 port of the old {@code PhantomSpawnerMixin}.
 * <p>
 * Yarn 1.21.7 {@code net.minecraft.world.spawner.PhantomSpawner}
 * became Mojang {@code net.minecraft.world.level.levelgen.PhantomSpawner};
 * {@code spawn} became {@code tick}, and it no longer implements {@code SpecialSpawner}
 * (now {@code net.minecraft.world.level.CustomSpawner}).
 */
@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"))
    public boolean isSpectatorORMPFakeMixin(ServerPlayer instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNotGeneratePhantom) {
            if (instance instanceof EntityPlayerMPFake) {
                instance.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
                return true;
            }
            return instance.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
        }
        return instance.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
    }
}
