package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

@Mixin(SRPEffectBase.class)
public abstract class SRPEffectBaseMixin {

    @Unique
    BlockPos blockPos;

    @Inject(
            method = "effectCOTH",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void saveBlockPosMixin(EntityLivingBase entity, int amplifier, CallbackInfo ci){
        this.blockPos = entity.getPosition();
    }

    @Redirect(
            method="effectCOTH",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "effectPrey",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void saveBlockPosMixin2(EntityLivingBase entity, int amplifier, CallbackInfo ci){
        this.blockPos = entity.getPosition();
    }

    @Redirect(
            method="effectPrey",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin2(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

}