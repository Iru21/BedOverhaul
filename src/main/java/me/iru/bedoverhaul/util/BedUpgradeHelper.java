package me.iru.bedoverhaul.util;

import net.minecraft.block.BedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BedUpgradeHelper {

    public enum BedUpgrade {
        SpawnPointUpgrade,
        TimeResetUpgrade
    }

    public static boolean upgradeFullBed(World world, BlockPos firstHalfPos, BedUpgrade upgrade) {
        ModifiedBedBlockEntity firstHalf = (ModifiedBedBlockEntity) world.getBlockEntity(firstHalfPos);
        ModifiedBedBlockEntity secondHalf = (ModifiedBedBlockEntity) world.getBlockEntity(
                firstHalfPos.offset(
                        BedBlock.getOppositePartDirection(world.getBlockState(firstHalfPos))
                )
        );
        if(firstHalf == null || secondHalf == null) {
            return false;
        }
        switch (upgrade) {
            case SpawnPointUpgrade -> {
                firstHalf.bedOverhaul$setCanSetSpawnPoint(true);
                secondHalf.bedOverhaul$setCanSetSpawnPoint(true);
            }
            case TimeResetUpgrade -> {
                firstHalf.bedOverhaul$setCanResetTime(true);
                secondHalf.bedOverhaul$setCanResetTime(true);
            }
        }
        return true;
    }
}
