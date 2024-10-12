package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {

    @Unique
    EntityPlayer player;
    @Unique
    World world;

    @Inject(
            method = "func_184881_a",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap = false
    )
    void saveBlockPosMixin(MinecraftServer server, ICommandSender sender, String[] argString, CallbackInfo ci){
        this.player = (EntityPlayer) sender.getCommandSenderEntity();
        this.world = sender.getEntityWorld();
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    public byte getPlayerDataMixin(SRPWorldData data){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(world,player.getUniqueID()).getEvolutionPhase();
        return data.getEvolutionPhase();
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getTotalKills()I"),
            remap=false
    )
    public int getPlayerDataMixin2(SRPWorldData data){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(world,player.getUniqueID()).getTotalKills();
        return data.getTotalKills();
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getCooldown(Lnet/minecraft/world/World;)I"),
            remap=false
    )
    public int getPlayerDataMixin3(SRPWorldData data, World worldIn){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(worldIn,player.getUniqueID()).getCooldown(worldIn);
        return data.getCooldown(worldIn);
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setTotalKills(IZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    public boolean getPlayerDataMixin4(SRPWorldData data, int in, boolean plus, World worldIn, boolean canChangePhase){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(worldIn,player.getUniqueID()).setTotalKills(in,plus,worldIn,canChangePhase);
        return data.setTotalKills(in,plus,worldIn,canChangePhase);
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setCooldown(ILnet/minecraft/world/World;)V"),
            remap=false
    )
    public void getPlayerDataMixin5(SRPWorldData data, int in, World worldIn){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            ((SRPWorldDataInterface) data).getByPlayer(worldIn,player.getUniqueID()).setCooldown(in,worldIn);
        else
            data.setCooldown(in,worldIn);
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setEvolutionPhase(BZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    public boolean getPlayerDataMixin6(SRPWorldData data, byte in, boolean override, World worldIn, boolean canChangePhase){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(worldIn,player.getUniqueID()).setEvolutionPhase(in,override,worldIn,canChangePhase);
        return data.setEvolutionPhase(in,override,worldIn,canChangePhase);
    }
}