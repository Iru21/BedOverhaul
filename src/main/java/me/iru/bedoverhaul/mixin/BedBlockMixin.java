package me.iru.bedoverhaul.mixin;

import me.iru.bedoverhaul.util.BedUpgradeHelper;
import me.iru.bedoverhaul.util.ModifiedBedBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends HorizontalFacingBlock {

    @Unique
    private final Random random = Random.create();

    protected BedBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void bedoverhaul$applyBedUpgradeHandler(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ModifiedBedBlockEntity bed = (ModifiedBedBlockEntity) world.getBlockEntity(pos);
        if(bed != null) {
            boolean shouldUpgradeCompass = player.getStackInHand(hand).isOf(Items.COMPASS) && !bed.bedOverhaul$getCanSetSpawnPoint();
            boolean shouldUpgradeClock = player.getStackInHand(hand).isOf(Items.CLOCK) && !bed.bedOverhaul$getCanResetTime();
            boolean success = false;
            if(shouldUpgradeCompass) success = BedUpgradeHelper.upgradeFullBed(world, pos, BedUpgradeHelper.BedUpgrade.SpawnPointUpgrade);
            else if(shouldUpgradeClock) success = BedUpgradeHelper.upgradeFullBed(world, pos, BedUpgradeHelper.BedUpgrade.TimeResetUpgrade);
            if(success) {
                if(!player.getAbilities().creativeMode) {
                    player.getStackInHand(hand).decrement(1);
                }
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
                for (int i = 0; i < 5; ++i) {
                    double d = this.random.nextGaussian() * 0.02;
                    double e = this.random.nextGaussian() * 0.02;
                    double f = this.random.nextGaussian() * 0.02;
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY() + 1, pos.getZ(), d, e, f);
                }
                cir.setReturnValue(ActionResult.SUCCESS);
                cir.cancel();
            }
        }
    }
}
