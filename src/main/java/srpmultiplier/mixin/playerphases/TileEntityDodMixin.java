package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.tile.TileEntityDod;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

@Mixin(TileEntityDod.class)
public abstract class TileEntityDodMixin extends TileEntity {

    @Redirect(
            method="func_73660_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,this.getPos());
        return data;
    }

}