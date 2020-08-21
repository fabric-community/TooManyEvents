package io.github.fabriccommunity.events.world;

import java.util.concurrent.atomic.AtomicReference;

import io.github.fabriccommunity.events.impl.EntitySpawnImpl;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorldAccess;

/**
 * Collection of events for entity spawning.
 */
public final class EntitySpawnCallback {
	/**
	 * Callback for before the entity spawns. Use this to cancel, force succeed, or alter the entity spawning.
	 */
	public static final Event<EntitySpawnCallback.Pre> PRE = EventFactory.createArrayBacked(EntitySpawnCallback.Pre.class, listeners -> (original, entity, world, reason) -> EntitySpawnImpl.eventPre(original, entity, world, reason, listeners));

	/**
	 * Callback for after the entity succeeds in spawning. Use this for general functions after an entity has spawned.
	 */
	public static final Event<EntitySpawnCallback.Post> POST = EventFactory.createArrayBacked(EntitySpawnCallback.Post.class, listeners -> (entity, level, pos, reason) -> {
		for (EntitySpawnCallback.Post callback : listeners) {
			callback.onEntitySpawnPost(entity, level, pos, reason);
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
		 * @param reason the cause of the spawning.
		 * @return <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to instantly succeed in spawning the entity in the world at its specified position.<br/>
		 * <li>{@code PASS} to leave SUCCESS/FAIL handling to subsequent events. If all events PASS, the action is determined to be a SUCCESS.<br/>
		 * <li>{@code FAIL} cancel spawning the entity.
		 * </ul>
		 */
		ActionResult onEntitySpawnPre(Entity original, AtomicReference<Entity> entity, ServerWorldAccess world, SpawnReason reason);
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
		 * @param reason the cause for the entity spawn.
		 */
		void onEntitySpawnPost(Entity entity, ServerWorldAccess world, Vec3d pos, SpawnReason reason);
	}
}
