package io.github.fabriccommunity.events;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicReference;

import com.mojang.authlib.GameProfile;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;

/**
 * Called on player join. Use this to deny players from logging in, or to perform other various player login tasks.
 * @author Valoeghese
 * @apiNote note that this event is called before player ban and whitelist handling is run.
 */
@FunctionalInterface
public interface PlayerLoginCallback {
	Event<PlayerLoginCallback> EVENT = EventFactory.createArrayBacked(PlayerLoginCallback.class, listeners -> (denyText, address, profile) -> {
		for (PlayerLoginCallback callback : listeners) {
			if (callback.onPlayerLogin(denyText, address, profile)) {
				return true;
			}
		}

		return false;
	});

	/**
	 * Called on player login. Return true to override further event processing and use, however keep in mind this may impact mods that need this event to listen to player login.
	 * @param denyText if null when event processing ends, the player is let through. Otherwise, the player is denied with this {@link Text} as a reason.
	 * @param address the socket address of the player.
	 * @param profile the game profile of the player.
	 * @return whether to cancel further event processing and continue in the current state.
	 */
	boolean onPlayerLogin(AtomicReference<Text> denyText, SocketAddress address, GameProfile profile);
}
