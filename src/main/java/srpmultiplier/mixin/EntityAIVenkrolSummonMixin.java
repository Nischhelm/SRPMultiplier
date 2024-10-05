package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIVenkrolSummon;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAIVenkrolSummon.class)
public abstract class EntityAIVenkrolSummonMixin {

    @Redirect(
            method = "<init>",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    private SRPWorldData fixPhaseResetMixin(World world){
        if(!world.isRemote)
            return SRPWorldData.get(world);
        else
            return null;
    }

    @Redirect(
            method = "<init>",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap = false
    )
    private byte fixPhaseResetMixin(SRPWorldData instance){
        if(instance == null)
            return 10;
        else
            return instance.getEvolutionPhase();
    }
}