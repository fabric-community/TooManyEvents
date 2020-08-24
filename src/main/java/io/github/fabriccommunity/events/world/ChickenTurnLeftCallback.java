package io.github.fabriccommunity.events.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.ActionResult;

/**
 * You deserved this.
 * @author leocth
 */
@FunctionalInterface
public interface ChickenTurnLeftCallback {
    Event<ChickenTurnLeftCallback> EVENT = EventFactory.createArrayBacked(ChickenTurnLeftCallback.class, listeners -> (chicken, oldYaw, targetYaw, yawSpeed, type) -> {
        for (ChickenTurnLeftCallback listener : listeners) {
            ActionResult result = listener.onTurnLeft(chicken, oldYaw, targetYaw, yawSpeed, type);
            if (result != ActionResult.PASS)
                return result;
        }
        return ActionResult.PASS;
    });

    /**
     * Called when a chicken ({@code ChickenEntity}) turns left.
     *
     * @param chicken the chicken in question
     * @param oldYaw the chicken's previous head yaw
     * @param targetYaw the chicken's target yaw (new yaw)
     * @param yawSpeed the speed (limiter, to be exact) of the rotation.
     * @param type the type of the turn (head, body)
     *
     * @return
     * {@code SUCCESS} and {@code CONSUME} cancels further processing and allows the chicken to turn.
     * {@code PASS} pass event handling on to further processing. If all listeners pass, functions exactly the same as {@code SUCCESS} and {@code CONSUME}.
     * {@code FAIL} cancels further processing and forces the chicken to turn right by {@code |oldYaw - targetYaw|} instead.
     */
    ActionResult onTurnLeft(ChickenEntity chicken, float oldYaw, float targetYaw, float yawSpeed, TurnLeftType type);

    enum TurnLeftType {
        HEAD, BODY
    }
}
