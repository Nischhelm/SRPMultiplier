package srpmixins.mixin.potions;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public abstract class FearDmgCondition {
    @ModifyExpressionValue(
            method = "mobFear",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;onGround:Z")
    )
    private boolean srpmixins_modifyFearCondition(boolean original){
        return true; //is on ground -> no dmg multi if jumping/falling
    }
}
