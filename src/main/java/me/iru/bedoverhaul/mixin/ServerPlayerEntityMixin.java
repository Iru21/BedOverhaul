package me.iru.bedoverhaul.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.iru.bedoverhaul.util.ModifiedBedBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @WrapWithCondition(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"))
    private boolean bedoverhaul$onlySetSpawnPointIf(ServerPlayerEntity player, RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage) {
        ModifiedBedBlockEntity bed = (ModifiedBedBlockEntity) player.getWorld().getBlockEntity(pos);
        return bed != null && bed.bedOverhaul$getCanSetSpawnPoint();
    }

}
