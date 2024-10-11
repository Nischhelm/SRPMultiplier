package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import com.google.common.collect.Sets;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.List;
import java.util.Set;


@Mixin(SRPWorldEntitySpawner.class)
public abstract class SRPWorldEntitySpawnerMixin {

    @Unique
    private static int dimension;

    @Inject(
            method="findChunksForSpawning",
            at=@At(value="HEAD"),
            remap = false
    )
    private static void saveDimensionMixin(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir){
        dimension = worldServerIn.provider.getDimension();
    }

    @Redirect(
            method="findChunksForSpawning",
            at=@At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap=false
    )
    private static int increaseParasiteMobCapMixin(){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC && BloodmoonHandler.INSTANCE.isBloodmoonActive() && dimension==111)
            return SRPConfig.worldMobCap* SRPMultiplierConfigHandler.server.bloodmoonInLCmobCapMultiplier;
        return SRPConfig.worldMobCap;
    }

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

    @Shadow(remap = false)
    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        return false;
    }

    @Redirect(
            method = "findChunksForSpawning",
            at = @At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldEntitySpawner;canCreatureTypeSpawnAtLocation(Lnet/minecraft/entity/EntityLiving$SpawnPlacementType;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"),
            remap = false
    )
    private static boolean disableLazySpawnSlowedMixin(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos){
        if(SRPMultiplierConfigHandler.server.noLazySpawnsSlow && !SRPMultiplierConfigHandler.server.noLazySpawnsFast) {
            int i = pos.getX();
            int j = pos.getZ();

            boolean areaIsFullyLoaded = worldIn.isAreaLoaded(new BlockPos(i,0,j),32, true);
            if (!areaIsFullyLoaded)
                return false;
        }
        return canCreatureTypeSpawnAtLocation(spawnPlacementTypeIn, worldIn, pos);
    }

    @Shadow(remap = false)
    @Final
    private static Set<ChunkPos> eligibleChunksForSpawning;

    @Inject(
            method = "findChunksForSpawning",
            at = @At(value="INVOKE",target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;"),
            remap = false
    )
    private static void disableLazySpawnFastMixin(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir){
        if(SRPMultiplierConfigHandler.server.noLazySpawnsFast) {
            Set<ChunkPos> eligibleChunks = Sets.newHashSet();
            for (ChunkPos chunkPos : eligibleChunksForSpawning) {
                int i = chunkPos.x * 16;
                int j = chunkPos.z * 16;

                boolean areaIsFullyLoaded = worldServerIn.isAreaLoaded(new BlockPos(i,0,j),32, true);
                if (areaIsFullyLoaded)
                    eligibleChunks.add(chunkPos);
            }
            eligibleChunksForSpawning.clear();
            eligibleChunksForSpawning.addAll(eligibleChunks);
        }
    }
}