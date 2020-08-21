package io.github.fabriccommunity.events.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * @author Valoeghese
 */
@Mixin(EntityType.class)
public class MixinEntityType {
	@Overwrite
	public Entity spawn(ServerWorld serverWorld, CompoundTag itemTag, Text name, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
		Entity entity = ((EntityType) (Object) this).create(serverWorld, itemTag, name, player, pos, spawnReason, alignPosition, invertY);
		return EntitySpawnImpl.spawnEntityE(entity, serverWorld, spawnReason);
	}
}
