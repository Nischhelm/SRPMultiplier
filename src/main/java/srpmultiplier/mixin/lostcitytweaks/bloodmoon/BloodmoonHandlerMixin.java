package srpmultiplier.mixin.lostcitytweaks.bloodmoon;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import lumien.bloodmoon.network.PacketHandler;
import lumien.bloodmoon.network.messages.MessageBloodmoonStatus;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(BloodmoonHandler.class)
public abstract class BloodmoonHandlerMixin {

    @Redirect(
            method="endWorldTick",
            at=@At(value="INVOKE",target = "Lnet/minecraft/world/WorldProvider;getDimension()I",ordinal = 0),
            remap = false
    )
    private int allowLCdimensionMixin(WorldProvider instance){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if(instance.getDimension() == 111)
                return 0;
        }
        return instance.getDimension();
    }

    @Inject(
            method="endWorldTick",
            at=@At(value="INVOKE",target = "Llumien/bloodmoon/server/BloodmoonSpawner;findChunksForSpawning(Lnet/minecraft/world/WorldServer;ZZZ)I"),
            remap=false
    )
    private void spawnParasitesMixin(TickEvent.WorldTickEvent event, CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if(!event.world.isRemote && SRPConfigSystems.useEvolution && SRPConfigSystems.phaseCustomSpawner) {
                WorldServer server = event.world.getMinecraftServer().getWorld(event.world.provider.getDimension());
                SRPWorldEntitySpawner.findChunksForSpawning(server, true, false, server.getWorldInfo().getWorldTotalTime() % 400L == 0L);
            }
        }
    }

    @Unique
    public int time = 0;

    @Inject(
            method="endWorldTick",
            at=@At(value="HEAD"),
            remap = false
    )
    private void saveWorldTimeMixin(TickEvent.WorldTickEvent event, CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC)
            this.time = (int)(event.world.getWorldTime() % 24000L);
    }

    @Redirect(
            method="endWorldTick",
            at=@At(value="INVOKE",target="Llumien/bloodmoon/server/BloodmoonHandler;isBloodmoonActive()Z"),
            remap = false
    )
    private boolean skipFirstBloodMoonTickInLCMixin(BloodmoonHandler instance){
        //This is needed so the blood moon message in else if(time==12000) is also sent in LC
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC)
            if(time==12000)
                return false;
        return instance.isBloodmoonActive();
    }

    @Shadow(remap = false)
    boolean bloodMoon;

    @Inject(
            method="updateClients",
            at=@At(value="HEAD"),
            remap=false
    )
    private void updateBloodmoonMixin(CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(this.bloodMoon), 111);
        }
    }

    @Inject(
            method="setBloodmoon",
            at=@At(value="HEAD"),
            remap=false
    )
    private void setBloodmoonMixin(boolean bloodMoon, CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.bloodmoonInLC){
            if (this.bloodMoon != bloodMoon) {
                PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(bloodMoon), 111);
            }
        }
    }
}