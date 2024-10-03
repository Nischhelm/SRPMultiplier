package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEndermanHead;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {

    @Redirect(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPInfected;cannotDespawn(Z)V"),
            remap = false
    )
    private static void endSimmermenDespawnMixin(EntityPInfected parasite, boolean b){
        if(SRPMultiplierConfigHandler.server.despawnEndSimmermen)
            if(parasite instanceof EntityInfEnderman || parasite instanceof EntityInfEndermanHead)
                if(parasite.world.provider.getDimension()==1)
                    b = true;
        parasite.cannotDespawn(b);
    }

    @Redirect(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/World;func_72838_d(Lnet/minecraft/entity/Entity;)Z"),
            remap = false
    )
    private static boolean simmermanCapMixin(World world, Entity entity){
        if(SRPMultiplierConfigHandler.server.endSimmermenCap>-1)
            if(entity instanceof EntityInfEnderman || entity instanceof EntityInfEndermanHead)
                if(world.provider.getDimension()==1){
                    int simmermancount = 0;
                    for(Entity e: world.loadedEntityList)
                        if(e instanceof EntityInfEnderman || e instanceof EntityInfEndermanHead)
                            ++simmermancount;

                    if(simmermancount >= SRPMultiplierConfigHandler.server.endSimmermenCap)
                        return false;
                }

        return world.spawnEntity(entity);
    }
}