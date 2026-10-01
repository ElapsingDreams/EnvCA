package org.envisioncraft.envisionCarpetAddition.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Minecraft 26.2 port of the old {@code EndermanEntity$PickUpBlockGoal} mixin.
 * <p>
 * Yarn 1.21.7 {@code net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal}
 * became Mojang {@code net.minecraft.world.entity.monster.EnderMan$EndermanTakeBlockGoal};
 * the {@code World.removeBlock(BlockPos, boolean)} call this hooks is still there.
 */
@Mixin(targets = "net.minecraft.world.entity.monster.EnderMan$EndermanTakeBlockGoal")
public abstract class EnderMan_TakeBlockGoalMixin {

    @Inject(
            method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"),
            cancellable = true
    )
    private void beforeRemoveBlock(CallbackInfo ci,
                                   @Local Level level,
                                   @Local BlockPos blockPos,
                                   @Local BlockState blockState) {
        Block block = blockState.getBlock();
        if (!EnvisionCarpetAdditionSettings.canEndermanPickUpMushroom && (block == Blocks.RED_MUSHROOM ||
                block == Blocks.BROWN_MUSHROOM ||
                block == Blocks.RED_MUSHROOM_BLOCK ||
                block == Blocks.BROWN_MUSHROOM_BLOCK)) {
            ci.cancel();
        }
    }
}
