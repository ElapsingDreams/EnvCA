package org.envisioncraft.envisionCarpetAddition.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.EndSpikeConfiguration;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Minecraft 26.2 port of the old {@code EndSpikeFeatureMixin}.
 * <p>
 * Yarn 1.21.7 {@code net.minecraft.world.gen.feature.EndSpikeFeature}
 * became Mojang {@code net.minecraft.world.level.levelgen.feature.EndSpikeFeature}.
 * The old {@code generateSpike(ServerWorldAccess, Random, EndSpikeFeatureConfig, Spike)}
 * hook no longer exists: {@code place} now iterates the spikes itself and calls the
 * private {@code placeSpike} for each one, so this hooks {@code place} instead and
 * takes over the whole feature when the rule asks for crystal-only spikes.
 * <p>
 * The effect is the same as before: no obsidian pillars, just the crystals - and only
 * where vanilla would have placed a spike in the first place.
 */
@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {

    @Inject(
            method = "place",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPlace(
            FeaturePlaceContext<EndSpikeConfiguration> context,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (EnvisionCarpetAdditionSettings.respawnDragonNoObsidianSpike) {
            placeCrystalsOnly(context);
            cir.setReturnValue(true);
        }
    }

    @Unique
    private static void placeCrystalsOnly(FeaturePlaceContext<EndSpikeConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        EndSpikeConfiguration config = context.config();

        // Keep vanilla's per-chunk placement semantics: only the chunk that owns a spike's
        // centre gets the crystal.
        BlockPos origin = context.origin();
        for (EndSpikeFeature.EndSpike spike : config.getSpikes()) {
            if (!spike.isCenterWithinChunk(origin)) {
                continue;
            }

            spawnCrystal(world, random, config, spike);
        }
    }

    @Unique
    private static void spawnCrystal(
            WorldGenLevel world,
            RandomSource random,
            EndSpikeConfiguration config,
            EndSpikeFeature.EndSpike spike
    ) {
        ServerLevelAccessor accessor = world;
        ServerLevel serverLevel = accessor.getLevel();

        EndCrystal endCrystalEntity = EntityTypes.END_CRYSTAL.create(
                serverLevel,
                EntitySpawnReason.STRUCTURE
        );

        if (endCrystalEntity != null) {
            endCrystalEntity.setBeamTarget(config.getCrystalBeamTarget());
            endCrystalEntity.setInvulnerable(config.isCrystalInvulnerable());
            endCrystalEntity.setPos(
                    (double) spike.getCenterX() + 0.5D,
                    (double) (spike.getHeight() + 1),
                    (double) spike.getCenterZ() + 0.5D
            );
            accessor.addFreshEntity(endCrystalEntity);
        }
    }
}
