package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.*;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EndermanHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(SRPMultiplierConfigHandler.server.endermanDmgVsParasites > -1.0) {
            Entity e = event.getEntity();
            World world = e.world;
            if(world.provider.getDimension()==1 && e instanceof EntityParasiteBase) {
                Entity attacker = event.getSource().getTrueSource();
                if (attacker instanceof EntityEnderman)
                    event.setAmount((float) (event.getAmount() * SRPMultiplierConfigHandler.server.endermanDmgVsParasites));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event){
        if(!event.isCanceled() && SRPMultiplierConfigHandler.server.endermanRageChance > 0.0 && SRPMultiplierConfigHandler.server.endermanRageLevel>0) {
            Entity e = event.getEntity();
            World world = event.getWorld();
            if(world.provider.getDimension()==1 && e instanceof EntityEnderman) {
                float chance = SRPMultiplierConfigHandler.server.endermanRageChance;
                if (chance >= 1.0f || world.rand.nextFloat() < chance) {
                    ((EntityEnderman) e).addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, 100000, SRPMultiplierConfigHandler.server.endermanRageLevel + 1));
                    ((EntityEnderman) e).targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) e, EntityParasiteBase.class, true));
                }
            }
        }
    }
}