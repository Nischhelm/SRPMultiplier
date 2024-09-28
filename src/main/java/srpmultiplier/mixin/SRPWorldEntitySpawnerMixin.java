package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPWorldEntitySpawner.class)
public abstract class SRPWorldEntitySpawnerMixin {

    @Unique
    private static int dimension;

    @Inject(
            method="findChunksForSpawning",
            at=@At(value="HEAD"),
            remap = false
    )
    private static void saveDimensionMixin(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir){
        dimension = worldServerIn.provider.getDimension();
    }

    @Redirect(
            method="findChunksForSpawning",
            at=@At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap=false
    )
    private static int increaseParasiteMobCapMixin(){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension==111)
            return SRPConfig.worldMobCap* SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCap;
    }
}