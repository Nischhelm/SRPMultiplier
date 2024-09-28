package srpmultiplier.mixin;

import lumien.bloodmoon.client.ClientBloodmoonHandler;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(ClientBloodmoonHandler.class)
public abstract class ClientBloodmoonHandlerMixin {

    @Redirect(
            method="clientTick",
            at=@At(value="INVOKE",target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap=false
    )
    private int bloodmoonInLCMixin(WorldProvider instance){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if(instance.getDimension() == 111) return 0;
        }
        return instance.getDimension();
    }
}