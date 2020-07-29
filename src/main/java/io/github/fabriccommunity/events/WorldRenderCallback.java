package io.github.fabriccommunity.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;

/**
 * Called at the end of vanilla world rendering, to allow for mods to add their own rendering.
 * @author Valoeghese
 * @apiNote this event is client side only.
 */
@FunctionalInterface
public interface WorldRenderCallback {
	Event<WorldRenderCallback> EVENT = EventFactory.createArrayBacked(WorldRenderCallback.class, listeners -> (world, matrices, tickDelta, limitTime, renderBlockOutline, renderer, projectionMatrix) -> {
		for (WorldRenderCallback listener : listeners) {
			listener.onWorldRender(world, matrices, tickDelta, limitTime, renderBlockOutline, renderer, projectionMatrix);
		}
	});

	/**
	 * Called on world render.
	 *
	 * @param world use this one for world stuff.
	 * @param matrices the matrix stack for epic matrix operations.
	 * @param tickDelta the delta between adjacent ticks.
	 * @param limitTime I don't even know what this does.
	 * @param renderBlockOutline whether to render the block outline.
	 * @param renderer use this to get the camera and stuff.
	 * @param projectionMatrix the projection matrix.
	 */
	void onWorldRender(ClientWorld world, MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, GameRenderer renderer, Matrix4f projectionMatrix);
}
