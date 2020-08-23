package io.github.fabriccommunity.events.test;

import io.github.fabriccommunity.events.world.ChickenTurnLeftCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;

/**
 * @author leocth
 */
public class ChickenTurnLeftTest implements ModInitializer {
    public static final boolean ENABLED = true;

    @Override
    public void onInitialize() {
        if (ENABLED) {
            ChickenTurnLeftCallback.EVENT.register((chicken, oldYaw, targetYaw, yawSpeed, type) -> {
                if (type == ChickenTurnLeftCallback.TurnLeftType.HEAD) {
                    // disable this if you want to
                    System.out.println(chicken + " turned left by " + Math.abs(oldYaw - targetYaw) + "! Adjusting...");
                    return ActionResult.FAIL;
                }
                return ActionResult.PASS;
            });
        }
    }
}
