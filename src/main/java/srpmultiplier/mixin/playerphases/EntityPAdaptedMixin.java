package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.Entity;
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

@Mixin(EntityPAdapted.class)
public abstract class EntityPAdaptedMixin extends Entity {

    @Unique
    BlockPos blockPos;

    public EntityPAdaptedMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "func_70623_bb",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void saveBlockPosMixin(CallbackInfo ci){
        this.blockPos = this.getPosition();
    }

    @Redirect(
            method="func_70623_bb",
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