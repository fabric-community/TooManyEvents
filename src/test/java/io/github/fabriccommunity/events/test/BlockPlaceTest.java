package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.play.ItemEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;

/**
 * @author Valoeghese
 */
public class BlockPlaceTest implements ModInitializer {
	public static boolean ENABLED = true;

	@Override
	public void onInitialize() {
		if (ENABLED) {
			ItemEvents.PLACED.register((context, original, state) -> {
				if (context.getBlockPos().getY() == 63) {
					return ActionResult.FAIL;
				} else if (context.getBlockPos().getY() == 64) {
					state.set(Blocks.GOLD_BLOCK.getDefaultState());
					return ActionResult.SUCCESS;
				}

				return ActionResult.PASS;
			});
		}
	}
}
