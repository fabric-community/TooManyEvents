package io.github.fabriccommunity.events.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.fabriccommunity.events.play.PlayerAdvancementEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class AdvancementsImpl {
	private static final Map<Item, Event<PlayerAdvancementEvents.ItemDurabilityChanged>> ITEM_DURAB_EVENTS = new HashMap<>();
	private static final Map<EntityType<?>, Event<PlayerAdvancementEvents.PlayerHurtEntity>> HURT_ENTITY_EVENTS = new HashMap<>();
	private static final Map<EntityType<?>, Event<PlayerAdvancementEvents.PlayerKilledEntity>> KILL_ENTITY_EVENTS = new HashMap<>();

	public static Event<PlayerAdvancementEvents.ItemDurabilityChanged> itemDurabEvent(Item item) {
		return ITEM_DURAB_EVENTS.computeIfAbsent(item, i -> EventFactory.createArrayBacked(PlayerAdvancementEvents.ItemDurabilityChanged.class,
				listeners -> (player, stack, damage) -> {
			for (PlayerAdvancementEvents.ItemDurabilityChanged event : listeners) {
				event.onItemDurabilityChange(player, stack, damage);
			}
		}));
	}

	public static Event<PlayerAdvancementEvents.PlayerHurtEntity> hurtEntityEvent(EntityType<?> type) {
		return HURT_ENTITY_EVENTS.computeIfAbsent(type, t -> EventFactory.createArrayBacked(PlayerAdvancementEvents.PlayerHurtEntity.class,
				listeners -> (attacker, victim, source, dealt, taken, blocked) -> {
			for (PlayerAdvancementEvents.PlayerHurtEntity event : listeners) {
				event.onPlayerHurtEntity(attacker, victim, source, dealt, taken, blocked);
			}
		}));
	}

	public static Event<PlayerAdvancementEvents.PlayerKilledEntity> killedEntityEvent(EntityType<?> type) {
		return KILL_ENTITY_EVENTS.computeIfAbsent(type, t -> EventFactory.createArrayBacked(PlayerAdvancementEvents.PlayerKilledEntity.class,
				listeners -> (killer, victim, source) -> {
					for (PlayerAdvancementEvents.PlayerKilledEntity event : listeners) {
						event.onPlayerKillEntity(killer, victim, source);
					}
				}));
	}
}
