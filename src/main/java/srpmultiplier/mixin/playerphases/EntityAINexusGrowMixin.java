package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.handlers.SRPWorldDataInterface;

@Mixin(EntityAINexusGrow.class)
public abstract class EntityAINexusGrowMixin {

    @Shadow(remap = false)
    @Final
    private EntityPStationaryArchitect parent;

    @Unique
    BlockPos blockPos;

    @Inject(
            method = "checkPhase",
            at = @At("HEAD"),
            remap = false
    )
    void mixin(CallbackInfo ci){
        this.blockPos = this.parent.getPosition();
    }

    @Redirect(
            method="checkPhase",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }
}