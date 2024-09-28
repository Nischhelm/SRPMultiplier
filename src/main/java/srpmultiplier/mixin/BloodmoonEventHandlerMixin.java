package srpmultiplier.mixin;

import lumien.bloodmoon.handler.BloodmoonEventHandler;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(BloodmoonEventHandler.class)
public abstract class BloodmoonEventHandlerMixin {

    @Redirect(
            method="loadWorld",
            at=@At(value="INVOKE",target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap=false
    )
    private int bloodmoonInLCMixin(WorldProvider instance){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if(instance.getDimension() == 111) return 0;
        }
        return instance.getDimension();
    }

    @Redirect(
            method="livingUpdate",
            at=@At(value="FIELD",target = "Lnet/minecraft/entity/EntityLivingBase;dimension:I"),
            remap=false
    )
    private int killBloodMobsMixin(EntityLivingBase instance){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if(instance.dimension == 111) return 0;
        }
        return instance.dimension;
    }
}