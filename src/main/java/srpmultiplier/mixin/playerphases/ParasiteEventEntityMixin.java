package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.handlers.SRPWorldDataInterface;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {

    @Unique
    private static BlockPos blockPos;
    private static World world;

    @Inject(
            method = "merge",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin(EntityParasiteBase entityin, int code, String name, CallbackInfoReturnable<Boolean> cir){
        blockPos = entityin.getPosition();
    }

    @Redirect(
            method="merge",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    private static SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "spawnBeckonE",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin2(SRPWorldData data, World worldIn, EntityParasiteBase in, CallbackInfo ci){
        blockPos = in.getPosition();
        world = worldIn;
    }

    @Redirect(
            method="spawnBeckonE",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    private static byte getPlayerDataMixin2(SRPWorldData instance){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) instance).getByBlock(world,blockPos).getEvolutionPhase();
        return instance.getEvolutionPhase();
    }

}