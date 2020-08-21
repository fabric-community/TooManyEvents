package io.github.fabriccommunity.events;

import java.util.concurrent.atomic.AtomicReference;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldAccess;

/**
 * Collection of events for entity spawning.
 */
public final class EntitySpawnCallback {
	/**
	 * Callback for before the entity spawns. Use this to cancel, force succeed, or alter the entity spawning.
	 */
	public static final Event<EntitySpawnCallback.Pre> PRE = EventFactory.createArrayBacked(EntitySpawnCallback.Pre.class, listeners -> (original, entity, world, natural) -> {
		for (EntitySpawnCallback.Pre callback : listeners) {
			ActionResult result = callback.onEntitySpawnPre(original, entity, world, natural);

			if (result == ActionResult.CONSUME) {
				return ActionResult.SUCCESS;
			} else if (result != ActionResult.PASS) {
				return result;
			}
		}

		return ActionResult.SUCCESS;
	});

	/**
	 * Callback for after the entity succeeds in spawning. Use this for general functions after an entity has spawned.
	 */
	public static final Event<EntitySpawnCallback.Post> POST = EventFactory.createArrayBacked(EntitySpawnCallback.Post.class, listeners -> (entity, level, pos, natural) -> {
		for (EntitySpawnCallback.Post callback : listeners) {
			callback.onEntitySpawnPost(entity, level, pos, natural);
		}
	});

	/**
	 * Callback for before the entity spawns. Use this to cancel, force succeed, or alter the entity spawning.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface Pre {
		/**
		 * @param original the entity that was originally going to spawn.
		 * @param entity the entity that is going to spawn. If this is different to {@code original}, then a mod has modified the entity to spawn.
		 * @param world the world in which the entity is to spawn.
		 * @param natural if this is natural spawning. If this is false it implies that the spawning is caused by some event, such as a monster spawner or spawn egg.
		 * @return <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to instantly succeed in spawning the entity in the world at its specified position.<br/>
		 * <li>{@code PASS} to leave SUCCESS/FAIL handling to subsequent events. If all events PASS, the action is determined to be a SUCCESS.<br/>
		 * <li>{@code FAIL} cancel spawning the entity.
		 * </ul>
		 */
		ActionResult onEntitySpawnPre(Entity original, AtomicReference<Entity> entity, WorldAccess world, boolean natural);
	}

	/**
	 * Callback for after the entity succeeds in spawning. Use this for general functions after an entity has spawned.
	 * @author Valoeghese
	 */
	@FunctionalInterface
	public interface Post {
		/**
		 * @param entity the entity that has spawned.
		 * @param world the world in which the entity spawned.
		 * @param pos the position at which the entity spawned.
		 * @param natural if this was a natural spawn. If this is false it implies that the spawning is caused by some event, such as a monster spawner or spawn egg.
		 */
		void onEntitySpawnPost(Entity entity, WorldAccess world, Vec3d pos, boolean natural);
	}
}
