package org.envisioncraft.envisionCarpetAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.GameMode;
import net.minecraft.world.spawner.PhantomSpawner;
import net.minecraft.world.spawner.SpecialSpawner;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin implements SpecialSpawner {
    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"))
    public boolean isSpectatorORMPFakeMixin(ServerPlayerEntity instance) {
        if (EnvisionCarpetAdditionSettings.fakePlayerNotGeneratePhantom) {
            instance.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
            return instance.interactionManager.getGameMode() == GameMode.SPECTATOR || instance instanceof EntityPlayerMPFake;
        }
        return instance.interactionManager.getGameMode() == GameMode.SPECTATOR;
    }
}
