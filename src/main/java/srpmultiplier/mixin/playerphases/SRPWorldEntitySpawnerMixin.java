package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.handlers.SRPWorldDataInterface;

@Mixin(SRPWorldEntitySpawner.class)
public abstract class SRPWorldEntitySpawnerMixin {

    @Unique
    private static BlockPos blockPos;

    @Inject(
            method = "getSpawnListEntryForTypeAt",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin(WorldServer worldServerIn, BlockPos pos, CallbackInfoReturnable<Biome.SpawnListEntry> cir){
        blockPos = pos;
    }

    @Redirect(
            method="getSpawnListEntryForTypeAt",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    private static SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }
}