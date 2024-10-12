package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRubble;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

@Mixin(BlockInfestedRubble.class)
public abstract class BlockInfestedRubbleMixin {

    @Unique
    EntityPlayer player;

    @Inject(
            method = "removedByPlayer",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void mixin(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, CallbackInfoReturnable<Boolean> cir){
        this.player = player;
    }

    @Redirect(
            method="removedByPlayer",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(world,player.getUniqueID());
        return data;
    }
}