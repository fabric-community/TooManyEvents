package io.github.fabriccommunity.events.mixin.spawn;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.server.world.ServerWorld;

/**
 * @author Valoeghese
 */
@Mixin(SummonCommand.class)
public class MixinSummonCommand {
	@Redirect(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_30736(Lnet/minecraft/entity/Entity;)Z"),
			method = "execute(Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/util/Identifier;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/nbt/CompoundTag;Z)I")
	private static boolean entitySpawnEventCommand(ServerWorld self, Entity entity) {
		return EntitySpawnImpl.spawnEntityZ(self, entity);
	}
}
