package me.iru.bedoverhaul.mixin;

import me.iru.bedoverhaul.util.ModifiedBedBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(SleepManager.class)
public abstract class SleepManagerMixin {

    @Shadow private int total;

    @Shadow private int sleeping;

    @Inject(method = "update(Ljava/util/List;)Z", at = @At("HEAD"), cancellable = true)
    public void bedoverhaul$gatedUpdated(List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir) {
        int i = this.total;
        int j = this.sleeping;
        this.total = 0;
        this.sleeping = 0;
        for (ServerPlayerEntity serverPlayerEntity : players) {
            if (serverPlayerEntity.isSpectator()) continue;
            ++this.total;
            if (!serverPlayerEntity.isSleeping()) continue;

            Optional<BlockPos> pos = serverPlayerEntity.getSleepingPosition();
            if(pos.isPresent()) {
                ModifiedBedBlockEntity bed = (ModifiedBedBlockEntity) serverPlayerEntity.getWorld().getBlockEntity(pos.get());
                if(bed != null && !bed.bedOverhaul$getCanResetTime()) continue;
            }

            ++this.sleeping;
        }
        cir.setReturnValue(!(j <= 0 && this.sleeping <= 0 || i == this.total && j == this.sleeping));
        cir.cancel();
    }

}
