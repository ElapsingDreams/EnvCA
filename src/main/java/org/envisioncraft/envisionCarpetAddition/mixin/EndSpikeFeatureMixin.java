package org.envisioncraft.envisionCarpetAddition.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {

    @Inject(
            method = "generateSpike",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGenerateSpike(
            ServerWorldAccess world,
            Random random,
            EndSpikeFeatureConfig config,
            EndSpikeFeature.Spike spike,
            CallbackInfo ci
    ) {
        if(EnvisionCarpetAdditionSettings.respawnDragonNoObsidianSpike) {
            generateCrystalOnly(world, random, config, spike);
            ci.cancel();
        }
    }

    @Unique
    private void generateCrystalOnly(
            ServerWorldAccess world,
            Random random,
            EndSpikeFeatureConfig config,
            EndSpikeFeature.Spike spike
    ){

        EndCrystalEntity endCrystalEntity = EntityType.END_CRYSTAL.create(
                world.toServerWorld(),
                SpawnReason.STRUCTURE
        );

        if (endCrystalEntity != null) {
            endCrystalEntity.setBeamTarget(config.getPos());
            endCrystalEntity.setInvulnerable(config.isCrystalInvulnerable());
            endCrystalEntity.refreshPositionAndAngles(
                    (double) spike.getCenterX() + (double) 0.5F,
                    (double) (spike.getHeight() + 1),
                    (double) spike.getCenterZ() + (double) 0.5F,
                    random.nextFloat() * 360.0F,
                    0.0F
            );
            world.spawnEntity(endCrystalEntity);

            // 3. 在水晶下方放置火
            // BlockPos crystalPos = endCrystalEntity.getBlockPos();
            // world.setBlockState(crystalPos.down(), Blocks.BEDROCK.getDefaultState(), 3);
            // world.setBlockState(crystalPos, Blocks.FIRE.getDefaultState(), 3);
        }
    }
}