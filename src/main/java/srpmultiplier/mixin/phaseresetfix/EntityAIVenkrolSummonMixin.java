package srpmultiplier.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIVenkrolSummon;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAIVenkrolSummon.class)
public abstract class EntityAIVenkrolSummonMixin {

    @Final
    @Shadow(remap = false)
    private EntityPStationaryArchitect parentEntity;

    @Redirect(
            method = "<init>",
            at = @At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private boolean fixPhaseResetMixin(){
        return !parentEntity.getEntityWorld().isRemote && SRPConfigSystems.useEvolution;
    }
}