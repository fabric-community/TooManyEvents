package io.github.fabriccommunity.events.mixin.world;

import io.github.fabriccommunity.events.impl.ChickenTurnLeftImpl;
import io.github.fabriccommunity.events.world.ChickenTurnLeftCallback;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author leocth
 */
@Mixin(MoveControl.class)
public abstract class MixinMoveControl {

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/MoveControl;changeAngle(FFF)F", ordinal = 0),
            method = "tick"
    )
    private float redirectBodyChangeAngle(MoveControl self, float headYaw, float targetYaw, float yawSpeed) {
        return (entity instanceof ChickenEntity)
                ? ChickenTurnLeftImpl.hackChangeAngle((ChickenEntity) entity, headYaw, targetYaw, yawSpeed, ChickenTurnLeftCallback.TurnLeftType.BODY)
                : ChickenTurnLeftImpl.noHackChangeAngle(headYaw, targetYaw, yawSpeed);
    }

    @Shadow @Final private MobEntity entity;
}
