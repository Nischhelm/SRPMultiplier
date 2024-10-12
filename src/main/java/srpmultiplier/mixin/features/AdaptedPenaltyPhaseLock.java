package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityPAdapted.class)
public abstract class AdaptedPenaltyPhaseLock {

    @Redirect(
            method="func_70623_bb",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setTotalKills(IZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    boolean phaseLockMixin(SRPWorldData data, int in, boolean plus, World worldIn, boolean canChangePhase){
        int startPhase = SRPMultiplierConfigHandler.server.adaptedDespawnPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase()<startPhase)
            return false;
        return data.setTotalKills(in,plus,worldIn,canChangePhase);
    }
}