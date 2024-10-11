package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.handlers.SRPWorldDataInterface;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionMixin {

    @Unique
    private static BlockPos blockPos;
    @Unique
    private static World world;

    @Inject(
            method = "onSpawn",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci){
        blockPos = event.getEntity().getPosition();
        world = event.getWorld();
    }

    @Redirect(
            method="onSpawn",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    private static byte getPlayerDataMixin(SRPWorldData instance){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) instance).getByBlock(world,blockPos).getEvolutionPhase();
        return instance.getEvolutionPhase();
    }
}