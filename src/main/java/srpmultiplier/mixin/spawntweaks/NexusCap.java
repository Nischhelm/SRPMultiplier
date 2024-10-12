package srpmultiplier.mixin.spawntweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.List;


@Mixin(SRPWorldEntitySpawner.class)
public abstract class NexusCap {

    @Redirect(
            method = "findChunksForSpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;"),
            remap = false
    )
    private static Event.Result nexusCapMixin(EntityLiving entity, World world, float x, float y, float z, boolean isSpawner){
        Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entity, world, x, y, z, false);

        int nexusCap = SRPMultiplierConfigHandler.server.nexusCap;
        if(nexusCap > 0 && entity instanceof EntityPStationaryArchitect) {
            List<Entity> entityList = world.loadedEntityList;
            int nexusCounter = 0;

            for (Entity e : entityList)
                if (e instanceof EntityPStationaryArchitect)
                    ++nexusCounter;

            if (nexusCounter > nexusCap)
                canSpawn = Event.Result.DENY;
        }

        return canSpawn;
    }
}