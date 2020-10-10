package io.github.fabriccommunity.events.play;

import io.github.fabriccommunity.events.impl.AdvancementsImpl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class PlayerAdvancementEvents {

	/**
	 * An event run when a player is killed by an entity.
	 */
	public static final Event<EntityKilledPlayer> ENTITY_KILLED_PLAYER = EventFactory.createArrayBacked(EntityKilledPlayer.class, listeners -> (victim, killer, source) -> {
		for (EntityKilledPlayer listener : listeners) {
			listener.onEntityKillPlayer(victim, killer, source);
		}
	});

	/**
	 * An event run when a player's inventory is changed.
	 */
	public static final Event<InventoryChanged> INVENTORY_CHANGED = EventFactory.createArrayBacked(InventoryChanged.class, listeners -> (player, inventory, stack, full, empty, occupied) -> {
		for (InventoryChanged listener : listeners) {
			listener.onInventoryChange(player, inventory, stack, full, empty, occupied);
		}
	});

	/**
	 * An event run when the durability of a stack of *any* item is changed.
	 */
	public static final Event<ItemDurabilityChanged> ITEM_DURABILITY_CHANGED_WILDCARD = EventFactory.createArrayBacked(ItemDurabilityChanged.class,
			listeners -> (player, stack, damage) -> {
				for (ItemDurabilityChanged listener : listeners) {
					listener.onItemDurabilityChange(player, stack, damage);
				}
			});

	/**
	 * An event run when the durability of a stack of <item> is changed.
	 * @param item The item to listen for.
	 * @return The event for that type of item.
	 */
	public static Event<ItemDurabilityChanged> itemDurabilityChanged(Item item) {
		return AdvancementsImpl.itemDurabEvent(item);
	}

	/**
	 * An event run when a player hurts *any* type of entity.
	 */
	public static final Event<PlayerHurtEntity> PLAYER_HURT_ENTITY_WILDCARD = EventFactory.createArrayBacked(PlayerHurtEntity.class,
			listeners -> (attacker, victim, source, dealt, taken, blocked) -> {
				for (PlayerHurtEntity listener : listeners) {
					listener.onPlayerHurtEntity(attacker, victim, source, dealt, taken, blocked);
				}
			});

	/**
	 * An event run when a player hurts a specific type of entity.
	 * @param type The type of entity to listen for.
	 * @return The event for that entity type.
	 */
	public static Event<PlayerHurtEntity> playerHurtEntity(EntityType<?> type) {
		return AdvancementsImpl.hurtEntityEvent(type);
	}

	/**
	 * An event run when a player kills *any* type of entity.
	 */
	public static final Event<PlayerKilledEntity> PLAYER_KILLED_ENTITY_WILDCARD = EventFactory.createArrayBacked(PlayerKilledEntity.class,
			listeners -> (killer, victim, source) -> {
				for (PlayerKilledEntity listener : listeners) {
					listener.onPlayerKillEntity(killer, victim, source);
				}
			});

	/**
	 * An event run when a play kills a specific type of entity.
	 * @param type The type of entity to listen for.
	 * @return The event for that entity type.
	 */
	public static Event<PlayerKilledEntity> playerKilledEntity(EntityType<?> type) {
		return AdvancementsImpl.killedEntityEvent(type);
	}

	/**
	 * An event run when a player is killed by an entity.
	 * @author b0undary
	 */
	public interface EntityKilledPlayer {
		/**
		 * @param victim The player killed.
		 * @param killer The entity who killed the player.
		 * @param source The damage source used to kill.
		 */
		void onEntityKillPlayer(PlayerEntity victim, Entity killer, DamageSource source);
	}

	/**
	 * An event run when a player's inventory is changed.
	 * @author b0undary
	 */
	public interface InventoryChanged {
		/**
		 * @param player The player whose inventory changed.
		 * @param inventory The inventory changed.
		 * @param stack The stack that got changed.
		 * @param full How many full item stacks are in the inventory.
		 * @param empty How many empty item stacks are in the inventory.
		 * @param occupied How many slots are occupied in the inventory.
		 */
		void onInventoryChange(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied);
	}

	/**
	 * An event run when the durability of an item stack is changed.
	 * @author b0undary
	 */
	public interface ItemDurabilityChanged {
		/**
		 * @param player The player whose item durability changed.
		 * @param stack The stack that got changed.
		 * @param damage How much total damage the stack has now.
		 */
		void onItemDurabilityChange(ServerPlayerEntity player, ItemStack stack, int damage);
	}

	/**
	 * An event run when a player hurts an entity.
	 * @author b0undary
	 */
	public interface PlayerHurtEntity {
		/**
		 * @param attacker The player doing the hurting.
		 * @param victim The entity that got hurt.
		 * @param source The damage source used.
		 * @param dealt How much damage was dealt.
		 * @param taken How much damage was actually taken.
		 * @param blocked Whether the attack got blocked.
		 */
		void onPlayerHurtEntity(ServerPlayerEntity attacker, Entity victim, DamageSource source, float dealt, float taken, boolean blocked);
	}

	/**
	 * An event run when a play kills an entity.
	 * @author b0undary
	 */
	public interface PlayerKilledEntity {
		/**
		 * @param killer The player doing the killing.
		 * @param victim The entity that got killed.
		 * @param source The damage source used to kill.
		 */
		void onPlayerKillEntity(ServerPlayerEntity killer, Entity victim, DamageSource source);
	}
}
