package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.SRPMultiplier;

@Mixin(SRPWorldData.class)
public abstract class SRPWoldDataMixin {

    @Shadow(remap = false)
    private static SRPWorldData create(World world, MapStorage storage) {
        return null;
    }

    @Redirect(
            method = "get",
            at = @At(value = "INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;create(Lnet/minecraft/world/World;Lnet/minecraft/world/storage/MapStorage;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    private static SRPWorldData getServerWorldDataMixin(World world, MapStorage storage) {
        SRPMultiplier.LOGGER.info("Creating new SRPWorldData for dim"+world.provider.getDimension());
        if(world.isRemote) {
            try {
                throw (new Exception("This Method called me"));
            } catch(Exception e){
                e.printStackTrace(System.out);
            }
        }
        return create(world, storage);
    }
}