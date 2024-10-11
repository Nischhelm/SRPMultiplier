package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
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

import java.util.Random;

@Mixin(ParasiteEventWorld.class)
public abstract class ParasiteEventWorldMixin {

    @Unique
    private static BlockPos blockPos;
    private static World world;

    @Inject(
            method = "placeHeartInWorld",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin(World worldIn, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        blockPos = pos;
        world = worldIn;
    }

    @Redirect(
            method="placeHeartInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    private static byte getPlayerDataMixin(SRPWorldData instance){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) instance).getByBlock(world,blockPos).getEvolutionPhase();
        return instance.getEvolutionPhase();
    }

    @Inject(
            method = "canInfestBlock",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin2(World worldIn, BlockPos pos, Random rand, int stage, boolean fromVenkrol, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="canInfestBlock",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    private static SRPWorldData getPlayerDataMixin2(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "spreadBiomeBlockStain",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin3(World worldIn, BlockPos pos, Random rand, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="spreadBiomeBlockStain",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    private static SRPWorldData getPlayerDataMixin3(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "spreadBiomeBlockTrunk",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin4(World worldIn, BlockPos pos, Random rand, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="spreadBiomeBlockTrunk",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    private static SRPWorldData getPlayerDataMixin4(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "placeColonyInWorld",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin5(World worldIn, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        blockPos = pos;
        world = worldIn;
    }

    @Redirect(
            method="placeColonyInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    private static byte getPlayerDataMixin5(SRPWorldData instance){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) instance).getByBlock(world,blockPos).getEvolutionPhase();
        return instance.getEvolutionPhase();
    }
}