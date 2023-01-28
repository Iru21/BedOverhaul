package me.iru.bedoverhaul.mixin;

import me.iru.bedoverhaul.util.ModifiedBedBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BedBlockEntity.class)
public abstract class BedBlockEntityMixin extends BlockEntity implements ModifiedBedBlockEntity {

    private boolean canResetTime = false;
    private boolean canSetSpawnPoint = false;

    public BedBlockEntityMixin(BlockEntityType<BedBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Boolean getCanResetTime() {
        return this.canResetTime;
    }

    @Override
    public void setCanResetTime(Boolean value) {
        this.canResetTime = value;
    }

    @Override
    public Boolean getCanSetSpawnPoint() {
        return this.canSetSpawnPoint;
    }

    @Override
    public void setCanSetSpawnPoint(Boolean value) {
        this.canSetSpawnPoint = value;
    }

    public void readNbt(NbtCompound nbt) {
        this.canResetTime = nbt.getBoolean("canResetTime");
        this.canSetSpawnPoint = nbt.getBoolean("canSetSpawnPoint");
    }

    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("canResetTime", this.canResetTime);
        nbt.putBoolean("canSetSpawnPoint", this.canSetSpawnPoint);
    }
}
