package me.iru.bedoverhaul.mixin;

import me.iru.bedoverhaul.util.ModifiedBedBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow private int sleepTimer;

    public PlayerEntityMixin(World world) {
        super(EntityType.PLAYER, world);
    }

    @Inject(method = "canResetTimeBySleeping()Z", at = @At("HEAD"), cancellable = true)
    public void bedoverhaul$canResetTimeBySleeping(CallbackInfoReturnable<Boolean> cir) {
        boolean isBedUpgraded = false;
        Optional<BlockPos> pos = this.getSleepingPosition();
        if(pos.isPresent()) {
            ModifiedBedBlockEntity bed = (ModifiedBedBlockEntity) this.getWorld().getBlockEntity(pos.get());
            if(bed != null) isBedUpgraded = bed.bedOverhaul$getCanResetTime();
        }
        cir.setReturnValue(this.isSleeping() && this.sleepTimer >= 100 && isBedUpgraded);
        cir.cancel();
    }

}
