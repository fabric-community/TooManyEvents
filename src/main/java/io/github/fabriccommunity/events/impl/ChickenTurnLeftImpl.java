package io.github.fabriccommunity.events.impl;

import io.github.fabriccommunity.events.world.ChickenTurnLeftCallback;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

/**
 * @author leocth
 */
public final class ChickenTurnLeftImpl {
    private ChickenTurnLeftImpl() {

    }

    public static float hackChangeAngle(ChickenEntity entity, float headYaw, float targetYaw, float yawSpeed, ChickenTurnLeftCallback.TurnLeftType type) {
        float f = MathHelper.subtractAngles(headYaw, targetYaw);
        float g = MathHelper.clamp(f, -yawSpeed, yawSpeed);
        float h = headYaw + g;
        if (MathHelper.wrapDegrees(g) < 0f) {
            ActionResult result = ChickenTurnLeftCallback.EVENT.invoker().onTurnLeft(entity, headYaw, targetYaw, yawSpeed, type);

            if (result == ActionResult.FAIL) {
                // trolololololo lololooooo lo lo loo loooo lo lo
                h = headYaw - g;
            }
        }
        return MathHelper.wrapDegrees(h);
    }
}
