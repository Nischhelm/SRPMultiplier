package srpmultiplier.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {

    @Inject(
            method="entityPlayer",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    void fixPhaseResetMixin(PlayerInteractEvent.EntityInteractSpecific event, CallbackInfo ci){
        ci.cancel();
    }
}