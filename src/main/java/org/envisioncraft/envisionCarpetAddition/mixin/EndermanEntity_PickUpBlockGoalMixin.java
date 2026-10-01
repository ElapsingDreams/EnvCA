package org.envisioncraft.envisionCarpetAddition.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.envisioncraft.envisionCarpetAddition.EnvisionCarpetAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity$PickUpBlockGoal")
public abstract class EndermanEntity_PickUpBlockGoalMixin{

    @Inject(
            method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"),
            cancellable = true
    )
    private void beforeRemoveBlock(CallbackInfo ci,
                                   @Local World world,
                                   @Local BlockPos blockPos,
                                   @Local BlockState blockState) {
        // 检查是否为蘑菇方块
        Block block = blockState.getBlock();
        if (!EnvisionCarpetAdditionSettings.canEndermanPickUpMushroom && (block == Blocks.RED_MUSHROOM ||
                block == Blocks.BROWN_MUSHROOM ||
                block == Blocks.RED_MUSHROOM_BLOCK ||
                block == Blocks.BROWN_MUSHROOM_BLOCK)) {
            /*

            if (world instanceof ServerWorld serverWorld) {
                Text message = Text.literal("§c取消末影人抱蘑菇 §7(" +
                        blockPos.getX() + ", " +
                        blockPos.getY() + ", " +
                        blockPos.getZ() + ")");
                serverWorld.getServer().getPlayerManager().broadcast(message, false);
            }
            */

            ci.cancel();
        }
    }
}
