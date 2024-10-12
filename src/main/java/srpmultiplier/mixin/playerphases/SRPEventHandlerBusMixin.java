package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {

    @Unique
    EntityPlayer player;
    @Unique
    BlockPos blockPos;
    @Unique
    World world;

    @Inject(
            method = "cropGrow",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap = false
    )
    void saveBlockPosMixin(BlockEvent.CropGrowEvent.Pre event, CallbackInfo ci){
        this.blockPos = event.getPos();
        this.world = event.getWorld();
    }

    @Redirect(
            method="cropGrow",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    public byte getPlayerDataMixin(SRPWorldData instance){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) instance).getByBlock(world,blockPos).getEvolutionPhase();
        return instance.getEvolutionPhase();
    }

    @Inject(
            method = "playerFishing",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void savePlayerMixin2(ItemFishedEvent event, CallbackInfo ci){
        this.player = event.getEntityPlayer();
    }

    @Redirect(
            method="playerFishing",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin2(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(world,player.getUniqueID());
        return data;
    }

    @Inject(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap = false
    )
    void saveBlockPosMixin3(EntityParasiteBase entity, String mobname, boolean flagNC, SRPWorldData data, CallbackInfo ci){
        this.blockPos = entity.getPosition();
    }

    @Inject(
            method = "onEntitySpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;setNewParasiteTask(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;Ljava/lang/String;ZLcom/dhanantry/scapeandrunparasites/world/SRPWorldData;)V"),
            remap = false
    )
    void saveWorldMixin3(EntityJoinWorldEvent event, CallbackInfo ci){
        this.world = event.getWorld();
    }

    @Redirect(
            method="setNewParasiteTask",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getEvolutionPhase()B"),
            remap=false
    )
    public byte getPlayerDataMixin3(SRPWorldData data){
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos).getEvolutionPhase();
        return data.getEvolutionPhase();
    }

    @Inject(
            method = "setLoot",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void saveBlockPosMixin4(LivingDropsEvent event, CallbackInfo ci){
        this.blockPos = event.getEntity().getPosition();
        this.world = event.getEntity().world;
    }

    @Redirect(
            method="setLoot",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin4(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByBlock(world,blockPos);
        return data;
    }

    @Inject(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap = false
    )
    void savePlayerMixin5(PlayerWakeUpEvent event, CallbackInfo ci){
        this.player = event.getEntityPlayer();
    }

    @Redirect(
            method="playerUp",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;"),
            remap=false
    )
    public SRPWorldData getPlayerDataMixin5(World world){
        SRPWorldData data = SRPWorldData.get(world);
        if(SRPMultiplierConfigHandler.server.playerPhases)
            return ((SRPWorldDataInterface) data).getByPlayer(world,player.getUniqueID());
        return data;
    }



}