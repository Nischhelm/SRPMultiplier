package srpmultiplier.mixin;

import com.google.common.collect.Sets;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.Set;

@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawnerMixin {

    @Redirect(
            method = "findChunksForSpawning",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/WorldServer;canCreatureTypeSpawnHere(Lnet/minecraft/entity/EnumCreatureType;Lnet/minecraft/world/biome/Biome$SpawnListEntry;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean disableLazySpawnSlowedMixin(WorldServer instance, EnumCreatureType creatureType, Biome.SpawnListEntry spawnListEntry, BlockPos pos){
        if(SRPMultiplierConfigHandler.server.noLazySpawnsSlow && !SRPMultiplierConfigHandler.server.noLazySpawnsFast) {
            int i = pos.getX();
            int j = pos.getZ();

            StructureBoundingBox bb = new StructureBoundingBox(i - 32, 0, j - 32, i + 32, 0, j + 32);
            boolean areaIsFullyLoaded = instance.isAreaLoaded(bb, true);
            if (!areaIsFullyLoaded)
                return false;
        }
        return instance.canCreatureTypeSpawnHere(creatureType, spawnListEntry, pos);
    }

    @Shadow
    @Final
    private Set<ChunkPos> eligibleChunksForSpawning;

    @Inject(
            method = "findChunksForSpawning",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/WorldServer;getSpawnPoint()Lnet/minecraft/util/math/BlockPos;")
    )
    private void disableLazySpawnFastMixin(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir){
        if(SRPMultiplierConfigHandler.server.noLazySpawnsFast) {
            Set<ChunkPos> eligibleChunks = Sets.newHashSet();
            for (ChunkPos chunkPos : eligibleChunksForSpawning) {
                int i = chunkPos.x * 16;
                int j = chunkPos.z * 16;
                StructureBoundingBox bb = new StructureBoundingBox(i - 32, 0, j - 32, i + 32, 0, j + 32);

                boolean areaIsFullyLoaded = worldServerIn.isAreaLoaded(bb, true);
                if (areaIsFullyLoaded)
                    eligibleChunks.add(chunkPos);
            }
            eligibleChunksForSpawning.clear();
            eligibleChunksForSpawning.addAll(eligibleChunks);
        }
    }
}