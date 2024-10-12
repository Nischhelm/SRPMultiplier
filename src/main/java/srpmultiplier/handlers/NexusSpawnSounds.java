package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.*;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NexusSpawnSounds {

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void soundsOnSpecialSpawns(LivingSpawnEvent.SpecialSpawn event){
        if(SRPMultiplierConfigHandler.server.playsounds) {
            Entity e = event.getEntity();
            if (!event.isCanceled() & SRPConfigSystems.rsSounds) {
                if (e instanceof EntityVenkrolSIV)
                    event.getEntity().playSound(SRPSounds.VENKROLSIV, 100.0F, 1.0F);
                if (e instanceof EntityVenkrolSIII)
                    event.getEntity().playSound(SRPSounds.VENKROLSIII, 100.0F, 1.0F);
                if (e instanceof EntityVenkrolSII)
                    event.getEntity().playSound(SRPSounds.VENKROLSII, 100.0F, 1.0F);
                if (e instanceof EntityDodSIV)
                    event.getEntity().playSound(SRPSounds.DODSIV, 100.0F, 1.0F);
                if (e instanceof EntityDodSIII)
                    event.getEntity().playSound(SRPSounds.DODSIII, 100.0F, 1.0F);
                if (e instanceof EntityDodSII)
                    event.getEntity().playSound(SRPSounds.DODSII, 100.0F, 1.0F);
            }
        }
    }
}