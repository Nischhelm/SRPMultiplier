package srpmultiplier.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAINexusGrow.class)
public abstract class EntityAINexusGrowMixin {

    @Shadow(remap = false)
    @Final
    private EntityPStationaryArchitect parent;

    @Redirect(
            method = "checkPhase",
            at = @At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private boolean fixPhaseResetMixin(){
        return !parent.getEntityWorld().isRemote && SRPConfigSystems.useEvolution;
    }
}